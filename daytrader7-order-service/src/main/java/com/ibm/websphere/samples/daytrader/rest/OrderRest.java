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

package com.ibm.websphere.samples.daytrader.rest;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.interfaces.OrderService;


@Path("/")
@ApplicationScoped
public class OrderRest {

  @Inject
  OrderService orderService;

  @POST
  @Path("/buy")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public OrderDataBean buy(
    @FormParam("userId") String userId, 
    @FormParam("symbol") String symbol, 
    @FormParam("quantity") double quantity) throws Exception {
    return orderService.buy(userId, symbol, quantity);
  }
  
  @POST
  @Path("/sell")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public OrderDataBean sell(
    @FormParam("userId")String userId, 
    @FormParam("holdingId") Integer holdingId) throws Exception {
    return orderService.sell(userId, holdingId);
  }
  
  @GET
  @Path("/cancelOrder/{orderId}")
  @Produces(MediaType.APPLICATION_JSON)
  public void cancelOrder(@PathParam("orderId") Integer orderId) throws Exception {
    orderService.cancelOrder(orderId);
  }
  
  @GET
  @Path("/getOrders/{userId}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<OrderDataBean> getOrders(@PathParam("userId") String userId) throws Exception {
    return orderService.getOrders(userId);
  }

  @GET
  @Path("/getClosedOrders/{userId}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<OrderDataBean> getClosedOrders(@PathParam("userId") String userId) throws Exception {
    return orderService.getClosedOrders(userId);
  }

  @GET
  @Path("/status")
  public Response status() {
    return Response.ok("OK").build();
  }
}
