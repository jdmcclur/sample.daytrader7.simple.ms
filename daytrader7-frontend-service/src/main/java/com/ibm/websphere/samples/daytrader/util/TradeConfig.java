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

package com.ibm.websphere.samples.daytrader.util;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * TradeConfig is a JavaBean holding all configuration and runtime parameters
 * for the Trade application TradeConfig sets runtime parameters such as the
 * RunTimeMode (EJB, JDBC, EJB_ALT).
 *
 */
@ApplicationScoped
@Named(value = "config")
public class TradeConfig {

  @Inject 
  Log Log;

  /* Trade Database Scaling parameters */
  @Inject @ConfigProperty(name = "MAX_USERS", defaultValue = "15000")
  private int maxUsers;

  @Inject @ConfigProperty(name = "MAX_QUOTES", defaultValue = "10000")
  private int maxQuotes;

  @Inject @ConfigProperty(name = "MAX_HOLDINGS", defaultValue = "10")
  private int maxHoldings;
 
  @Inject @ConfigProperty(name = "DISPLAY_ORDER_ALERTS", defaultValue = "true")
  private boolean displayOrderAlerts = true;

    
  public int getMaxUsers() {
    return maxUsers;
  }

  public void setMaxUsers(int maxUsers) {
    this.maxUsers = maxUsers;
  }

  public int getMaxQuotes() {
    return maxQuotes;
  }

  public void setMaxQuotes(int maxQuotes) {
    this.maxQuotes = maxQuotes;
  }

  public int getMaxHoldings() {
    return maxHoldings;
  }

  public void setMaxHoldings(int maxHoldings) {
    this.maxHoldings = maxHoldings;
  }
  
  public void setDisplayOrderAlerts(boolean value) {
    displayOrderAlerts = value;
  }

  public boolean getDisplayOrderAlerts() {
    return displayOrderAlerts;
  }
}
