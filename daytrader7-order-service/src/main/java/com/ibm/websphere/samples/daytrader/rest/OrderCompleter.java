package com.ibm.websphere.samples.daytrader.rest;

import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.interfaces.OrderService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.Asynchronous;

@ApplicationScoped
public class OrderCompleter {

  @Inject
  OrderService orderService;

  public OrderDataBean completeOrderSync(Integer orderId) throws Exception {
    return orderService.completeOrder(orderId);
  }

  @Asynchronous
  public CompletionStage<OrderDataBean> completeOrderAsync(Integer orderId) throws Exception {
    OrderDataBean completedOrder = orderService.completeOrder(orderId);
    return CompletableFuture.completedFuture(completedOrder);
  }

}