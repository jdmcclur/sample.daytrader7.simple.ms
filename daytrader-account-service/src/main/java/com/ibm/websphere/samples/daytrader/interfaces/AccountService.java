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

import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountProfileDataBean;

import java.math.BigDecimal;

public interface AccountService {

  /**
   * Return an AccountDataBean object for userID describing the account.
   *
   * @param userID the account userID to lookup
   * @return User account data in AccountDataBean
   */
  AccountDataBean getAccountData(String userID) throws Exception;

  /**
   * Return an AccountProfileDataBean for userID providing the users profile.
   *
   * @param userID the account userID to lookup
   */
  AccountProfileDataBean getAccountProfileData(String userID) throws Exception;

  /**
   * Update userID's account profile information using the provided
   * AccountProfileDataBean object.
   *
   * @param profileData account profile data in AccountProfileDataBean
   */
  AccountProfileDataBean updateAccountProfile(AccountProfileDataBean profileData) throws Exception;

  /**
   * Attempt to authenticate and login a user with the given password.
   *
   * @param userID   the customer to login
   * @param password the password entered by the customer for authentication
   * @return User account data in AccountDataBean
   */
  AccountDataBean login(String userID, String password) throws Exception;

  /**
   * Logout the given user.
   *
   * @param userID the customer to logout
   */

  void logout(String userID) throws Exception;

  /**
   * Register a new Trade customer. Create a new user profile, user registry
   * entry, account with initial balance, and empty portfolio.
   *
   * @param userID      the new customer to register
   * @param password    the customers password
   * @param fullname    the customers fullname
   * @param address     the customers street address
   * @param email       the customers email address
   * @param creditcard  the customers creditcard number
   * @param openBalance the amount to charge to the customers credit to open the
   *                    account and set the initial balance
   * @return the userID if successful, null otherwise
   */
  AccountDataBean register(String userID, String password, String fullname, String address, String email,
      String creditcard, BigDecimal openBalance) throws Exception;

  BigDecimal updateAccountBalance(String userId, BigDecimal balanceUpdate) throws Exception;

}
