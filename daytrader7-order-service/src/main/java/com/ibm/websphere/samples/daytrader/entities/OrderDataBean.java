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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity(name = "orderejb")
@Table(name = "orderejb")
@NamedQueries({
    @NamedQuery(name = "orderejb.findByOrderfee", query = "SELECT o FROM orderejb o WHERE o.orderFee = :orderfee"),
    @NamedQuery(name = "orderejb.findByCompletiondate", query = "SELECT o FROM orderejb o WHERE o.completionDate = :completiondate"),
    @NamedQuery(name = "orderejb.findByOrdertype", query = "SELECT o FROM orderejb o WHERE o.orderType = :ordertype"),
    @NamedQuery(name = "orderejb.findByOrderstatus", query = "SELECT o FROM orderejb o WHERE o.orderStatus = :orderstatus"),
    @NamedQuery(name = "orderejb.findByPrice", query = "SELECT o FROM orderejb o WHERE o.price = :price"),
    @NamedQuery(name = "orderejb.findByQuantity", query = "SELECT o FROM orderejb o WHERE o.quantity = :quantity"),
    @NamedQuery(name = "orderejb.findByOpendate", query = "SELECT o FROM orderejb o WHERE o.openDate = :opendate"),
    @NamedQuery(name = "orderejb.findByOrderid", query = "SELECT o FROM orderejb o WHERE o.orderID = :orderid"),
    @NamedQuery(name = "orderejb.findByAccountId", query = "SELECT o FROM orderejb o WHERE o.accountId = :accountid"),
    @NamedQuery(name = "orderejb.findByQuoteSymbol", query = "SELECT o FROM orderejb o WHERE o.quoteSymbol = :quoteSymbol"),
    @NamedQuery(name = "orderejb.findByHoldingHoldingid", query = "SELECT o FROM orderejb o WHERE o.holdingId = :holdingid"),
    @NamedQuery(name = "orderejb.closedOrders", query = "SELECT o FROM orderejb o WHERE o.orderStatus = 'closed' AND o.accountId  = :userID"),
    @NamedQuery(name = "orderejb.completeClosedOrders", query = "UPDATE orderejb o SET o.orderStatus = 'completed' WHERE o.orderStatus = 'closed' AND o.accountId  = :userID") })
public class OrderDataBean implements Serializable {

  private static final long serialVersionUID = 120650490200739057L;

  @Id
  @TableGenerator(name = "orderIdGen", table = "KEYGENEJB", pkColumnName = "KEYNAME", valueColumnName = "KEYVAL", pkColumnValue = "order", allocationSize = 1000)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "orderIdGen")
  @Column(name = "ORDERID", nullable = false)
  private Integer orderID; /* orderID */

  @Column(name = "ORDERTYPE")
  private String orderType; /* orderType (buy, sell, etc.) */

  @Column(name = "ORDERSTATUS")
  private String orderStatus; /*
                               * orderStatus (open, processing, completed, closed, cancelled)
                               */

  @Column(name = "OPENDATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date openDate; /* openDate (when the order was entered) */

  @Column(name = "COMPLETIONDATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date completionDate; /* completionDate */

  @NotNull
  @Column(name = "QUANTITY", nullable = false)
  private double quantity; /* quantity */

  @Column(name = "PRICE")
  private BigDecimal price; /* price */

  @Column(name = "ORDERFEE")
  private BigDecimal orderFee; /* price */

  @Column(name = "ACCOUNTID")
  private String accountId;

  @Column(name = "QUOTE_SYMBOL")
  private String quoteSymbol;

  @Column(name = "HOLDINGID")
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

