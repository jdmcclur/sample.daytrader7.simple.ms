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

import com.ibm.cardinal.util.KluInterface;

import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.FinancialUtils;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class MarketSummaryDataBean implements KluInterface, Serializable {
    //Cardinal generated attributes
    private String klu__referenceID = "";

    //Cardinal generated method
    public String getKlu__referenceID() {
        return klu__referenceID;
    }

    //Cardinal generated method
    public void setKlu__referenceID(String refID) {
        klu__referenceID = refID;
    }




  private static final long serialVersionUID = 650652242288745600L;
  private BigDecimal tsia; /* Trade Stock Index Average */
  private BigDecimal openTsia; /* Trade Stock Index Average at the open */
  private double volume; /* volume of shares traded */
  private Collection<QuoteDataBean> topGainers; /*
                                                 * Collection of top gaining stocks
                                                 */
  private Collection<QuoteDataBean> topLosers; /*
                                                * Collection of top losing stocks
                                                */
  // FUTURE private Collection topVolume; /* Collection of top stocks by
  // volume */
  private Date summaryDate; /* Date this summary was taken */

  // cache the gainPercent once computed for this bean
  private BigDecimal gainPercent = null;

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
    setSummaryDate(new java.sql.Date(System.currentTimeMillis()));
    gainPercent = FinancialUtils.computeGainPercent(getTsia(), getOpenTsia());

  }

  public static MarketSummaryDataBean getRandomInstance() {
    Collection<QuoteDataBean> gain = new ArrayList<QuoteDataBean>();
    Collection<QuoteDataBean> lose = new ArrayList<QuoteDataBean>();

    for (int ii = 0; ii < 5; ii++) {
      QuoteDataBean quote1 = QuoteDataBean.getRandomInstance();
      QuoteDataBean quote2 = QuoteDataBean.getRandomInstance();

      gain.add(quote1);
      lose.add(quote2);
    }

    return new MarketSummaryDataBean(TradeConfig.rndBigDecimal(1000000.0f), TradeConfig.rndBigDecimal(1000000.0f),
        TradeConfig.rndQuantity(), gain, lose);
  }

  @Override
  public String toString() {
    String ret = "\n\tMarket Summary at: " + getSummaryDate() + "\n\t\t        TSIA:" + getTsia()
        + "\n\t\t    openTSIA:" + getOpenTsia() + "\n\t\t        gain:" + getGainPercent() + "\n\t\t      volume:"
        + getVolume();

    if ((getTopGainers() == null) || (getTopLosers() == null)) {
      return ret;
    }
    ret += "\n\t\t   Current Top Gainers:";
    Iterator<QuoteDataBean> it = getTopGainers().iterator();
    while (it.hasNext()) {
      QuoteDataBean quoteData = it.next();
      ret += ("\n\t\t\t" + quoteData.toString());
    }
    ret += "\n\t\t   Current Top Losers:";
    it = getTopLosers().iterator();
    while (it.hasNext()) {
      QuoteDataBean quoteData = it.next();
      ret += ("\n\t\t\t" + quoteData.toString());
    }
    return ret;
  }

  public String toHTML() {
    String ret = "<BR>Market Summary at: " + getSummaryDate() + "<LI>        TSIA:" + getTsia() + "</LI>"
        + "<LI>    openTSIA:" + getOpenTsia() + "</LI>" + "<LI>      volume:" + getVolume() + "</LI>";
    if ((getTopGainers() == null) || (getTopLosers() == null)) {
      return ret;
    }
    ret += "<BR> Current Top Gainers:";
    Iterator<QuoteDataBean> it = getTopGainers().iterator();

    while (it.hasNext()) {
      QuoteDataBean quoteData = it.next();
      ret += ("<LI>" + quoteData.toString() + "</LI>");
    }
    ret += "<BR>   Current Top Losers:";
    it = getTopLosers().iterator();
    while (it.hasNext()) {
      QuoteDataBean quoteData = it.next();
      ret += ("<LI>" + quoteData.toString() + "</LI>");
    }
    return ret;
  }

  public JsonObject toJson() {

    JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

    int i = 1;
    for (Iterator<QuoteDataBean> iterator = topGainers.iterator(); iterator.hasNext();) {
      QuoteDataBean quote = iterator.next();

      jsonObjectBuilder.add("gainer" + i + "_stock", quote.getSymbol());
      jsonObjectBuilder.add("gainer" + i + "_price", "$" + quote.getPrice());
      jsonObjectBuilder.add("gainer" + i + "_change", quote.getChange());
      i++;
    }

    i = 1;
    for (Iterator<QuoteDataBean> iterator = topLosers.iterator(); iterator.hasNext();) {
      QuoteDataBean quote = iterator.next();

      jsonObjectBuilder.add("loser" + i + "_stock", quote.getSymbol());
      jsonObjectBuilder.add("loser" + i + "_price", "$" + quote.getPrice());
      jsonObjectBuilder.add("loser" + i + "_change", quote.getChange());
      i++;
    }

    jsonObjectBuilder.add("tsia", tsia);
    jsonObjectBuilder.add("volume", volume);
    jsonObjectBuilder.add("date", summaryDate.toString());

    return jsonObjectBuilder.build();

  }

  public void print() {
    Log.log(this.toString());
  }

  public BigDecimal getGainPercent() {
    if (gainPercent == null) {
      gainPercent = FinancialUtils.computeGainPercent(getTsia(), getOpenTsia());
    }
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
  public Date getSummaryDate() {
    return summaryDate;
  }

  /**
   * Sets the summaryDate.
   *
   * @param summaryDate The summaryDate to set
   */
  public void setSummaryDate(Date summaryDate) {
    this.summaryDate = summaryDate;
  }

}