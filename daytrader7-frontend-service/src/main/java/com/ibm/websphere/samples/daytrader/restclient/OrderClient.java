package com.ibm.websphere.samples.daytrader.restclient;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "orderClient")
@Path("/")
public interface OrderClient {

  @POST
  @Path("/buy")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  OrderDataBean buy(
    @FormParam("userId") String userId, 
    @FormParam("symbol") String symbol, 
    @FormParam("quantity") double quantity);

  @POST
  @Path("/sell")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  OrderDataBean sell(@FormParam("userId") String userId, @FormParam("holdingId") Integer holdingId);

  @GET
  @Path("/cancelOrder/{orderId}")
  @Produces(MediaType.APPLICATION_JSON)
  public void cancelOrder(@PathParam("orderId") Integer orderId);

  @GET
  @Path("/getOrders/{userId}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<OrderDataBean> getOrders(@PathParam("userId") String userId);

  @GET
  @Path("/getClosedOrders/{userId}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<OrderDataBean> getClosedOrders(@PathParam("userId") String userId);

  @GET
  @Path("/db/createDB")
  public Response createDB();
}
