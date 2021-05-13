package com.ibm.websphere.samples.daytrader.rest;

import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;
import com.ibm.websphere.samples.daytrader.interfaces.HoldingService;

@Path("/")
@ApplicationScoped
public class HoldingRest {
  
  @Inject
  HoldingService holdingService;

  @GET
  @Path("/getHoldings/{userId}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<HoldingDataBean> getHoldings(@PathParam("userId") String userId) throws Exception {
    return holdingService.getHoldings(userId);
  }

  @GET
  @Path("/getHolding/{holdingId}")
  @Produces(MediaType.APPLICATION_JSON)
  public HoldingDataBean getHolding(@PathParam("holdingId") Integer holdingId) throws Exception {
    return holdingService.getHolding(holdingId);
  }

  @GET
  @Path("/getHoldingQuoteSymbol/{holdingId}")
  @Produces(MediaType.APPLICATION_JSON)
  public String getHoldingQuoteSymbol(@PathParam("holdingId")  Integer holdingId) throws Exception {
    return holdingService.getHolding(holdingId).getQuoteSymbol();
  }

  @POST
  @Path("/createHolding")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public HoldingDataBean createHolding(
    @FormParam("accountId") String accountId, 
    @FormParam("quoteSymbol") String quoteSymbol, 
    @FormParam("quantity") double quantity, 
    @FormParam("purchasePrice") BigDecimal purchasePrice)
      throws Exception {
    return holdingService.createHolding(accountId, quoteSymbol, quantity, purchasePrice);
  }

  @GET
  @Path("/removeHolding/{holdingId}")
  public void removeHolding(@PathParam("holdingId") Integer holdingId) throws Exception {
    holdingService.removeHolding(holdingId);
  }

}
