package com.ibm.websphere.samples.daytrader.rest;

import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;
import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.interfaces.OrderService;
import com.ibm.websphere.samples.daytrader.restclient.AccountClient;
import com.ibm.websphere.samples.daytrader.restclient.HoldingClient;
import com.ibm.websphere.samples.daytrader.restclient.QuoteClient;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.Asynchronous;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/* Utils for buy/sell operations */

@ApplicationScoped
public class OrderUtils {

  @Inject
  OrderService orderService;

  @Inject
  @RestClient
  HoldingClient holdingClient;

  @Inject
  @RestClient
  AccountClient accountClient;

  @Inject
  @RestClient
  QuoteClient quoteClient;

  public OrderDataBean doBuy(String userId, String symbol, double quantity) throws Exception {
    BigDecimal price = quoteClient.getQuotePrice(symbol);
    return orderService.buy(userId, symbol, quantity, price);
  }

  public OrderDataBean doSell(String userId, Integer holdingId) throws Exception {
    HoldingDataBean holding = holdingClient.getHolding(holdingId);
    BigDecimal price = quoteClient.getQuotePrice(holding.getQuoteSymbol());
    return orderService.sell(userId, holding.getQuoteSymbol(), holdingId, price, holding.getQuantity());
  }

  public OrderDataBean doCompleteOrder(Integer orderId) throws Exception {
    OrderDataBean order = orderService.getOrder(orderId);
    BigDecimal price = order.getPrice();
    double quantity = order.getQuantity();

    BigDecimal orderFee = order.getOrderFee();
    BigDecimal balanceUpdate = (new BigDecimal(quantity).multiply(price)).subtract(orderFee);

    accountClient.updateAccountBalance(order.getAccountId(), balanceUpdate);
    quoteClient.updateQuotePriceVolume(order.getQuoteSymbol(), quantity, order.getOrderType());
    if (order.getOrderType().equals("sell")) {
      holdingClient.removeHolding(order.getHoldingId());
    } else {
      holdingClient.createHolding(order.getAccountId(), order.getQuoteSymbol(), quantity, order.getPrice());
    }
    return orderService.completeOrder(orderId);
  }

  @Asynchronous
  public CompletionStage<OrderDataBean> doCompleteOrderAsync(Integer orderId) throws Exception {
    OrderDataBean completedOrder = doCompleteOrder(orderId);
    return CompletableFuture.completedFuture(completedOrder);
  }

}