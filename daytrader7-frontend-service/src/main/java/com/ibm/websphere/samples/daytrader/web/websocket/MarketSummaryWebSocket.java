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

package com.ibm.websphere.samples.daytrader.web.websocket;

import com.ibm.websphere.samples.daytrader.beans.MarketSummaryDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.interfaces.TradeService;
import com.ibm.websphere.samples.daytrader.util.Log;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.kafka.common.serialization.Deserializer;
import org.eclipse.microprofile.reactive.messaging.Incoming;

/**
 * This class is a WebSocket EndPoint that sends the Market Summary in JSON form
 * when requested and sends stock price changes when received from an MDB
 * through a CDI event.
 */

@ServerEndpoint(value = "/marketsummary", encoders = { MarketSummaryEncoder.class,
    QuotePriceChangeListEncoder.class }, decoders = ActionDecoder.class)
public class MarketSummaryWebSocket {

  private static final Jsonb jsonb = JsonbBuilder.create();

  @Inject
  Log logService;

  @Inject
  TradeService tradeService;

  private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
  private final CountDownLatch latch = new CountDownLatch(1);

  @OnOpen
  public void onOpen(final Session session, EndpointConfig ec) {
    if (logService.doTrace()) {
      logService.trace("MarketSummaryWebSocket:onOpen -- session -->" + session + "<--");
    }

    sessions.add(session);
    latch.countDown();
  }

  @OnMessage
  public void sendMarketSummary(ActionMessage message, Session currentSession) {

    String action = message.getDecodedAction();

    if (logService.doTrace()) {
      if (action != null) {
        logService.trace("MarketSummaryWebSocket:sendMarketSummary -- received -->" + action + "<--");
      } else {
        logService.trace("MarketSummaryWebSocket:sendMarketSummary -- received -->null<--");
      }
    }

    if (action != null && action.equals("update")) {

      try {

        MarketSummaryDataBean marketSummary = tradeService.getMarketSummary();

        if (logService.doTrace()) {
          logService.trace("MarketSummaryWebSocket:sendMarketSummary -- sending -->" + marketSummary + "<--");
        }

        // Make sure onopen is finished
        latch.await();

        currentSession.getAsyncRemote().sendObject(marketSummary);

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @OnError
  public void onError(Throwable t, Session currentSession) {
    if (logService.doTrace()) {
      logService.trace("MarketSummaryWebSocket:onError -- session -->" + currentSession + "<--");
    }
    t.printStackTrace();
  }

  @OnClose
  public void onClose(Session session, CloseReason reason) {

    if (logService.doTrace()) {
      logService.trace("MarketSummaryWebSocket:onClose -- session -->" + session + "<--");
    }

    sessions.remove(session);

  }

  @Incoming("recentquotepricechange")
  public void receiveQuoteChanges(List<QuoteDataBean> recentQuotePriceChanges) {
    Iterator<Session> failSafeIterator = sessions.iterator();
    while (failSafeIterator.hasNext()) {
      Session s = failSafeIterator.next();
      if (s.isOpen()) {
        s.getAsyncRemote().sendObject(recentQuotePriceChanges);
      }
    }
  }

  // end::jsonbSerializer[]// tag::jsonbDeSerializer[]
  public static class QuoteChangeDeserializer implements Deserializer<CopyOnWriteArrayList<QuoteDataBean>> {
    @Override
    public CopyOnWriteArrayList<QuoteDataBean> deserialize(String topic, byte[] data) {
      if (data == null) {
        return null;
      }

      return jsonb.fromJson(new String(data), new CopyOnWriteArrayList<QuoteDataBean>() {
      }.getClass().getGenericSuperclass());
    }
  }
}
