/**
 * (C) Copyright IBM Corporation 2021.
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

import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.interfaces.OrderService;
import com.ibm.websphere.samples.daytrader.util.Log;

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

  private static final String BUY = "buy";
  private static final String SELL = "sell";
  private static final int ROUND = BigDecimal.ROUND_HALF_UP;
  private static final int SCALE = 2;
  private static final BigDecimal ORDER_FEE = new BigDecimal("24.95");

  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  private Log logService;

  @Override
  public OrderDataBean buy(String userId, String symbol, double quantity, BigDecimal price) {
    if (logService.doTrace()) {
      logService.trace("OrderServiceEJBImpl:buy", userId, symbol, quantity, price);
    }
    return createOrder(userId, symbol, null, price, BUY, quantity);
  }

  @Override
  public OrderDataBean sell(String userId, String symbol, Integer holdingId, BigDecimal price, double quantity) {

    try {
      if (logService.doTrace()) {
        logService.trace("OrderServiceEJBImpl:sell", userId, holdingId);
      }

      return createOrder(userId, symbol, holdingId, price, SELL, quantity);

    } catch (Exception e) {
      logService.error("OrderServiceEJBImpl:sell(" + userId + "," + holdingId + ") --> failed", e);
      // if (order != null) order.cancel();
      // UPDATE - handle all exceptions like:
      throw new EJBException("OrderServiceEJBImpl:sell(" + userId + "," + holdingId + ")", e);
    }
  }

  @Override
  public OrderDataBean completeOrder(Integer orderId) {

    OrderDataBean order;

    try {
      order = entityManager.find(OrderDataBean.class, orderId);
      order.setOrderStatus("closed");
      order.setCompletionDate(new java.sql.Timestamp(System.currentTimeMillis()));

    } catch (Exception e) {
      logService.error("OrderServiceEJBImpl:completeOrder(" + orderId + ") --> failed", e);
      // if (order != null) order.cancel();
      // UPDATE - handle all exceptions like:
      throw new EJBException("OrderServiceEJBImpl:completeOrder(" + orderId + ")", e);
    }
  
    return order;
  }

  @Override
  public void cancelOrder(Integer orderID) {
    if (logService.doTrace()) {
      logService.trace("OrderServiceEJBImpl:cancelOrder", orderID);
    }

    OrderDataBean order = entityManager.find(OrderDataBean.class, orderID);
    order.cancel();
  }

  @Override
  public List<OrderDataBean> getOrders(String userId) {
    if (logService.doTrace()) {
      logService.trace("OrderServiceEJBImpl:getOrders", userId);
    }

    TypedQuery<OrderDataBean> q = entityManager.createNamedQuery("orderejb.findByAccountId", OrderDataBean.class);
    return q.setParameter("accountId", userId).getResultList();
  }

  @Override
  public List<OrderDataBean> getClosedOrders(String userID) {
    if (logService.doTrace()) {
      logService.trace("OrderServiceEJBImpl:getClosedOrders", userID);
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
          criteriaBuilder.equal(orders.get("accountId"), criteriaBuilder.parameter(String.class, "p_accountId")));

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
      logService.error("OrderServiceEJBImpl.getClosedOrders", e);
      throw new EJBException("OrderServiceEJBImpl.getClosedOrders - error", e);
    }
  }

  private OrderDataBean createOrder(String accountId, String quoteSymbol, Integer holdingId, BigDecimal price,
      String orderType, double quantity) {

    OrderDataBean order;

    if (logService.doTrace()) {
      logService.trace(
          "OrderServiceEJBImpl:createOrder(orderID=" + " account=" + ((accountId == null) ? null : accountId) + " quote="
              + ((quoteSymbol == null) ? null : quoteSymbol) + " orderType=" + orderType + " quantity=" + quantity);
    }
    try {
      order = new OrderDataBean(orderType, "open", new Timestamp(System.currentTimeMillis()), null, quantity,
          price.setScale(SCALE, ROUND), ORDER_FEE, accountId, quoteSymbol, holdingId);
      entityManager.persist(order);
    } catch (Exception e) {
      logService.error(
          "OrderServiceEJBImpl:createOrder -- failed to create Order. The stock/quote may not exist in the database.", e);
      throw new EJBException(
          "OrderServiceEJBImpl:createOrder -- failed to create Order. Check that the symbol exists in the database.", e);
    }
    return order;
  }

  @Override
  public OrderDataBean getOrder(Integer orderId) {
    return entityManager.find(OrderDataBean.class, orderId);
  }
}