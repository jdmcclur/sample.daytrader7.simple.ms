package com.ibm.websphere.samples.daytrader.restclient;

import javax.ws.rs.Produces;

import java.math.BigDecimal;
import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey="holdingClient")
@Path("/")
public interface HoldingClient {  
  
  @GET
  @Path("/getHoldings/{userId}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<HoldingDataBean> getHoldings(@PathParam("userId") String userId);

  @GET
  @Path("/getHolding/{holdingId}")
  @Produces(MediaType.APPLICATION_JSON)
  public HoldingDataBean getHolding(@PathParam("holdingId") Integer holdingId); 

  @POST
  @Path("/createHolding")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public HoldingDataBean createHolding(
    @FormParam("accountId") String accountId, 
    @FormParam("quoteSymbol") String quoteSymbol, 
    @FormParam("quantity") double quantity, 
    @FormParam("purchasePrice") BigDecimal purchasePrice);

  @GET
  @Path("/removeHolding/{holdingId}")
  public void removeHolding(@PathParam("holdingId") Integer holdingId);

    // DB ------

    @GET
    @Path("/db/createDB")
    public Response createDB();
  
    @GET
    @Path("/db/resetDB/{deleteAll}")
    public Response resetDB(@PathParam("deleteAll") Boolean deleteAll);

}
