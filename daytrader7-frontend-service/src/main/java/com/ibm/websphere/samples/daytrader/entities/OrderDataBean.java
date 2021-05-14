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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrderDataBean implements Serializable {

  private static final long serialVersionUID = 120650490200739057L;


  private Integer orderID; /* orderID */
  private String orderType; /* orderType (buy, sell, etc.) */
  private String orderStatus; /*
                               * orderStatus (open, processing, completed, closed, cancelled)
                               */
  private Date openDate; /* openDate (when the order was entered) */
  private Date completionDate; /* completionDate */
  private double quantity; /* quantity */
  private BigDecimal price; /* price */
  private BigDecimal orderFee; /* price */
  private String accountId;
  private String quoteSymbol;
  private Integer holdingId;

  public OrderDataBean() {
  }

  public OrderDataBean(Integer orderID, String orderType, String orderStatus, Date openDate, Date completionDate,
      double quantity, BigDecimal price, BigDecimal orderFee, String symbol) {
    setOrderID(orderID);
    setOrderType(orderType);
    setOrderStatus(orderStatus);
    setOpenDate(openDate);
    setCompletionDate(completionDate);
    setQuantity(quantity);
    setPrice(price);
    setOrderFee(orderFee);
    setQuoteSymbol(symbol);
  }

  public OrderDataBean(String orderType, String orderStatus, Date openDate, Date completionDate, double quantity,
      BigDecimal price, BigDecimal orderFee, String accountId, String quoteSymbol, Integer holdingId) {
    setOrderType(orderType);
    setOrderStatus(orderStatus);
    setOpenDate(openDate);
    setCompletionDate(completionDate);
    setQuantity(quantity);
    setPrice(price);
    setOrderFee(orderFee);
    setAccountId(accountId);
    setQuoteSymbol(quoteSymbol);
    setHoldingId(holdingId);
  }

  @Override
  public String toString() {
    return "Order " + getOrderID() + "\n\t      orderType: " + getOrderType() + "\n\t    orderStatus: "
        + getOrderStatus() + "\n\t       openDate: " + getOpenDate() + "\n\t completionDate: " + getCompletionDate()
        + "\n\t       quantity: " + getQuantity() + "\n\t          price: " + getPrice() + "\n\t       orderFee: "
        + getOrderFee() + "\n\t         symbol: " + getQuoteSymbol();
  }

  public String toHTML() {
    return "<BR>Order <B>" + getOrderID() + "</B>" + "<LI>      orderType: " + getOrderType() + "</LI>"
        + "<LI>    orderStatus: " + getOrderStatus() + "</LI>" + "<LI>       openDate: " + getOpenDate() + "</LI>"
        + "<LI> completionDate: " + getCompletionDate() + "</LI>" + "<LI>       quantity: " + getQuantity() + "</LI>"
        + "<LI>          price: " + getPrice() + "</LI>" + "<LI>       orderFee: " + getOrderFee() + "</LI>"
        + "<LI>         symbol: " + getQuoteSymbol() + "</LI>";
  }

  public Integer getOrderID() {
    return orderID;
  }

  public void setOrderID(Integer orderID) {
    this.orderID = orderID;
  }

  public String getOrderType() {
    return orderType;
  }

  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }

  public String getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }

  public Date getOpenDate() {
    return openDate;
  }

  public void setOpenDate(Date openDate) {
    this.openDate = openDate;
  }

  public Date getCompletionDate() {
    return completionDate;
  }

  public void setCompletionDate(Date completionDate) {
    this.completionDate = completionDate;
  }

  public double getQuantity() {
    return quantity;
  }

  public void setQuantity(double quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getOrderFee() {
    return orderFee;
  }

  public void setOrderFee(BigDecimal orderFee) {
    this.orderFee = orderFee;
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

  public Integer getHoldingId() {
    return holdingId;
  }

  public void setHoldingId(Integer holdingId) {
    this.holdingId = holdingId;
  }

  public void cancel() {
    setOrderStatus("cancelled");
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (this.orderID != null ? this.orderID.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {

    if (!(object instanceof OrderDataBean)) {
      return false;
    }
    OrderDataBean other = (OrderDataBean) object;
    if (this.orderID != other.orderID && (this.orderID == null || !this.orderID.equals(other.orderID))) {
      return false;
    }
    return true;
  }
}

