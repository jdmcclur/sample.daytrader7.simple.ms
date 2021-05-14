/**
 * (C) Copyright IBM Corporation 2015.
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

package com.ibm.websphere.samples.daytrader.interfaces;

import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.beans.MarketSummaryDataBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * TradeServices interface specifies the business methods provided by the Trade
 * online broker application. These business methods represent the features and
 * operations that can be performed by customers of the brokerage such as login,
 * logout, get a stock quote, buy or sell a stock, etc. This interface is
 * implemented by {@link Trade} providing an EJB implementation of these
 * business methods and also by {@link TradeDirect} providing a JDBC
 * implementation.
 *
 * @see Trade
 * @see TradeDirect
 *
 */

public interface QuoteService {
  
  /**
   * Given a market symbol, price, and details, create and return a new
   * {@link QuoteDataBean}.
   *
   * @param symbol the symbol of the stock
   * @param price  the current stock price
   * @return a new QuoteDataBean or null if Quote could not be created
   */
  QuoteDataBean createQuote(String symbol, String companyName) throws Exception;

  /**
   * Return a {@link QuoteDataBean} describing a current quote for the given stock
   * symbol.
   *
   * @param symbol the stock symbol to retrieve the current Quote
   * @return the QuoteDataBean
   */
  QuoteDataBean getQuote(String symbol) throws Exception;

  List<QuoteDataBean> getQuotes(String symbols);

  /**
   * Return a {@link java.util.Collection} of {@link QuoteDataBean} describing all
   * current quotes.
   *
   * @return A collection of QuoteDataBean
   */
  List<QuoteDataBean> getAllQuotes() throws Exception;

  /**
   * Update the stock quote price and volume for the specified stock symbol.
   *
   * @param symbol   for stock quote to update
   * @param newPrice the updated quote price
   * @return the new price
   */
  BigDecimal updateQuotePriceVolume(String symbol, double sharesTraded, String orderType) throws Exception;

  MarketSummaryDataBean getMarketSummary(); 
}
