/**
 * (C) Copyright IBM Corporation 2021
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

import com.ibm.websphere.samples.daytrader.beans.MarketSummaryDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.interfaces.QuoteService;

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
import javax.ws.rs.core.Response;

@Path("/")
@ApplicationScoped
public class QuoteRest {

  @Inject
  QuoteService quoteService;

  @Inject
  RecentQuotePriceChangeList changeList;

  @Path("/createQuote")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public QuoteDataBean createQuote(
      @FormParam("symbol") String symbol, 
      @FormParam("companyName") String companyName) throws Exception {
    return quoteService.createQuote(symbol, companyName);
  }

  @Path("/getQuote/{symbol}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public QuoteDataBean getQuote(@PathParam("symbol") String symbol) throws Exception {
    return quoteService.getQuote(symbol);
  }

  @Path("/getQuotePrice/{symbol}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public BigDecimal getQuotePrice(@PathParam("symbol") String symbol) throws Exception {
    return quoteService.getQuote(symbol).getPrice();
  }

  @POST
  @Path("/getQuotes")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public List<QuoteDataBean> getQuotes(@FormParam("symbols") String symbols) {
    return quoteService.getQuotes(symbols);
  }

  @GET
  @Path("/getAllQuotes")
  @Produces(MediaType.APPLICATION_JSON)
  public List<QuoteDataBean> getAllQuotes() throws Exception {
    return quoteService.getAllQuotes();
  }

  @Path("/updateQuotePriceVolume")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public BigDecimal updateQuotePriceVolume(
      @FormParam("symbol") String symbol, 
      @FormParam("sharesTraded") double sharesTraded,
      @FormParam("orderType") String orderType) throws Exception {

    BigDecimal price  = quoteService.updateQuotePriceVolume(symbol, sharesTraded, orderType);

    QuoteDataBean quote = quoteService.getQuote(symbol);
    changeList.add(quote);

    return price;
  }

  @GET
  @Path("/getMarketSummary")
  @Produces(MediaType.APPLICATION_JSON)
  public MarketSummaryDataBean getMarketSummary() {
    return quoteService.getMarketSummary();
  }
  
  @GET
  @Path("/status")
  public Response status() {
    return Response.ok("OK").build();
  }
}
