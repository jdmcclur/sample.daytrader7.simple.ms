/* (C) Copyright IBM Corporation 2015.
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
import com.ibm.websphere.samples.daytrader.interfaces.QuoteService;
import com.ibm.websphere.samples.daytrader.util.Log;


import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class MarketSummaryEJB {

 @Inject
 Log Log;

 @Inject QuoteService quoteService;
 
 private MarketSummaryDataBean marketSummaryDataBean; 

 @PostConstruct
 private void setup() {
   updateMarketSummary();
 }

 /* Update Market Summary every 20 seconds */
 @Schedule(second = "*/20", minute = "*", hour = "*", persistent = false)
 private void updateMarketSummary() {

   if (Log.doTrace()) {
     Log.trace("MarketSummarySingleton:updateMarketSummary -- updating market summary");
   }

   try {    
     setMarketSummaryDataBean(quoteService.getMarketSummary());
   } catch (Exception e) {
     e.printStackTrace();
   }
 }

 @Lock(LockType.READ)
 public MarketSummaryDataBean getMarketSummaryDataBean() {
   return marketSummaryDataBean;
 }

 @Lock(LockType.WRITE)
 public void setMarketSummaryDataBean(MarketSummaryDataBean marketSummaryDataBean) {
   this.marketSummaryDataBean = marketSummaryDataBean;
 }

}class r {
  
}
