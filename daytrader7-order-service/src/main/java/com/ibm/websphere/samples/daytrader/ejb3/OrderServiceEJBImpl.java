/**
 * (C) Copyright IBM Corporation 2015, 2021
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.websphere.samples.daytrader.ejb3;

import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;
import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.interfaces.OrderService;
import com.ibm.websphere.samples.daytrader.restclient.AccountClient;
import com.ibm.websphere.samples.daytrader.restclient.HoldingClient;
import com.ibm.websphere.samples.daytrader.restclient.QuoteClient;
import com.ibm.websphere.samples.daytrader.util.Log;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrderServiceEJBImpl implements OrderService {

  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  @RestClient
  HoldingClient holdingClient;

  @Inject
  @RestClient
  AccountClient accountClient;

  @Inject
  @RestClient
  QuoteClient quoteClient;

  @Inject
  private Log Log;

  public static final int ROUND = BigDecimal.ROUND_HALF_UP;
  public static final int SCALE = 2;
  private static final BigDecimal ORDER_FEE = new BigDecimal("24.95");

  @Override
  public OrderDataBean sell(final String userId, final Integer holdingId) {
    OrderDataBean order;

    try {
      if (Log.doTrace()) {
        Log.trace("TradeSLSBBean:sell", userId, holdingId);
      }

      // TODO Call Holding Service to get Holding
      HoldingDataBean holding = holdingClient.getHolding(holdingId);

      if (holding == null) {
        Log.error("TradeSLSBBean:sell User " + userId + " attempted to sell holding " + holdingId
            + " which has already been sold");

        OrderDataBean orderData = new OrderDataBean();
        orderData.setOrderStatus("cancelled");
        entityManager.persist(orderData);

        return orderData;
      }

      String quoteSymbol = holding.getQuoteSymbol();
      BigDecimal price = quoteClient.getQuotePrice(quoteSymbol);

      double quantity = holding.getQuantity();
      order = createOrder(userId, quoteSymbol, holdingId, price, "sell", quantity);

      // UPDATE the holding purchase data to signify this holding is
      // "inflight" to be sold
      // -- could add a new holdingStatus attribute to holdingEJB
      // ToDo: holding.setPurchaseDate(new java.sql.Timestamp(0));

      BigDecimal orderFee = order.getOrderFee();
      BigDecimal balanceUpdate = (new BigDecimal(quantity).multiply(price)).subtract(orderFee);
      accountClient.updateAccountBalance(userId, balanceUpdate);

      holdingClient.removeHolding(holdingId);
      // update account

      order.setOrderStatus("closed");
      order.setCompletionDate(new java.sql.Timestamp(System.currentTimeMillis()));

    } catch (Exception e) {
      Log.error("TradeSLSBBean:sell(" + userId + "," + holdingId + ") --> failed", e);
      // if (order != null) order.cancel();
      // UPDATE - handle all exceptions like:
      throw new EJBException("TradeSLSBBean:sell(" + userId + "," + holdingId + ")", e);
    }
    return order;
  }

  @Override
  public void cancelOrder(Integer orderID) {
    if (Log.doTrace()) {
      Log.trace("TradeSLSBBean:cancelOrder", orderID);
    }

    OrderDataBean order = entityManager.find(OrderDataBean.class, orderID);
    order.cancel();
  }

  @Override
  public List<OrderDataBean> getOrders(String userId) {
    if (Log.doTrace()) {
      Log.trace("TradeSLSBBean:getOrders", userId);
    }

    TypedQuery<OrderDataBean> q = entityManager.createNamedQuery("orderejb.findByAccountId", OrderDataBean.class);
    return q.setParameter("accountId", userId).getResultList();
  }

  @Override
  public List<OrderDataBean> getClosedOrders(String userID) {
    if (Log.doTrace()) {
      Log.trace("TradeSLSBBean:getClosedOrders", userID);
    }

    try {
      /*
       * I want to do a CriteriaUpdate here, but there are issues with JBoss/Hibernate
       */
      CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
      CriteriaQuery<OrderDataBean> criteriaQuery = criteriaBuilder.createQuery(OrderDataBean.class);
      Root<OrderDataBean> orders = criteriaQuery.from(OrderDataBean.class);
      criteriaQuery.select(orders);
      criteriaQuery.where(
          criteriaBuilder.equal(orders.get("orderStatus"), criteriaBuilder.parameter(String.class, "p_status")),
          criteriaBuilder.equal(orders.get("accountId"),
              criteriaBuilder.parameter(String.class, "p_accountId")));

      TypedQuery<OrderDataBean> q = entityManager.createQuery(criteriaQuery);
      q.setParameter("p_status", "closed");
      q.setParameter("p_accountId", userID);
      List<OrderDataBean> results = q.getResultList();

      Iterator<OrderDataBean> itr = results.iterator();

      // Spin through the orders to remove or mark completed
      while (itr.hasNext()) {
        OrderDataBean order = itr.next();

        // Added this for Longruns (to prevent orderejb growth)
        entityManager.remove(order);

      }

      return results;

    } catch (

    Exception e) {
      Log.error("TradeSLSBBean.getClosedOrders", e);
      throw new EJBException("TradeSLSBBean.getClosedOrders - error", e);
    }
  }

  private OrderDataBean createOrder(String accountId, String quoteSymbol, Integer holdingId, BigDecimal price,
      String orderType, double quantity) {

    OrderDataBean order;

    if (Log.doTrace()) {
      Log.trace(
          "TradeSLSBBean:createOrder(orderID=" + " account=" + ((accountId == null) ? null : accountId) + " quote="
              + ((quoteSymbol == null) ? null : quoteSymbol) + " orderType=" + orderType + " quantity=" + quantity);
    }
    try {
      order = new OrderDataBean(orderType, "open", new Timestamp(System.currentTimeMillis()), null, quantity,
          price.setScale(SCALE, ROUND), ORDER_FEE, accountId, quoteSymbol, holdingId);
      entityManager.persist(order);
    } catch (Exception e) {
      Log.error("TradeSLSBBean:createOrder -- failed to create Order. The stock/quote may not exist in the database.",
          e);
      throw new EJBException(
          "TradeSLSBBean:createOrder -- failed to create Order. Check that the symbol exists in the database.", e);
    }
    return order;
  }

  @Override
  public OrderDataBean buy(String userId, String symbol, double quantity) {
    OrderDataBean order;

    try {
      if (Log.doTrace()) {
        Log.trace("TradeSLSBBean:buy", userId, symbol, quantity);
      }

      BigDecimal price = quoteClient.getQuotePrice(symbol);
      order = createOrder(userId, symbol, null, price, "buy", quantity);

      BigDecimal orderFee = order.getOrderFee();
      BigDecimal balanceUpdate = (new BigDecimal(quantity).multiply(price)).subtract(orderFee);
      accountClient.updateAccountBalance(userId, balanceUpdate);

      HoldingDataBean newHolding = holdingClient.createHolding(userId, symbol, quantity, price);
      order.setHoldingId(newHolding.getHoldingID());
      order.setOrderStatus("closed");

      order.setCompletionDate(new java.sql.Timestamp(System.currentTimeMillis()));

    } catch (Exception e) {
      Log.error("TradeSLSBBean:buy(" + userId + "," + symbol + "," + quantity + ") --> failed", e);
      /* On exception - cancel the order */
      // TODO figure out how to do this with JPA
      // if (order != null) order.cancel();
      throw new EJBException(e);
    }
    return order;
  }
}