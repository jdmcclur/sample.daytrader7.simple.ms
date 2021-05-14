/**
 * (C) Copyright IBM Corporation 2021.
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

import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;

import java.util.List;

public interface OrderService {

  /**
   * Purchase a stock and create a new holding for the given user. Given a stock
   * symbol and quantity to purchase, retrieve the current quote price, debit the
   * user's account balance, and add holdings to user's portfolio. buy/sell are
   * asynchronous, using J2EE messaging, A new order is created and submitted for
   * processing to the TradeBroker
   *
   * @param userID   the customer requesting the stock purchase
   * @param symbol   the symbol of the stock being purchased
   * @param quantity the quantity of shares to purchase
   * @return OrderDataBean providing the status of the newly created buy order
   */

  OrderDataBean buy(String userID, String symbol, double quantity) throws Exception;

  /**
   * Sell a stock holding and removed the holding for the given user. Given a
   * Holding, retrieve current quote, credit user's account, and reduce holdings
   * in user's portfolio.
   *
   * @param userID    the customer requesting the sell
   * @param holdingID the users holding to be sold
   * @return OrderDataBean providing the status of the newly created sell order
   */
  OrderDataBean sell(String userID, Integer holdingID) throws Exception;
  
  /**
   * Cancel the Order identefied by orderID.
   *
   * <p>The boolean twoPhase specifies to the server implementation whether or not
   * the method is to participate in a global transaction
   *
   * @param orderID the Order to complete
   */
  void cancelOrder(Integer orderID) throws Exception;
  
  /**
   * Get the collection of all orders for a given account.
   *
   * @param userID the customer account to retrieve orders for
   * @return Collection OrderDataBeans providing detailed order information
   */
  List<OrderDataBean> getOrders(String userID) throws Exception;

  /**
   * Get the collection of completed orders for a given account that need to be
   * alerted to the user.
   *
   * @param userID the customer account to retrieve orders for
   * @return Collection OrderDataBeans providing detailed order information
   */
  List<OrderDataBean> getClosedOrders(String userID) throws Exception;

}
