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
package com.ibm.websphere.samples.daytrader.web;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.websphere.samples.daytrader.interfaces.TradeService;
import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountProfileDataBean;
import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;
import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;

/**
 * TradeServletradeService provides servlet specific client side access to each
 * of the Trade brokerage user operations. These include login, logout, buy,
 * sell, getQuote, etc. TradeServletradeService manages a web interface to Trade
 * handling HttpRequests/HttpResponse objects and forwarding results to the
 * appropriate JSP page for the web interface. TradeServletradeService invokes
 * {@link TradeAction} methods to actually perform each trading operation.
 *
 */
@SessionScoped
public class TradeServletAction implements Serializable {

  private static final long serialVersionUID = 7732313125198761455L;

  /* Trade Scenario Workload parameters */
  private final String WELCOME_PAGE = "/welcome.jsp";
  private final String REGISTER_PAGE = "/register.jsp";
  private final String PORTFOLIO_PAGE = "/portfolio.jsp";
  private final String QUOTE_PAGE = "/quote.jsp";
  private final String HOME_PAGE = "/tradehome.jsp";
  private final String ACCOUNT_PAGE = "/account.jsp";
  private final String ORDER_PAGE = "/order.jsp";
  private final String MARKET_SUMMARY_PAGE = "/marketSummary.jsp";

  @Inject
  private TradeService tradeService;

  @Inject
  Log Log;

  @Inject
  TradeConfig TradeConfig;

  /**
   * Display User Profile information such as address, email, etc. for the given
   * Trader Dispatch to the Trade Account JSP for display
   *
   * @param userID  The User to display profile info
   * @param ctx     the servlet context
   * @param req     the HttpRequest object
   * @param resp    the HttpResponse object
   * @param results A short description of the results/success of this web request
   *                provided on the web page
   * @exception javax.servlet.ServletException If a servlet specific exception is
   *                                           encountered
   * @exception javax.io.IOException           If an exception occurs while
   *                                           writing results back to the user
   *
   */
  void doAccount(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, String results)
      throws javax.servlet.ServletException, java.io.IOException {
    try {

      AccountDataBean accountData = tradeService.getAccountData(userID);
      AccountProfileDataBean accountProfileData = accountData.getProfile();

      // Long Run
      Collection<?> orderDataBeans = new ArrayList<Object>();

      req.setAttribute("accountData", accountData);
      req.setAttribute("accountProfileData", accountProfileData);
      req.setAttribute("orderDataBeans", orderDataBeans);
      req.setAttribute("results", results);
      requestDispatch(ctx, req, resp, userID, ACCOUNT_PAGE);
    } catch (java.lang.IllegalArgumentException e) { // this is a user
      // error so I will
      // forward them to another page rather than throw a 500
      req.setAttribute("results", results + "could not find account for userID = " + userID);
      requestDispatch(ctx, req, resp, userID, HOME_PAGE);
      // log the exception with an error level of 3 which means, handled
      // exception but would invalidate a automation run
      Log.error("TradeServletradeService.doAccount(...)", "illegal argument, information should be in exception string",
          e);
    } catch (Exception e) {
      // log the exception with error page
      throw new ServletException("TradeServletradeService.doAccount(...)" + " exception user =" + userID, e);
    }

  }

  /**
   * Update User Profile information such as address, email, etc. for the given
   * Trader Dispatch to the Trade Account JSP for display If any in put is
   * incorrect revert back to the account page w/ an appropriate message
   *
   * @param userID    The User to upddate profile info
   * @param password  The new User password
   * @param cpassword Confirm password
   * @param fullname  The new User fullname info
   * @param address   The new User address info
   * @param cc        The new User credit card info
   * @param email     The new User email info
   * @param ctx       the servlet context
   * @param req       the HttpRequest object
   * @param resp      the HttpResponse object
   * @exception javax.servlet.ServletException If a servlet specific exception is
   *                                           encountered
   * @exception javax.io.IOException           If an exception occurs while
   *                                           writing results back to the user
   *
   */
  void doAccountUpdate(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID,
      String password, String cpassword, String fullName, String address, String creditcard, String email)
      throws javax.servlet.ServletException, java.io.IOException {
    String results = "";

    // First verify input data
    boolean doUpdate = true;
    if (password.equals(cpassword) == false) {
      results = "Update profile error: passwords do not match";
      doUpdate = false;
    } else if (password.length() <= 0 || fullName.length() <= 0 || address.length() <= 0 || creditcard.length() <= 0
        || email.length() <= 0) {
      results = "Update profile error: please fill in all profile information fields";
      doUpdate = false;
    }
    AccountProfileDataBean accountProfileData = new AccountProfileDataBean(userID, password, fullName, address, email,
        creditcard);
    try {
      if (doUpdate) {
        accountProfileData = tradeService.updateAccountProfile(accountProfileData);
        results = "Account profile update successful";
      }

    } catch (java.lang.IllegalArgumentException e) { // this is a user
      // error so I will
      // forward them to another page rather than throw a 500
      req.setAttribute("results",
          results + "invalid argument, check userID is correct, and the database is populated" + userID);
      Log.error(e, "TradeServletradeService.doAccount(...)",
          "illegal argument, information should be in exception string",
          "treating this as a user error and forwarding on to a new page");
    } catch (Exception e) {
      // log the exception with error page
      throw new ServletException("TradeServletradeService.doAccountUpdate(...)" + " exception user =" + userID, e);
    }
    doAccount(ctx, req, resp, userID, results);
  }

  /**
   * Buy a new holding of shares for the given trader Dispatch to the Trade
   * Portfolio JSP for display
   *
   * @param userID The User buying shares
   * @param symbol The stock to purchase
   * @param amount The quantity of shares to purchase
   * @param ctx    the servlet context
   * @param req    the HttpRequest object
   * @param resp   the HttpResponse object
   * @exception javax.servlet.ServletException If a servlet specific exception is
   *                                           encountered
   * @exception javax.io.IOException           If an exception occurs while
   *                                           writing results back to the user
   *
   */
  void doBuy(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, String symbol,
      String quantity) throws ServletException, IOException {

    String results = "";

    try {
      OrderDataBean orderData = tradeService.buy(userID, symbol, new Double(quantity).doubleValue());

      req.setAttribute("orderData", orderData);
      req.setAttribute("results", results);
    } catch (java.lang.IllegalArgumentException e) { // this is a user
      // error so I will
      // forward them to another page rather than throw a 500
      req.setAttribute("results", results + "illegal argument:");
      requestDispatch(ctx, req, resp, userID, HOME_PAGE);
      // log the exception with an error level of 3 which means, handled
      // exception but would invalidate a automation run
      Log.error(e, "TradeServletradeService.doBuy(...)", "illegal argument. userID = " + userID, "symbol = " + symbol);
    } catch (Exception e) {
      // log the exception with error page
      throw new ServletException(
          "TradeServletradeService.buy(...)" + " exception buying stock " + symbol + " for user " + userID, e);
    }
    requestDispatch(ctx, req, resp, userID, ORDER_PAGE);
  }

  /**
   * Create the Trade Home page with personalized information such as the traders
   * account balance Dispatch to the Trade Home JSP for display
   *
   * @param ctx     the servlet context
   * @param req     the HttpRequest object
   * @param resp    the HttpResponse object
   * @param results A short description of the results/success of this web request
   *                provided on the web page
   * @exception javax.servlet.ServletException If a servlet specific exception is
   *                                           encountered
   * @exception javax.io.IOException           If an exception occurs while
   *                                           writing results back to the user
   *
   */
  void doHome(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, String results)
      throws javax.servlet.ServletException, java.io.IOException {

    try {
      AccountDataBean accountData = tradeService.getAccountData(userID);
      Collection<?> holdingDataBeans = tradeService.getHoldings(userID);

      // Edge Caching:
      // Getting the MarketSummary has been moved to the JSP
      // MarketSummary.jsp. This makes the MarketSummary a
      // standalone "fragment", and thus is a candidate for
      // Edge caching.
      // marketSummaryData = tradeService.getMarketSummary();

      req.setAttribute("accountData", accountData);
      req.setAttribute("holdingDataBeans", holdingDataBeans);
      // See Edge Caching above
      // req.setAttribute("marketSummaryData", marketSummaryData);
      req.setAttribute("results", results);
    } catch (java.lang.IllegalArgumentException e) { // this is a user
      // error so I will
      // forward them to another page rather than throw a 500
      req.setAttribute("results", results + "check userID = " + userID + " and that the database is populated");
      requestDispatch(ctx, req, resp, userID, HOME_PAGE);
      // log the exception with an error level of 3 which means, handled
      // exception but would invalidate a automation run
      Log.error("TradeServletradeService.doHome(...)" + "illegal argument, information should be in exception string"
          + "treating this as a user error and forwarding on to a new page", e);
    } /*
       * catch (javax.ejb.FinderException e) { // this is a user error so I will //
       * forward them to another page rather than throw a 500
       * req.setAttribute("results", results + "\nCould not find account for + " +
       * userID); // requestDispatch(ctx, req, resp, //
       * TradeConfig.getPage(TradeConfig.HOME_PAGE)); // log the exception with an
       * error level of 3 which means, handled // exception but would invalidate a
       * automation run Log.error("TradeServletradeService.doHome(...)" +
       * "Error finding account for user " + userID +
       * "treating this as a user error and forwarding on to a new page", e); }
       */ catch (Exception e) {
      // log the exception with error page
      throw new ServletException("TradeServletradeService.doHome(...)" + " exception user =" + userID, e);
    }

    requestDispatch(ctx, req, resp, userID, HOME_PAGE);
  }

  /**
   * Login a Trade User. Dispatch to the Trade Home JSP for display
   *
   * @param userID  The User to login
   * @param passwd  The password supplied by the trader used to authenticate
   * @param ctx     the servlet context
   * @param req     the HttpRequest object
   * @param resp    the HttpResponse object
   * @param results A short description of the results/success of this web request
   *                provided on the web page
   * @exception javax.servlet.ServletException If a servlet specific exception is
   *                                           encountered
   * @exception javax.io.IOException           If an exception occurs while
   *                                           writing results back to the user
   *
   */
  void doLogin(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, String passwd)
      throws javax.servlet.ServletException, java.io.IOException {

    String results = "";
    try {
      AccountDataBean accountData = tradeService.login(userID, passwd);

      if (accountData != null) {
        HttpSession session = req.getSession(true);
        session.setAttribute("uidBean", userID);
        session.setAttribute("sessionCreationDate", new java.util.Date());

        results = "Ready to Trade";
        doHome(ctx, req, resp, userID, results);
        return;
      } else {
        req.setAttribute("results", results + "\nCould not find account for + " + userID);
        // log the exception with an error level of 3 which means,
        // handled exception but would invalidate a automation run
        Log.log("TradeServletradeService.doLogin(...)", "Error finding account for user " + userID + "",
            "user entered a bad username or the database is not populated");
      }
    } catch (java.lang.IllegalArgumentException e) { // this is a user
      // error so I will
      // forward them to another page rather than throw a 500
      req.setAttribute("results", results + "illegal argument:" + e.getMessage());
      // log the exception with an error level of 3 which means, handled
      // exception but would invalidate a automation run
      Log.error(e, "TradeServletradeService.doLogin(...)",
          "illegal argument, information should be in exception string",
          "treating this as a user error and forwarding on to a new page");

    } catch (Exception e) {
      // log the exception with error page
      throw new ServletException(
          "TradeServletradeService.doLogin(...)" + "Exception logging in user " + userID + "with password" + passwd, e);
    }

    requestDispatch(ctx, req, resp, userID, WELCOME_PAGE);

  }

  /**
   * Logout a Trade User Dispatch to the Trade Welcome JSP for display
   *
   * @param userID  The User to logout
   * @param ctx     the servlet context
   * @param req     the HttpRequest object
   * @param resp    the HttpResponse object
   * @param results A short description of the results/success of this web request
   *                provided on the web page
   * @exception javax.servlet.ServletException If a servlet specific exception is
   *                                           encountered
   * @exception javax.io.IOException           If an exception occurs while
   *                                           writing results back to the user
   *
   */
  void doLogout(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID)
      throws ServletException, IOException {
    String results = "";

    try {
      tradeService.logout(userID);

    } catch (java.lang.IllegalArgumentException e) { // this is a user
      // error so I will
      // forward them to another page, at the end of the page.
      req.setAttribute("results", results + "illegal argument:" + e.getMessage());

      // log the exception with an error level of 3 which means, handled
      // exception but would invalidate a automation run
      Log.error(e, "TradeServletradeService.doLogout(...)",
          "illegal argument, information should be in exception string",
          "treating this as a user error and forwarding on to a new page");
    } catch (Exception e) {
      // log the exception and foward to a error page
      Log.error(e, "TradeServletradeService.doLogout(...):", "Error logging out" + userID,
          "fowarding to an error page");
      // set the status_code to 500
      throw new ServletException("TradeServletradeService.doLogout(...)" + "exception logging out user " + userID, e);
    }
    HttpSession session = req.getSession();
    if (session != null) {
      session.invalidate();
    }

    // Added to actually remove a user from the authentication cache
    req.logout();

    Object o = req.getAttribute("TSS-RecreateSessionInLogout");
    if (o != null && ((Boolean) o).equals(Boolean.TRUE)) {
      // Recreate Session object before writing output to the response
      // Once the response headers are written back to the client the
      // opportunity
      // to create a new session in this request may be lost
      // This is to handle only the TradeScenarioServlet case
      session = req.getSession(true);
    }
    requestDispatch(ctx, req, resp, userID, WELCOME_PAGE);
  }

  /**
   * Retrieve the current portfolio of stock holdings for the given trader
   * Dispatch to the Trade Portfolio JSP for display
   *
   * @param userID  The User requesting to view their portfolio
   * @param ctx     the servlet context
   * @param req     the HttpRequest object
   * @param resp    the HttpResponse object
   * @param results A short description of the results/success of this web request
   *                provided on the web page
   * @exception javax.servlet.ServletException If a servlet specific exception is
   *                                           encountered
   * @exception javax.io.IOException           If an exception occurs while
   *                                           writing results back to the user
   *
   */
  void doPortfolio(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, String results)
      throws ServletException, IOException {

    try {
      // Get the holdiings for this user

      Collection<QuoteDataBean> quoteDataBeans = new ArrayList<QuoteDataBean>();
      Collection<?> holdingDataBeans = tradeService.getHoldings(userID);

      // Walk through the collection of user
      // holdings and creating a list of quotes
      if (holdingDataBeans.size() > 0) {

        Iterator<?> it = holdingDataBeans.iterator();
        while (it.hasNext()) {
          HoldingDataBean holdingData = (HoldingDataBean) it.next();
          QuoteDataBean quoteData = tradeService.getQuote(holdingData.getQuoteSymbol());
          quoteDataBeans.add(quoteData);
        }
      } else {
        results = results + ".  Your portfolio is empty.";
      }
      req.setAttribute("results", results);
      req.setAttribute("holdingDataBeans", holdingDataBeans);
      req.setAttribute("quoteDataBeans", quoteDataBeans);
      requestDispatch(ctx, req, resp, userID, PORTFOLIO_PAGE);
    } catch (java.lang.IllegalArgumentException e) { // this is a user
      // error so I will
      // forward them to another page rather than throw a 500
      req.setAttribute("results", results + "illegal argument:" + e.getMessage());
      requestDispatch(ctx, req, resp, userID, PORTFOLIO_PAGE);
      // log the exception with an error level of 3 which means, handled
      // exception but would invalidate a automation run
      Log.error(e, "TradeServletradeService.doPortfolio(...)",
          "illegal argument, information should be in exception string", "user error");
    } catch (Exception e) {
      // log the exception with error page
      throw new ServletException("TradeServletradeService.doPortfolio(...)" + " exception user =" + userID, e);
    }
  }

  /**
   * Retrieve the current Quote for the given stock symbol Dispatch to the Trade
   * Quote JSP for display
   *
   * @param userID The stock symbol used to get the current quote
   * @param ctx    the servlet context
   * @param req    the HttpRequest object
   * @param resp   the HttpResponse object
   * @exception javax.servlet.ServletException If a servlet specific exception is
   *                                           encountered
   * @exception javax.io.IOException           If an exception occurs while
   *                                           writing results back to the user
   *
   */
  void doQuotes(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, String symbols)
      throws ServletException, IOException {

    try {
      // @TODO get a batch results from order service.
      Collection<QuoteDataBean> quoteDataBeans = new ArrayList<QuoteDataBean>();
      String[] symbolsSplit = symbols.split(",");
      for (String symbol : symbolsSplit) {
        QuoteDataBean quoteData = tradeService.getQuote(symbol.trim());
        quoteDataBeans.add(quoteData);
      }
      req.setAttribute("quoteDataBeans", quoteDataBeans);
      requestDispatch(ctx, req, resp, userID, QUOTE_PAGE);

    } catch (Exception e) {
      // log the exception with error page
      throw new ServletException("TradeServletAction.doQuotes(...)" + " exception user =" + userID, e);
    }
  }

  /**
   * Register a new trader given the provided user Profile information such as
   * address, email, etc. Dispatch to the Trade Home JSP for display
   *
   * @param userID   The User to create
   * @param passwd   The User password
   * @param fullname The new User fullname info
   * @param ccn      The new User credit card info
   * @param money    The new User opening account balance
   * @param address  The new User address info
   * @param email    The new User email info
   * @return The userID of the new trader
   * @param ctx  the servlet context
   * @param req  the HttpRequest object
   * @param resp the HttpResponse object
   * @exception javax.servlet.ServletException If a servlet specific exception is
   *                                           encountered
   * @exception javax.io.IOException           If an exception occurs while
   *                                           writing results back to the user
   *
   */
  void doRegister(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, String passwd,
      String cpasswd, String fullname, String ccn, String openBalanceString, String email, String address)
      throws ServletException, IOException {
    String results = "";

    try {
      // Validate user passwords match and are atleast 1 char in length
      if ((passwd.equals(cpasswd)) && (passwd.length() >= 1)) {

        AccountDataBean accountData = tradeService.register(userID, passwd, fullname, address, email, ccn,
            new BigDecimal(openBalanceString));
        if (accountData == null) {
          results = "Registration operation failed;";
          System.out.println(results);
          req.setAttribute("results", results);
          requestDispatch(ctx, req, resp, userID, REGISTER_PAGE);
        } else {
          doLogin(ctx, req, resp, userID, passwd);
          results = "Registration operation succeeded;  Account " + accountData.getAccountID() + " has been created.";
          req.setAttribute("results", results);

        }
      } else {
        // Password validation failed
        results = "Registration operation failed, your passwords did not match";
        System.out.println(results);
        req.setAttribute("results", results);
        requestDispatch(ctx, req, resp, userID, REGISTER_PAGE);
      }

    } catch (Exception e) {
      // log the exception with error page
      throw new ServletException("TradeServletradeService.doRegister(...)" + " exception user =" + userID, e);
    }
  }

  /**
   * Sell a current holding of stock shares for the given trader. Dispatch to the
   * Trade Portfolio JSP for display
   *
   * @param userID The User buying shares
   * @param symbol The stock to sell
   * @param indx   The unique index identifying the users holding to sell
   * @param ctx    the servlet context
   * @param req    the HttpRequest object
   * @param resp   the HttpResponse object
   * @exception javax.servlet.ServletException If a servlet specific exception is
   *                                           encountered
   * @exception javax.io.IOException           If an exception occurs while
   *                                           writing results back to the user
   *
   */
  void doSell(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID, Integer holdingID)
      throws ServletException, IOException {
    String results = "";
    try {
      OrderDataBean orderData = tradeService.sell(userID, holdingID);

      req.setAttribute("orderData", orderData);
      req.setAttribute("results", results);
    } catch (java.lang.IllegalArgumentException e) { // this is a user
      // error so I will
      // just log the exception and then later on I will redisplay the
      // portfolio page
      // because this is just a user exception
      Log.error(e, "TradeServletradeService.doSell(...)", "illegal argument, information should be in exception string",
          "user error");
    } catch (Exception e) {
      // log the exception with error page
      throw new ServletException(
          "TradeServletradeService.doSell(...)" + " exception selling holding " + holdingID + " for user =" + userID,
          e);
    }
    requestDispatch(ctx, req, resp, userID, ORDER_PAGE);
  }

  void doWelcome(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String status)
      throws ServletException, IOException {

    req.setAttribute("results", status);
    requestDispatch(ctx, req, resp, null, WELCOME_PAGE);
  }

  private void requestDispatch(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID,
      String page) throws ServletException, IOException {

    ctx.getRequestDispatcher(page).include(req, resp);
  }

  void doMarketSummary(ServletContext ctx, HttpServletRequest req, HttpServletResponse resp, String userID)
      throws ServletException, IOException {
    req.setAttribute("results", "test");
    requestDispatch(ctx, req, resp, userID, MARKET_SUMMARY_PAGE);

  }
}