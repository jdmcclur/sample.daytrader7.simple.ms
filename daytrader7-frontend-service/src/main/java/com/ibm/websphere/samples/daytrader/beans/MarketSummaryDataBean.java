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

package com.ibm.websphere.samples.daytrader.beans;

import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;


public class MarketSummaryDataBean implements Serializable {

  private static final long serialVersionUID = 650652242288745600L;
  
  private BigDecimal tsia; /* Trade Stock Index Average */
  private BigDecimal openTsia; /* Trade Stock Index Average at the open */
  private double volume; /* volume of shares traded */
  private Collection<QuoteDataBean> topGainers; 
  private Collection<QuoteDataBean> topLosers; 
  private LocalDate summaryDate; /* Date this summary was taken */

  // cache the gainPercent once computed for this bean
  private BigDecimal gainPercent = null;
  private static final int ROUND = BigDecimal.ROUND_HALF_UP;
  private static final int SCALE = 2;
  private static final BigDecimal ZERO = (new BigDecimal(0.00)).setScale(SCALE);
  private static final BigDecimal ONE = (new BigDecimal(1.00)).setScale(SCALE);
  private static final BigDecimal HUNDRED = (new BigDecimal(100.00)).setScale(SCALE);

  public MarketSummaryDataBean() {
  }

  public MarketSummaryDataBean(BigDecimal TSIA, BigDecimal openTSIA, double volume,
      Collection<QuoteDataBean> topGainers, Collection<QuoteDataBean> topLosers// , Collection topVolume
  ) {
    setTsia(TSIA);
    setOpenTsia(openTSIA);
    setVolume(volume);
    setTopGainers(topGainers);
    setTopLosers(topLosers);
    setSummaryDate(LocalDate.now());
    gainPercent = computeGainPercent(getTsia(), getOpenTsia());

  }

  
  
  public BigDecimal getGainPercent() {
    if (gainPercent == null) {
      gainPercent = computeGainPercent(getTsia(), getOpenTsia());
    }
    return gainPercent;
  }

  private BigDecimal computeGainPercent(BigDecimal currentBalance, BigDecimal openBalance) {
    if (openBalance.doubleValue() == 0.0) {
      return ZERO;
    }
    BigDecimal gainPercent = currentBalance.divide(openBalance, ROUND).subtract(ONE).multiply(HUNDRED);
    return gainPercent;
  }

  /**
   * Gets the tSIA.
   *
   * @return Returns a BigDecimal
   */
  public BigDecimal getTsia() {
    return tsia;
  }

  /**
   * Sets the tSIA.
   *
   * @param tsia The tSIA to set
   */
  public void setTsia(BigDecimal tsia) {
    this.tsia = tsia;
  }

  /**
   * Gets the openTSIA.
   *
   * @return Returns a BigDecimal
   */
  public BigDecimal getOpenTsia() {
    return openTsia;
  }

  /**
   * Sets the openTSIA.
   *
   * @param openTsia The openTSIA to set
   */
  public void setOpenTsia(BigDecimal openTsia) {
    this.openTsia = openTsia;
  }

  /**
   * Gets the volume.
   *
   * @return Returns a BigDecimal
   */
  public double getVolume() {
    return volume;
  }

  /**
   * Sets the volume.
   *
   * @param volume The volume to set
   */
  public void setVolume(double volume) {
    this.volume = volume;
  }

  /**
   * Gets the topGainers.
   *
   * @return Returns a Collection
   */
  public Collection<QuoteDataBean> getTopGainers() {
    return topGainers;
  }

  /**
   * Sets the topGainers.
   *
   * @param topGainers The topGainers to set
   */
  public void setTopGainers(Collection<QuoteDataBean> topGainers) {
    this.topGainers = topGainers;
  }

  /**
   * Gets the topLosers.
   *
   * @return Returns a Collection
   */
  public Collection<QuoteDataBean> getTopLosers() {
    return topLosers;
  }

  /**
   * Sets the topLosers.
   *
   * @param topLosers The topLosers to set
   */
  public void setTopLosers(Collection<QuoteDataBean> topLosers) {
    this.topLosers = topLosers;
  }

  /**
   * Gets the summaryDate.
   *
   * @return Returns a Date
   */
  public LocalDate getSummaryDate() {
    return summaryDate;
  }

  /**
   * Sets the summaryDate.
   *
   * @param summaryDate The summaryDate to set
   */
  public void setSummaryDate(LocalDate summaryDate) {
    this.summaryDate = summaryDate;
  }

}

