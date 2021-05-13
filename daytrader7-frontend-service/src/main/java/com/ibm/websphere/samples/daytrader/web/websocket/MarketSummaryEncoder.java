/**
 * (C) Copyright IBM Corporation 2019.
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
package com.ibm.websphere.samples.daytrader.web.websocket;

import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.ibm.websphere.samples.daytrader.beans.MarketSummaryDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;

/**
 * This class takes a list of quotedata (from the RecentQuotePriceChangeList
 * bean) and encodes it to the json format the client (marektsummary.html) is
 * expecting.
 **/
public class MarketSummaryEncoder implements Encoder.Text<MarketSummaryDataBean> {

  private static final JsonBuilderFactory jsonObjectFactory = Json.createBuilderFactory(null);

  public String encode(MarketSummaryDataBean mkSummary) throws EncodeException {

    JsonObjectBuilder jObjectBuilder = jsonObjectFactory.createObjectBuilder();

    int i = 1;
    for (Iterator<QuoteDataBean> iterator = mkSummary.getTopGainers().iterator(); iterator.hasNext();) {
      QuoteDataBean quote = iterator.next();

      jObjectBuilder.add("gainer" + i + "_stock", quote.getSymbol());
      jObjectBuilder.add("gainer" + i + "_price", "$" + quote.getPrice());
      jObjectBuilder.add("gainer" + i + "_change", quote.getChange());
      i++;
    }

    i = 1;
    for (Iterator<QuoteDataBean> iterator = mkSummary.getTopLosers().iterator(); iterator.hasNext();) {
      QuoteDataBean quote = iterator.next();

      jObjectBuilder.add("loser" + i + "_stock", quote.getSymbol());
      jObjectBuilder.add("loser" + i + "_price", "$" + quote.getPrice());
      jObjectBuilder.add("loser" + i + "_change", quote.getChange());
      i++;
    }

    jObjectBuilder.add("tsia", mkSummary.getTsia());
    jObjectBuilder.add("volume", mkSummary.getVolume());
    jObjectBuilder.add("date", mkSummary.getSummaryDate().toString());

    return jObjectBuilder.build().toString();
  }

  @Override
  public void init(EndpointConfig config) {
  }

  @Override
  public void destroy() {
  }

}