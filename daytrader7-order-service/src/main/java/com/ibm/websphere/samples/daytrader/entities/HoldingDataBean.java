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

package com.ibm.websphere.samples.daytrader.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class HoldingDataBean implements  Serializable {

  /* persistent/relationship fields */

  private static final long serialVersionUID = -2338411656251935480L;

  private Integer holdingID; /* holdingID */

  private double quantity; /* quantity */

  private BigDecimal purchasePrice; /* purchasePrice */

  private Date purchaseDate; /* purchaseDate */

  private String accountId;

  private String quoteSymbol;

  public HoldingDataBean() {
  }

  public HoldingDataBean(Integer holdingID, double quantity, BigDecimal purchasePrice, Date purchaseDate,
      String quoteSymbol) {
    setHoldingID(holdingID);
    setQuantity(quantity);
    setPurchasePrice(purchasePrice);
    setPurchaseDate(purchaseDate);
    setQuoteSymbol(quoteSymbol);
  }

  public HoldingDataBean(double quantity, BigDecimal purchasePrice, Date purchaseDate, String accountId,
      String quoteSymbol) {
    setQuantity(quantity);
    setPurchasePrice(purchasePrice);
    setPurchaseDate(purchaseDate);
    setAccountId(accountId);
    setQuoteSymbol(quoteSymbol);
  } 

  @Override
  public String toString() {
    return "\n\tHolding Data for holding: " + getHoldingID() + "\n\t\t      quantity:" + getQuantity()
        + "\n\t\t purchasePrice:" + getPurchasePrice() + "\n\t\t  purchaseDate:" + getPurchaseDate();
  }

  public String toHTML() {
    return "<BR>Holding Data for holding: " + getHoldingID() + "</B>" + "<LI>      quantity:" + getQuantity() + "</LI>"
        + "<LI> purchasePrice:" + getPurchasePrice() + "</LI>" + "<LI>  purchaseDate:" + getPurchaseDate() + "</LI>";
  }

  public Integer getHoldingID() {
    return holdingID;
  }

  public void setHoldingID(Integer holdingID) {
    this.holdingID = holdingID;
  }

  public double getQuantity() {
    return quantity;
  }

  public void setQuantity(double quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getPurchasePrice() {
    return purchasePrice;
  }

  public void setPurchasePrice(BigDecimal purchasePrice) {
    this.purchasePrice = purchasePrice;
  }

  public Date getPurchaseDate() {
    return purchaseDate;
  }

  public void setPurchaseDate(Date purchaseDate) {
    this.purchaseDate = purchaseDate;
  }


  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getQuoteSymbol() {
    return quoteSymbol;
  }

  public void setQuoteSymbol(String quoteSymbol) {
    this.quoteSymbol = quoteSymbol;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (this.holdingID != null ? this.holdingID.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {

    if (!(object instanceof HoldingDataBean)) {
      return false;
    }
    HoldingDataBean other = (HoldingDataBean) object;

    if (this.holdingID != other.holdingID && (this.holdingID == null || !this.holdingID.equals(other.holdingID))) {
      return false;
    }

    return true;
  }
}


