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

package com.ibm.websphere.samples.daytrader.util;

import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.restclient.AccountClient;
import com.ibm.websphere.samples.daytrader.restclient.HoldingClient;
import com.ibm.websphere.samples.daytrader.restclient.OrderClient;
import com.ibm.websphere.samples.daytrader.restclient.QuoteClient;

import java.math.BigDecimal;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * TradeBuildDB uses operations provided by the TradeApplication to (a) create
 * the Database tables (b)populate a DayTrader database without creating the
 * tables. Specifically, a new DayTrader User population is created using
 * UserIDs of the form "uid:xxx" where xxx is a sequential number (e.g. uid:0,
 * uid:1, etc.). New stocks are also created of the form "s:xxx", again where
 * xxx represents sequential numbers (e.g. s:1, s:2, etc.)
 */
@ApplicationScoped
public class DBLoader {

  @Inject
  TradeConfig configService;

  @Inject
  Log logService;

  @Inject
  @RestClient
  QuoteClient quoteClient;

  @Inject
  @RestClient
  AccountClient accountClient;

  @Inject
  @RestClient
  OrderClient orderClient;

  @Inject
  @RestClient
  HoldingClient holdingClient;

  private Random randomNumberGenerator = new Random(System.currentTimeMillis());

  public void createDB() throws Exception {
    quoteClient.createDB();
    accountClient.createDB();
    holdingClient.createDB();
    orderClient.createDB();
  }

  public void populateDB(java.io.PrintWriter out) throws Exception {
    String symbol;
    String companyName;
    int errorCount = 0; // Give up gracefully after 10 errors

    out.println("<BR>TradeBuildDB: **** Creating " + configService.getMaxQuotes() + " Quotes ****</BR>");
    

    try {
      // Attempt to delete all of the Trade users and Trade Quotes first
      resetTrade(true);
    } catch (Exception e) {
      e.printStackTrace();
      logService.error(e, "TradeBuildDB: Unable to delete Trade users (uid:0, uid:1, ...) and Trade Quotes (s:0, s:1, ...)");
    }
    for (int i = 0; i < configService.getMaxQuotes(); i++) {
      symbol = "s:" + i;
      companyName = "S" + i + " Incorporated";
      try {
        quoteClient.createQuote(symbol, companyName);
        if (i % 10 == 0) {
          out.print("....." + symbol);
          if (i % 100 == 0) {
            out.println(" -<BR>");
            out.flush();
          }
        }
      } catch (Exception e) {
        if (errorCount++ >= 10) {
          String error = "Populate Trade DB aborting after 10 create quote errors. Check the EJB datasource configuration. "
              + "Check the log for details <BR><BR> Exception is: <BR> " + e.toString();
          logService.error(e, error);
          throw e;
        }
      }
    }
    out.println("<BR>");
    out.println("<BR>**** Registering " + configService.getMaxUsers() + " Users **** ");
    errorCount = 0; // reset for user registrations

    // Registration is a formal operation in Trade 2.
    for (int i = 0; i < configService.getMaxUsers(); i++) {
      String userID = "uid:" + i;
      String fullname = randomFullName();
      String email = randomEmail(userID);
      String address = randomAddress();
      String creditcard = randomCreditCard();
      double initialBalance = (double) (randomInt(100000)) + 200000;
      if (i == 0) {
        initialBalance = 1000000; // uid:0 starts with a cool million.
      }
      try {
        AccountDataBean accountData = accountClient.register(userID, "xxx", fullname, address, email, creditcard,
            new BigDecimal(initialBalance));

        if (accountData != null) {
          if (i % 50 == 0) {
            out.print("<BR>Account# " + accountData.getAccountID() + " userID=" + userID);
          } // end-if

          // 0-MAX_HOLDING (inclusive), avg
          // holdings per user = (MAX-0)/2
          int holdings = randomInt(configService.getMaxHoldings() + 1);

          double quantity = 0;

          for (int j = 0; j < holdings; j++) {
            symbol = randomSymbol();
            quantity = randomQuantity();
            orderClient.buy(userID, symbol, quantity);
            quoteClient.updateQuotePriceVolume(symbol, quantity, "buy");
          } // end-for
          if (i % 50 == 0) {
            out.println(" has " + holdings + " holdings.");
            out.flush();
          } // end-if
        } else {
          out.println("<BR>UID " + userID + " already registered.</BR>");
          out.flush();
        } // end-if

      } catch (Exception e) {
        if (errorCount++ >= 10) {
          String error = "Populate Trade DB aborting after 10 user registration errors. Check the log for details. <BR><BR> Exception is: <BR>"
              + e.toString();
          logService.error(e, error);
          e.printStackTrace();
          throw e;
         
        }
      }
    } // end-for
    out.println("</BODY>");
    System.out.println("done");
  }
  
  public void resetTrade(Boolean deleteAll) throws Exception {
    System.out.println("resetTrade " + deleteAll);
    quoteClient.resetDB(deleteAll);
    accountClient.resetDB(deleteAll);
    holdingClient.resetDB(deleteAll);
    orderClient.resetDB(deleteAll);
  }
  
  private String randomFullName() {
    return "first:" + randomInt(1000) + " last:" + randomInt(5000);
  }

  private String randomAddress() {
    return randomInt(1000) + " Oak St.";
  }

  private String randomCreditCard() {
    return randomInt(100) + "-" + randomInt(1000) + "-" + randomInt(1000) + "-" + randomInt(1000);
  }

  private String randomEmail(String userID) {
    return userID.replace(":", "") + "@" + randomInt(100) + ".com";
  }

  private float randomQuantity() {
    return ((new Integer(randomInt(200))).floatValue()) + 1.0f;
  }

  private String randomSymbol() {
    return "s:" + randomInt(configService.getMaxQuotes() - 1);
  }

  private int randomInt(int i) {
    return (new Float(random() * i)).intValue();
  }

  public double random() {
    return randomNumberGenerator.nextDouble();
  }

}
