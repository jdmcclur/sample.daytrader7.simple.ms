package com.ibm.websphere.samples.daytrader.restclient;

import javax.ws.rs.Produces;

import java.math.BigDecimal;

import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey="quoteClient")
@Path("/")
public interface QuoteClient {  
  
  @Path("/getQuotePrice/{symbol}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public BigDecimal getQuotePrice(@PathParam("symbol") String symbol);
}

