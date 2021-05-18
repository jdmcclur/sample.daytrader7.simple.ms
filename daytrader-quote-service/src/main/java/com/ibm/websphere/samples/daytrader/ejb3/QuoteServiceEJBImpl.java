/**
 * (C) Copyright IBM Corporation 2021
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

import com.ibm.websphere.samples.daytrader.beans.MarketSummaryDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.interfaces.QuoteService;
import com.ibm.websphere.samples.daytrader.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class QuoteServiceEJBImpl implements QuoteService {

  private static final Random r0 = new Random(System.currentTimeMillis());
  private static final int ROUND = BigDecimal.ROUND_HALF_UP;
  private static final int SCALE = 2;
  private static final BigDecimal ONE = new BigDecimal(1.0);
  private static final BigDecimal ZERO = (new BigDecimal(0.00)).setScale(SCALE);
  private static final BigDecimal PENNY_STOCK_PRICE = new BigDecimal(0.01).setScale(2, BigDecimal.ROUND_HALF_UP);
  private static final BigDecimal MAXIMUM_STOCK_PRICE = new BigDecimal(400).setScale(2, BigDecimal.ROUND_HALF_UP);

  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  private Log logService;

  @Override
  public QuoteDataBean createQuote(String symbol, String companyName) {
    try {
      BigDecimal price = new BigDecimal(randomPrice());

      QuoteDataBean quote = new QuoteDataBean(symbol, companyName, 0, price, price, price, price, 0);
      entityManager.persist(quote);
      if (logService.doTrace()) {
        logService.trace("TradeSLSBBean:createQuote-->" + quote);
      }
      return quote;
    } catch (Exception e) {
      logService.error("TradeSLSBBean:createQuote -- exception creating Quote", e);
      throw new EJBException(e);
    }
  }

  @Override
  public QuoteDataBean getQuote(String symbol) {
    if (logService.doTrace()) {
      logService.trace("TradeSLSBBean:getQuote", symbol);
    }

    return entityManager.find(QuoteDataBean.class, symbol);
  }

  @Override
  public List<QuoteDataBean> getAllQuotes() {
    if (logService.doTrace()) {
      logService.trace("TradeSLSBBean:getAllQuotes");
    }

    TypedQuery<QuoteDataBean> query = entityManager.createNamedQuery("quoteejb.allQuotes", QuoteDataBean.class);
    List<QuoteDataBean> quotes = query.getResultList();
    return quotes;
  }

  @Override
  public BigDecimal updateQuotePriceVolume(String symbol, double sharesTraded, String orderType) {

    if (logService.doTrace()) {
      logService.trace("TradeSLSBBean:updateQuote", symbol, sharesTraded);
    }

    TypedQuery<QuoteDataBean> q = entityManager.createNamedQuery("quoteejb.quoteForUpdate", QuoteDataBean.class);
    q.setParameter(1, symbol);
    QuoteDataBean quote = q.getSingleResult();

    BigDecimal oldPrice = quote.getPrice();
    BigDecimal openPrice = quote.getOpen();

    BigDecimal changeFactor = getRandomPriceChangeFactor(orderType);
    BigDecimal newPrice = changeFactor.multiply(oldPrice).setScale(2, BigDecimal.ROUND_HALF_UP);

    if (newPrice.compareTo(PENNY_STOCK_PRICE) < 0) {
      newPrice = PENNY_STOCK_PRICE;
    } else if (newPrice.compareTo(MAXIMUM_STOCK_PRICE) > 0) {
      newPrice = MAXIMUM_STOCK_PRICE;
    }

    quote.setPrice(newPrice);
    quote.setChange(newPrice.subtract(openPrice).doubleValue());
    quote.setVolume(quote.getVolume() + sharesTraded);

    if (newPrice.compareTo(quote.getLow()) < 0) {
      quote.setLow(newPrice);
    } else if (newPrice.compareTo(quote.getHigh()) > 0) {
      quote.setHigh(newPrice);
    }

    entityManager.merge(quote);

    /*
     * send to mksummary publishQuotePriceChange(quote, oldPrice, changeFactor,
     * sharesTraded); } recentQuotePriceChangeList.add(quote);
     */

    return quote.getPrice();

  }

  class QuotePriceComparator implements Comparator<Object> {

    @Override
    public int compare(Object quote1, Object quote2) {
      double change1 = ((QuoteDataBean) quote1).getChange();
      double change2 = ((QuoteDataBean) quote2).getChange();
      return new Double(change2).compareTo(change1);
    }

  }

  private BigDecimal getRandomPriceChangeFactor(String orderType) {
    // CJB (DAYTRADER-25) - Vary change factor between 1.1 and 0.9
    double percentGain = randomFloat(1) * 0.1;
    if (orderType.equals("sell")) {
      percentGain *= -1;
    }
    percentGain += 1;

    // change factor is between +/- 20%
    BigDecimal percentGainBD = (new BigDecimal(percentGain)).setScale(2, BigDecimal.ROUND_HALF_UP);
    if (percentGainBD.doubleValue() <= 0.0) {
      percentGainBD = ONE;
    }

    return percentGainBD;
  }

  private float randomFloat(int i) {
    return (new Float(random() * i)).floatValue();
  }

  public float randomPrice() {
    return ((new Integer(randomInt(200))).floatValue()) + 1.0f;
  }

  private int randomInt(int i) {
    return (new Float(random() * i)).intValue();
  }

  private double random() {
    return r0.nextDouble();
  }

  @Override
  public MarketSummaryDataBean getMarketSummary() {
    List<QuoteDataBean> quotes;

    try {
      // Find Trade Stock Index Quotes (Top 100 quotes) ordered by their change in
      // value
      CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
      CriteriaQuery<QuoteDataBean> criteriaQuery = criteriaBuilder.createQuery(QuoteDataBean.class);
      Root<QuoteDataBean> quoteRoot = criteriaQuery.from(QuoteDataBean.class);
      criteriaQuery.orderBy(criteriaBuilder.desc(quoteRoot.get("change1")));
      criteriaQuery.select(quoteRoot);
      TypedQuery<QuoteDataBean> q = entityManager.createQuery(criteriaQuery);
      quotes = q.getResultList();
    } catch (Exception e) {
      logService.debug(
          "Warning: The database has not been configured. If this is the first time the application has been started, "
          + " please create and populate the database tables. Then restart the server.");
      return null;
    }

    /* TODO: Make this cleaner? */
    QuoteDataBean[] quoteArray = quotes.toArray(new QuoteDataBean[quotes.size()]);
    ArrayList<QuoteDataBean> topGainers = new ArrayList<QuoteDataBean>(5);
    ArrayList<QuoteDataBean> topLosers = new ArrayList<QuoteDataBean>(5);
    BigDecimal tsia = ZERO;
    BigDecimal openTSIA = ZERO;
    double totalVolume = 0.0;

    if (quoteArray.length > 5) {
      for (int i = 0; i < 5; i++) {
        topGainers.add(quoteArray[i]);
      }
      for (int i = quoteArray.length - 1; i >= quoteArray.length - 5; i--) {
        topLosers.add(quoteArray[i]);
      }

      for (QuoteDataBean quote : quoteArray) {
        BigDecimal price = quote.getPrice();
        BigDecimal open = quote.getOpen();
        double volume = quote.getVolume();
        tsia = tsia.add(price);
        openTSIA = openTSIA.add(open);
        totalVolume += volume;
      }
      tsia = tsia.divide(new BigDecimal(quoteArray.length), ROUND);
      openTSIA = openTSIA.divide(new BigDecimal(quoteArray.length), ROUND);
    }

    return new MarketSummaryDataBean(tsia, openTSIA, totalVolume, topGainers, topLosers);
  }

  @Override
  public List<QuoteDataBean> getQuotes(String symbols) {
    List<QuoteDataBean> quoteDataBeans = new ArrayList<QuoteDataBean>();

    String[] symbolsSplit = symbols.split(",");
    for (String symbol : symbolsSplit) {
      QuoteDataBean quoteData = entityManager.find(QuoteDataBean.class, symbol.trim());
      quoteDataBeans.add(quoteData);
    }
    return quoteDataBeans;

  }
}