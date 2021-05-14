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

  @GET
  @Path("/getQuote/{symbol}")
  @Produces(MediaType.APPLICATION_JSON)
  public QuoteDataBean getQuote(@PathParam("symbol") String symbol);

  @Path("/getQuotePrice/{symbol}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public BigDecimal getQuotePrice(@PathParam("symbol") String symbol);

  @POST
  @Path("/getQuotes")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public List<QuoteDataBean> getQuotes(@FormParam("symbols") String symbols);

  @GET
  @Path("/getAllQuotes")
  @Produces(MediaType.APPLICATION_JSON)
  public List<QuoteDataBean> getAllQuotes();

  @GET
  @Path("/getMarketSummary")
  @Produces(MediaType.APPLICATION_JSON)
  public MarketSummaryDataBean getMarketSummary();

  @POST
  @Path("/createQuote")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public QuoteDataBean createQuote(@FormParam("symbol") String symbol, @FormParam("companyName") String companyName);

  @POST
  @Path("/updateQuotePriceVolume")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public BigDecimal updateQuotePriceVolume(@FormParam("symbol") String symbol,
      @FormParam("sharesTraded") double sharesTraded,
      @FormParam("orderType") String orderType);

  // DB ------

  @GET
  @Path("/db/createDB")
  public Response createDB();

  @GET
  @Path("/db/resetDB/{deleteAll}")
  public Response resetDB(@PathParam("deleteAll") Boolean deleteAll);
}
