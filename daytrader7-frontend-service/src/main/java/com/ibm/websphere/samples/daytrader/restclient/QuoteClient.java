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

import com.ibm.websphere.samples.daytrader.beans.MarketSummaryDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "quoteClient")
@Path("/")
public interface QuoteClient {

  @Path("/getQuotePrice/{symbol}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public BigDecimal getQuotePrice(@PathParam("symbol") String symbol);

  @GET
  @Path("/getAllQuotes")
  @Produces(MediaType.APPLICATION_JSON)
  public List<QuoteDataBean> getAllQuotes();

  @Path("/createQuote")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public QuoteDataBean createQuote(@FormParam("symbol") String symbol, @FormParam("companyName") String companyName);

  @Path("/getQuote/{symbol}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public QuoteDataBean getQuote(@PathParam("symbol") String symbol);

  @Path("/updateQuotePriceVolume")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public QuoteDataBean updateQuotePriceVolume(
      @FormParam("symbol") String symbol, 
      @FormParam("sharesTraded") double sharesTraded);

  @GET
  @Path("/db/createDB")
  public Response createDB();

  @GET
  @Path("/getMarketSummary")
  @Produces(MediaType.APPLICATION_JSON)
  public MarketSummaryDataBean getMarketSummary();
}
