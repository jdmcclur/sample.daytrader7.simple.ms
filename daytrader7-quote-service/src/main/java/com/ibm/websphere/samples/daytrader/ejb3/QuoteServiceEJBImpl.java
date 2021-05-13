/**
 * (C) Copyright IBM Corporation 2015, 2021
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

import com.ibm.websphere.samples.daytrader.interfaces.QuoteService;
import com.ibm.websphere.samples.daytrader.beans.MarketSummaryDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
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

  private final Random r0 = new Random(System.currentTimeMillis());
  public final int ROUND = BigDecimal.ROUND_HALF_UP;
  public final int SCALE = 2;
  private final BigDecimal ONE = new BigDecimal(1.0);
  public final BigDecimal ZERO = (new BigDecimal(0.00)).setScale(SCALE);
  private BigDecimal pennyStockPrice;
  private BigDecimal pennyStockRevoveryMiracleMultiplier;
  private BigDecimal maximumStockPrice;
  private BigDecimal maximumStockSplitMultiplier;

  @PersistenceContext
  private EntityManager entityManager;

  @Resource
  private SessionContext context;

  @Inject
  private Log Log;

  @Override
  public QuoteDataBean createQuote(String symbol, String companyName) {
    try {
      BigDecimal price = new BigDecimal(randomPrice());

      QuoteDataBean quote = new QuoteDataBean(symbol, companyName, 0, price, price, price, price, 0);
      entityManager.persist(quote);
      if (Log.doTrace()) {
        Log.trace("TradeSLSBBean:createQuote-->" + quote);
      }
      return quote;
    } catch (Exception e) {
      Log.error("TradeSLSBBean:createQuote -- exception creating Quote", e);
      throw new EJBException(e);
    }
  }

  @Override
  public QuoteDataBean getQuote(String symbol) {
    if (Log.doTrace()) {
      Log.trace("TradeSLSBBean:getQuote", symbol);
    }

    return entityManager.find(QuoteDataBean.class, symbol);
  }

  @Override
  public List<QuoteDataBean> getAllQuotes() {
    if (Log.doTrace()) {
      Log.trace("TradeSLSBBean:getAllQuotes");
    }

    TypedQuery<QuoteDataBean> query = entityManager.createNamedQuery("quoteejb.allQuotes", QuoteDataBean.class);
    List<QuoteDataBean> quotes = query.getResultList();
    return quotes;
  }

  @Override
  public QuoteDataBean updateQuotePriceVolume(String symbol, double sharesTraded) {

    if (Log.doTrace()) {
      Log.trace("TradeSLSBBean:updateQuote", symbol, sharesTraded);
    }

    TypedQuery<QuoteDataBean> q = entityManager.createNamedQuery("quoteejb.quoteForUpdate", QuoteDataBean.class);
    q.setParameter(1, symbol);
    QuoteDataBean quote = q.getSingleResult();

    BigDecimal oldPrice = quote.getPrice();
    BigDecimal openPrice = quote.getOpen();

    BigDecimal changeFactor = getRandomPriceChangeFactor();

    if (oldPrice.equals(pennyStockPrice)) {
      changeFactor = pennyStockRevoveryMiracleMultiplier;
    } else if (oldPrice.compareTo(maximumStockPrice) > 0) {
      changeFactor = maximumStockSplitMultiplier;
    }

    BigDecimal newPrice = changeFactor.multiply(oldPrice).setScale(2, BigDecimal.ROUND_HALF_UP);

    quote.setPrice(newPrice);
    quote.setChange(newPrice.subtract(openPrice).doubleValue());
    quote.setVolume(quote.getVolume() + sharesTraded);
    entityManager.merge(quote);

    /*
     * send to mksummary publishQuotePriceChange(quote, oldPrice, changeFactor,
     * sharesTraded); } recentQuotePriceChangeList.add(quote);
     */

    return quote;

  }

  class QuotePriceComparator implements Comparator<Object> {

    @Override
    public int compare(Object quote1, Object quote2) {
      double change1 = ((QuoteDataBean) quote1).getChange();
      double change2 = ((QuoteDataBean) quote2).getChange();
      return new Double(change2).compareTo(change1);
    }

  }

  @PostConstruct
  public void postConstruct() {
    pennyStockPrice = new BigDecimal(0.01);
    pennyStockPrice = pennyStockPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
    pennyStockRevoveryMiracleMultiplier = new BigDecimal(600.0);
    pennyStockRevoveryMiracleMultiplier.setScale(2, BigDecimal.ROUND_HALF_UP);
    maximumStockPrice = new BigDecimal(400);
    maximumStockPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
    maximumStockSplitMultiplier = new BigDecimal(0.5);
    maximumStockSplitMultiplier.setScale(2, BigDecimal.ROUND_HALF_UP);
  }

  private BigDecimal getRandomPriceChangeFactor() {
    // CJB (DAYTRADER-25) - Vary change factor between 1.1 and 0.9
    double percentGain = randomFloat(1) * 0.1;
    if (random() < .5) {
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
        	// Find Trade Stock Index Quotes (Top 100 quotes) ordered by their change in value
        	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        	CriteriaQuery<QuoteDataBean> criteriaQuery = criteriaBuilder.createQuery(QuoteDataBean.class);
        	Root<QuoteDataBean> quoteRoot = criteriaQuery.from(QuoteDataBean.class);
        	criteriaQuery.orderBy(criteriaBuilder.desc(quoteRoot.get("change1")));
        	criteriaQuery.select(quoteRoot);
        	TypedQuery<QuoteDataBean> q = entityManager.createQuery(criteriaQuery);
        	quotes = q.getResultList();
        } catch (Exception e) {
        	Log.debug("Warning: The database has not been configured. If this is the first time the application has been started, please create and populate the database tables. Then restart the server.");
        	return null;
        }	
                
        /* TODO: Make this cleaner? */
        QuoteDataBean[] quoteArray = quotes.toArray(new QuoteDataBean[quotes.size()]);
        ArrayList<QuoteDataBean> topGainers = new ArrayList<QuoteDataBean>(5);
        ArrayList<QuoteDataBean> topLosers = new ArrayList<QuoteDataBean>(5);
        BigDecimal TSIA = ZERO;
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
                TSIA = TSIA.add(price);
                openTSIA = openTSIA.add(open);
                totalVolume += volume;
            }
            TSIA = TSIA.divide(new BigDecimal(quoteArray.length), ROUND);
            openTSIA = openTSIA.divide(new BigDecimal(quoteArray.length), ROUND);
        }
        
        return new MarketSummaryDataBean(TSIA, openTSIA, totalVolume, topGainers, topLosers);
  }
}