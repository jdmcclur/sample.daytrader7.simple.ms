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

package com.ibm.websphere.samples.daytrader.interfaces;

import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;

import java.math.BigDecimal;
import java.util.List;

public interface HoldingService {

  
  /**
   * Return the portfolio of stock holdings for the specified customer as a
   * collection of HoldingDataBeans.
   *
   * @param userID the customer requesting the portfolio
   * @return Collection of the users portfolio of stock holdings
   */
 List<HoldingDataBean> getHoldings(String userID) throws Exception;

  /**
   * Return a specific user stock holding identifed by the holdingID.
   *
   * @param holdingID the holdingID to return
   * @return a HoldingDataBean describing the holding
   */
  HoldingDataBean getHolding(Integer holdingID) throws Exception;

  HoldingDataBean createHolding(String accountId, String quoteSymbol, double quantity, BigDecimal purchasePrice) throws Exception;

  void removeHolding(Integer holdingId) throws Exception;
}
