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

package com.ibm.websphere.samples.daytrader.web;

import com.ibm.websphere.samples.daytrader.interfaces.TradeService;
import com.ibm.websphere.samples.daytrader.util.ClosedOrdersCache;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collection;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(filterName = "OrdersAlertFilter", urlPatterns = "/app")
public class OrdersAlertFilter implements Filter {

  @Inject
  TradeService tradeService;

  @Inject
  Log logService;

  @Inject
  TradeConfig configService;

  private static ClosedOrdersCache closedOrderCache = new ClosedOrdersCache(1000 * 60 * 5);
  private static Long updateTimeout = new Long(1000 * 10);

  /**
   * Constructor for CompletedOrdersAlertFilter.
   */
  public OrdersAlertFilter() {
    super();
  }

  /**
   * see Filter#init(FilterConfig).
   */
  private FilterConfig filterConfig = null;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    this.filterConfig = filterConfig;
  }

  /**
   * see Filter#doFilter(ServletRequest, ServletResponse, FilterChain).
   */
  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
      throws IOException, ServletException {
    if (filterConfig == null) {
      return;
    }

    if (configService.getDisplayOrderAlerts() == true) {

      try {
        String action = req.getParameter("action");
        if (action != null) {
          action = action.trim();
          if ((action.length() > 0) && (!action.equals("logout"))) {
            String userID;
            if (action.equals("login")) {
              userID = req.getParameter("uid");
            } else {
              userID = (String) ((HttpServletRequest) req).getSession().getAttribute("uidBean");
            }

            if ((userID != null) && (userID.trim().length() > 0)) {

              Long lastUpdate = (Long) closedOrderCache.get(userID);
              Long currentTime = ZonedDateTime.now().toInstant().toEpochMilli();
              if (lastUpdate == null || currentTime - lastUpdate > updateTimeout) {
                Collection<?> closedOrders = tradeService.getClosedOrders(userID);
                if ((closedOrders != null) && (closedOrders.size() > 0)) {
                  req.setAttribute("closedOrders", closedOrders);
                }
                if (logService.doTrace()) {
                  logService.printCollection("OrderAlertFilter: userID=" + userID + " closedOrders=", closedOrders);
                }
                closedOrderCache.put(userID, currentTime);
              }
            }
          }
        }
      } catch (Exception e) {
        logService.error(e, "OrdersAlertFilter - Error checking for closedOrders");
      }
    }

    chain.doFilter(req, resp/* wrapper */);
  }

  /**
   * see Filter#destroy().
   */
  @Override
  public void destroy() {
    this.filterConfig = null;
  }

}
