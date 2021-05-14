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

package com.ibm.websphere.samples.daytrader.entities;

import javax.json.bind.annotation.JsonbTransient;

public class AccountProfileDataBean implements java.io.Serializable {

  /* Accessor methods for persistent fields */

  private static final long serialVersionUID = 2794584136675420624L;
  private String userID; /* userID */
  private String passwd; /* password */
  private String fullName; /* fullName */
  private String address; /* address */
  private String email; /* email */
  private String creditCard; /* creditCard */

  @JsonbTransient
  private AccountDataBean account;

  public AccountProfileDataBean() {
  }

  public AccountProfileDataBean(String userID, String password, String fullName, String address, String email,
      String creditCard) {
    setUserID(userID);
    setPassword(password);
    setFullName(fullName);
    setAddress(address);
    setEmail(email);
    setCreditCard(creditCard);
  }

  @Override
  public String toString() {
    return "\n\tAccount Profile Data for userID:" + getUserID() + "\n\t\t   passwd:" + getPassword()
        + "\n\t\t   fullName:" + getFullName() + "\n\t\t    address:" + getAddress() + "\n\t\t      email:" + getEmail()
        + "\n\t\t creditCard:" + getCreditCard();
  }

  public String toHTML() {
    return "<BR>Account Profile Data for userID: <B>" + getUserID() + "</B>" + "<LI>   passwd:" + getPassword()
        + "</LI>" + "<LI>   fullName:" + getFullName() + "</LI>" + "<LI>    address:" + getAddress() + "</LI>"
        + "<LI>      email:" + getEmail() + "</LI>" + "<LI> creditCard:" + getCreditCard() + "</LI>";
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public String getPassword() {
    return passwd;
  }

  public void setPassword(String password) {
    this.passwd = password;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCreditCard() {
    return creditCard;
  }

  public void setCreditCard(String creditCard) {
    this.creditCard = creditCard;
  }

  public AccountDataBean getAccount() {
    return account;
  }

  public void setAccount(AccountDataBean account) {
    this.account = account;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (this.userID != null ? this.userID.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {

    if (!(object instanceof AccountProfileDataBean)) {
      return false;
    }
    AccountProfileDataBean other = (AccountProfileDataBean) object;

    if (this.userID != other.userID && (this.userID == null || !this.userID.equals(other.userID))) {
      return false;
    }

    return true;
  }
}