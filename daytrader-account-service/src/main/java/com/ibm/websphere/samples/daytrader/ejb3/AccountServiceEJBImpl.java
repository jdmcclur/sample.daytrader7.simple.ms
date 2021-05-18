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

package com.ibm.websphere.samples.daytrader.ejb3;

import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountProfileDataBean;
import com.ibm.websphere.samples.daytrader.interfaces.AccountService;
import com.ibm.websphere.samples.daytrader.util.Log;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.transaction.RollbackException;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AccountServiceEJBImpl implements AccountService {

  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  private Log logService;

  @Override
  public AccountDataBean getAccountData(String userID) {
    if (logService.doTrace()) {
      logService.trace("TradeSLSBBean:getAccountData", userID);
    }

    AccountProfileDataBean profile = entityManager.find(AccountProfileDataBean.class, userID);
    AccountDataBean account = profile.getAccount();

    // Added to populate transient field for account
    account.setProfileID(profile.getUserID());

    return account;
  }

  @Override
  public AccountProfileDataBean getAccountProfileData(String userID) {
    if (logService.doTrace()) {
      logService.trace("TradeSLSBBean:getProfileData", userID);
    }

    return entityManager.find(AccountProfileDataBean.class, userID);
  }

  @Override
  public AccountProfileDataBean updateAccountProfile(AccountProfileDataBean profileData) {
    if (logService.doTrace()) {
      logService.trace("TradeSLSBBean:updateAccountProfileData", profileData);
    }

    AccountProfileDataBean temp = entityManager.find(AccountProfileDataBean.class, profileData.getUserID());
    temp.setAddress(profileData.getAddress());
    temp.setPassword(profileData.getPassword());
    temp.setFullName(profileData.getFullName());
    temp.setCreditCard(profileData.getCreditCard());
    temp.setEmail(profileData.getEmail());

    entityManager.merge(temp);

    return temp;
  }

  @Override
  public AccountDataBean login(String userID, String password) throws RollbackException {
    AccountProfileDataBean profile = entityManager.find(AccountProfileDataBean.class, userID);

    if (profile == null) {
      throw new EJBException("No such user: " + userID);
    }

    AccountDataBean account = profile.getAccount();

    if (logService.doTrace()) {
      logService.trace("TradeSLSBBean:logServicein", userID, password);
    }
    account.login(password);
    if (logService.doTrace()) {
      logService.trace("TradeSLSBBean:logServicein(" + userID + "," + password + ") success" + account);
    }

    return account;
  }

  @Override
  public void logout(String userID) {
    if (logService.doTrace()) {
      logService.trace("TradeSLSBBean:logServiceout", userID);
    }

    AccountProfileDataBean profile = entityManager.find(AccountProfileDataBean.class, userID);
    AccountDataBean account = profile.getAccount();

    account.logout();

    if (logService.doTrace()) {
      logService.trace("TradeSLSBBean:logServiceout(" + userID + ") success");
    }

  }

  @Override
  public AccountDataBean register(String userID, String password, String fullname, String address, String email,
      String creditcard, BigDecimal openBalance) {
    AccountDataBean account = null;
    AccountProfileDataBean profile = null;

    if (logService.doTrace()) {
      logService.trace("TradeSLSBBean:register", userID, password, fullname, address, email, creditcard, openBalance);
    }

    // Check to see if a profile with the desired userID already exists
    profile = entityManager.find(AccountProfileDataBean.class, userID);

    if (profile != null) {
      logService.error("Failed to register new Account - AccountProfile with userID(" + userID + ") already exists");
      return null;
    } else {
      profile = new AccountProfileDataBean(userID, password, fullname, address, email, creditcard);
      account = new AccountDataBean(0, 0, null, new Timestamp(System.currentTimeMillis()), openBalance, openBalance,
          userID);

      profile.setAccount(account);
      account.setProfile(profile);

      entityManager.persist(profile);
      entityManager.persist(account);
    }

    return account;
  }

  @Override
  public BigDecimal updateAccountBalance(String userId, BigDecimal balanceUpdate) {
    AccountProfileDataBean profile = entityManager.find(AccountProfileDataBean.class, userId);
    AccountDataBean temp = profile.getAccount();
    BigDecimal newTotal = temp.getBalance().add(balanceUpdate);
    temp.setBalance(newTotal);
    entityManager.merge(temp);

    return newTotal;
  }
}