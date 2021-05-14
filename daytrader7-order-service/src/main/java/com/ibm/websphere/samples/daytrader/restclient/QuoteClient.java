package com.ibm.websphere.samples.daytrader.restclient;

import javax.ws.rs.Produces;

import java.math.BigDecimal;

import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "quoteClient")
@Path("/")
public interface QuoteClient {

  @Path("/getQuotePrice/{symbol}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public BigDecimal getQuotePrice(@PathParam("symbol") String symbol);

  @POST
  @Path("/updateQuotePriceVolume")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public BigDecimal updateQuotePriceVolume(@FormParam("symbol") String symbol,
      @FormParam("sharesTraded") double sharesTraded, @FormParam("orderType") String orderType);
}
