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

package com.ibm.websphere.samples.daytrader.rest;

import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;

import io.reactivex.rxjava3.core.Flowable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.apache.kafka.common.serialization.Serializer;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.reactivestreams.Publisher;

@ApplicationScoped
public class RecentQuotePriceChangeList {

  private static final Jsonb jsonb = JsonbBuilder.create();

  private List<QuoteDataBean> list = new CopyOnWriteArrayList<QuoteDataBean>();
  private int maxSize = 5;

  public boolean add(QuoteDataBean quoteData) {

    // Do this 10% of the time?
    if (ThreadLocalRandom.current().nextInt(1, 11) < 11) {
      list.add(0, quoteData);

      // Add stock, remove if needed
      if (list.size() > maxSize) {
        list.remove(maxSize);
      }
    }
    return true;
  }

  public boolean isEmpty() {
    return list.isEmpty();
  }

  public List<QuoteDataBean> getList() {
    return list;
  }

  @Outgoing("recentquotepricechange")
  public Publisher<List<QuoteDataBean>> sendQuoteChanges() {
    return Flowable.interval(5, TimeUnit.SECONDS).map((interval -> getList()));
  }

  // tag::jsonbSerializer[]
  public static class QuoteChangeSerializer implements Serializer<Object> {
    @Override
    public byte[] serialize(String topic, Object data) {
      return jsonb.toJson(data).getBytes();
    }
  }
}
