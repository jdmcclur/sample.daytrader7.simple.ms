/**
 * (C) Copyright IBM Corporation 2015,2021.
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

package com.ibm.websphere.samples.daytrader;

import com.ibm.websphere.samples.daytrader.beans.MarketSummaryDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountProfileDataBean;
import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;
import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.interfaces.TradeService;
import com.ibm.websphere.samples.daytrader.restclient.AccountClient;
import com.ibm.websphere.samples.daytrader.restclient.HoldingClient;
import com.ibm.websphere.samples.daytrader.restclient.OrderClient;
import com.ibm.websphere.samples.daytrader.restclient.QuoteClient;
import com.ibm.websphere.samples.daytrader.util.FinancialUtils;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;



/**
 * The TradeAction class provides the generic client side access to each of the
 * Trade brokerage user operations. These include login, logout, buy, sell,
 * getQuote, etc. The TradeAction class does not handle user interface
 * processing and should be used by a class that is UI specific. For example,
 * {trade_client.TradeServletAction}manages a web interface to Trade, making
 * calls to TradeAction methods to actually performance each operation.
 */
@ApplicationScoped
public class TradeAction implements TradeService {

  private final Integer marketSummaryLock = new Integer(0);
  private long nextMarketSummary = System.currentTimeMillis();
  private MarketSummaryDataBean cachedMSDB = new MarketSummaryDataBean();
  private static final int MARKET_SUMMARY_INTERVAL = 20;

  @Inject
  Log logService;

  @Inject
  TradeConfig configService;

  @Inject
  @RestClient
  HoldingClient holdingClient;

  @Inject
  @RestClient
  AccountClient accountClient;

  @Inject
  @RestClient
  QuoteClient quoteClient;

  @Inject
  @RestClient
  OrderClient orderClient;

  /**
   * Market Summary is inherently a heavy database operation. For servers that
   * have a caching story this is a great place to cache data that is good for a
   * period of time. In order to provide a flexible framework for this we allow
   * the market summary operation to be invoked on every transaction, time delayed
   * or never. This is configurable in the configuration panel.
   *
   * @return An instance of the market summary
   */
  @Override
  public MarketSummaryDataBean getMarketSummary() throws Exception {

    if (logService.doActionTrace()) {
      logService.trace("TradeAction:getMarketSummary()");
    }
    
    /**
     * This is a little funky. If its time to fetch a new Market summary then we'll
     * synchronize access to make sure only one requester does it. Others will
     * merely return the old copy until the new MarketSummary has been executed.
     */

    long currentTime = System.currentTimeMillis();

    if (currentTime > nextMarketSummary) {
      long oldNextMarketSummary = nextMarketSummary;
      boolean fetch = false;

      synchronized (marketSummaryLock) {
        /**
         * Is it still ahead or did we miss lose the race? If we lost then let's get out
         * of here as the work has already been done.
         */
        if (oldNextMarketSummary == nextMarketSummary) {
          fetch = true;
          nextMarketSummary += MARKET_SUMMARY_INTERVAL * 1000;

          /**
           * If the server has been idle for a while then its possible that
           * nextMarketSummary could be way off. Rather than try and play catch up we'll
           * simply get in sync with the current time + the interval.
           */
          if (nextMarketSummary < currentTime) {
            nextMarketSummary = currentTime + MARKET_SUMMARY_INTERVAL * 1000;
          }
        }
      }

      // If we're the lucky one then let's update the MarketSummary
      if (fetch) {
        cachedMSDB = quoteClient.getMarketSummary();
      }
    }

    return cachedMSDB;
  }

  /**
   * Purchase a stock and create a new holding for the given user. Given a stock
   * symbol and quantity to purchase, retrieve the current quote price, debit the
   * user's account balance, and add holdings to user's portfolio.
   *
   * @param userID   the customer requesting the stock purchase
   * @param symbol   the symbol of the stock being purchased
   * @param quantity the quantity of shares to purchase
   * @return OrderDataBean providing the status of the newly created buy order
   */
  @Override
  public OrderDataBean buy(String userID, String symbol, double quantity) throws Exception {
    if (logService.doActionTrace()) {
      logService.trace("TradeAction:buy", userID, symbol, new Double(quantity));
    }
    return orderClient.buy(userID, symbol, quantity);
  }

  /**
   * Sell a stock holding and removed the holding for the given user. Given a
   * Holding, retrieve current quote, credit user's account, and reduce holdings
   * in user's portfolio.
   *
   * @param userID    the customer requesting the sell
   * @param holdingID the users holding to be sold
   * @return OrderDataBean providing the status of the newly created sell order
   */
  @Override
  public OrderDataBean sell(String userID, Integer holdingID) throws Exception {
    if (logService.doActionTrace()) {
      logService.trace("TradeAction:sell", userID, holdingID);
    }
    return orderClient.sell(userID, holdingID);
  }

  /**
   * Get the collection of all orders for a given account.
   *
   * @param userID the customer account to retrieve orders for
   * @return Collection OrderDataBeans providing detailed order information
   */
  @Override
  public Collection<?> getOrders(String userID) throws Exception {
    if (logService.doActionTrace()) {
      logService.trace("TradeAction:getOrders", userID);
    }
    Collection<?> orderDataBeans = orderClient.getOrders(userID);

    return orderDataBeans;
  }

  /**
   * Get the collection of completed orders for a given account that need to be
   * alerted to the user.
   *
   * @param userID the customer account to retrieve orders for
   * @return Collection OrderDataBeans providing detailed order information
   */
  @Override
  public Collection<?> getClosedOrders(String userID) throws Exception {
    if (logService.doActionTrace()) {
      logService.trace("TradeAction:getClosedOrders", userID);
    }

    Collection<?> orderDataBeans = orderClient.getClosedOrders(userID);

    return orderDataBeans;
  }

  /**
   * Given a market symbol, price, and details, create and return a new
   * {@link QuoteDataBean}.
   *
   * @param symbol the symbol of the stock
   * @param price  the current stock price
   * @return a new QuoteDataBean or null if Quote could not be created
   */
  @Override
  public QuoteDataBean createQuote(String symbol, String companyName) throws Exception {
    if (logService.doActionTrace()) {
      logService.trace("TradeAction:createQuote", symbol, companyName);
    }

    return quoteClient.createQuote(symbol, companyName);
  }

  /**
   * Return a collection of {@link QuoteDataBean}describing all current quotes.
   *
   * @return the collection of QuoteDataBean
   */
  @Override
  public Collection<?> getAllQuotes() throws Exception {
    if (logService.doActionTrace()) {
      logService.trace("TradeAction:getAllQuotes");
    }

    return quoteClient.getAllQuotes();

  }

  /**
   * Return a {@link QuoteDataBean}describing a current quote for the given stock
   * symbol.
   *
   * @param symbol the stock symbol to retrieve the current Quote
   * @return the QuoteDataBean
   */
  @Override
  public QuoteDataBean getQuote(String symbol) throws Exception {
    if (logService.doActionTrace()) {
      logService.trace("TradeAction:getQuote", symbol);
    }
    if ((symbol == null) || (symbol.length() == 0) || (symbol.length() > 10)) {
      if (logService.doActionTrace()) {
        logService.trace("TradeAction:getQuote   ---  primitive workload");
      }
      return new QuoteDataBean("Invalid symbol", "", 0.0, FinancialUtils.ZERO, FinancialUtils.ZERO, FinancialUtils.ZERO,
          FinancialUtils.ZERO, 0.0);
    }

    return quoteClient.getQuote(symbol);
  }

  @Override
  public List<QuoteDataBean> getQuotes(String symbols) {
    return quoteClient.getQuotes(symbols);
  }
  
  /**
   * Return the portfolio of stock holdings for the specified customer as a
   * collection of HoldingDataBeans.
   *
   * @param userID the customer requesting the portfolio
   * @return Collection of the users portfolio of stock holdings
   */
  @Override
  public Collection<?> getHoldings(String userID) throws Exception {
    if (logService.doActionTrace()) {
      logService.trace("TradeAction:getHoldings", userID);
    }

    Collection<?> holdingDataBeans = holdingClient.getHoldings(userID);

    return holdingDataBeans;
  }

  /**
   * Return a specific user stock holding identifed by the holdingID.
   *
   * @param holdingID the holdingID to return
   * @return a HoldingDataBean describing the holding
   */
  @Override
  public HoldingDataBean getHolding(Integer holdingID) throws Exception {
    if (logService.doActionTrace()) {
      logService.trace("TradeAction:getHolding", holdingID);
    }

    return holdingClient.getHolding(holdingID);
  }

  /**
   * Return an AccountDataBean object for userID describing the account.
   *
   * @param userID the account userID to lookup
   * @return User account data in AccountDataBean
   */
  @Override
  public AccountDataBean getAccountData(String userID) throws Exception {
    if (logService.doActionTrace()) {
      logService.trace("TradeAction:getAccountData", userID);
    }
    AccountDataBean accountData = accountClient.getAccountData(userID);

    return accountData;
  }

  /**
   * Return an AccountProfileDataBean for userID providing the users profile.
   *
   * @param userID the account userID to lookup
   */
  @Override
  public AccountProfileDataBean getAccountProfileData(String userID) throws Exception {
    if (logService.doActionTrace()) {
      logService.trace("TradeAction:getAccountProfileData", userID);
    }
    AccountProfileDataBean accountProfileData = accountClient.getAccountProfileData(userID);

    return accountProfileData;
  }

  /**
   * Update userID's account profile information using the provided
   * AccountProfileDataBean object.
   *
   * @param accountProfileData account profile data in AccountProfileDataBean
   */
  @Override
  public AccountProfileDataBean updateAccountProfile(AccountProfileDataBean accountProfileData) throws Exception {
    if (logService.doActionTrace()) {
      logService.trace("TradeAction:updateAccountProfile", accountProfileData);
    }

    accountProfileData = accountClient.updateAccountProfile(accountProfileData);
    return accountProfileData;
  }

  /**
   * Attempt to authenticate and login a user with the given password.
   *
   * @param userID   the customer to login
   * @param password the password entered by the customer for authentication
   * @return User account data in AccountDataBean
   */
  @Override
  public AccountDataBean login(String userID, String password) throws Exception {
    if (logService.doActionTrace()) {
      logService.trace("TradeAction:login", userID, password);
    }
    return accountClient.login(userID, password);
  }

  /**
   * Logout the given user.
   *
   * @param userID the customer to logout
   */
  @Override
  public void logout(String userID) throws Exception {
    if (logService.doActionTrace()) {
      logService.trace("TradeAction:logout", userID);
    }

    accountClient.logout(userID);

  }

  /**
   * Register a new Trade customer. Create a new user profile, user registry
   * entry, account with initial balance, and empty portfolio.
   *
   * @param userID      the new customer to register
   * @param password    the customers password
   * @param fullname    the customers fullname
   * @param address     the customers street address
   * @param email       the customers email address
   * @param creditCard  the customers creditcard number
   * @param openBalance the amount to charge to the customers credit to open the
   *                    account and set the initial balance
   * @return the userID if successful, null otherwise
   */
  @Override
  public AccountDataBean register(String userID, String password, String fullname, String address, String email,
      String creditCard, BigDecimal openBalance) throws Exception {
    if (logService.doActionTrace()) {
      logService.trace("TradeAction:register", userID, password, fullname, address, email, creditCard, openBalance);
    }

    return accountClient.register(userID, password, fullname, address, email, creditCard, openBalance);
  }

  public AccountDataBean register(String userID, String password, String fullname, String address, String email,
      String creditCard, String openBalanceString) throws Exception {
    BigDecimal openBalance = new BigDecimal(openBalanceString);
    return register(userID, password, fullname, address, email, creditCard, openBalance);
  }

}