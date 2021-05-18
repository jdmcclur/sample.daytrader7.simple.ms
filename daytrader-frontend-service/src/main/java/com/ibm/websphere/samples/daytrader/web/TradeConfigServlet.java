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

import com.ibm.websphere.samples.daytrader.util.DBLoader;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TradeConfigServlet provides a servlet interface to adjust DayTrader runtime
 * parameters. TradeConfigServlet updates values in the
 * {@link com.ibm.websphere.samples.daytrader.web.TradeConfig} JavaBean holding
 * all configuration and runtime parameters for the Trade application
 *
 */
@WebServlet(name = "TradeConfigServlet", urlPatterns = { "/config" })
public class TradeConfigServlet extends HttpServlet {

  private static final long serialVersionUID = -1910381529792500095L;

  private static final String CONFIG_PAGE = "/config.jsp";
  private static final String STATS_PAGE = "/runStats.jsp";

  @Inject
  Log logService;

  @Inject
  TradeConfig configService;

  @Inject DBLoader dbLoader;

  /**
   * Servlet initialization method.
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  /**
   * Create the TradeConfig bean and pass it the config.jsp page to display the
   * current Trade runtime configuration Creation date: (2/8/2000 3:43:59 PM)
   */
  void doConfigDisplay(HttpServletRequest req, HttpServletResponse resp, String results) throws Exception {
    

    //req.setAttribute("tradeConfig", currentConfig);
    req.setAttribute("status", results);
    getServletConfig().getServletContext().getRequestDispatcher(CONFIG_PAGE)
        .include(req, resp);
  }

  void doResetTrade(HttpServletRequest req, HttpServletResponse resp, String results) throws Exception {
    TradeConfig currentConfig = new TradeConfig();
    try {
      //@TODO
      //runStatsData = TradeDbUtils.resetTrade(false);

      //req.setAttribute("runStatsData", runStatsData);
      req.setAttribute("tradeConfig", currentConfig);
      results += "Trade Reset completed successfully";
      req.setAttribute("status", results);

    } catch (Exception e) {
      results += "Trade Reset Error  - see log for details";
      logService.error(e, results);
      throw e;
    }
    getServletConfig().getServletContext().getRequestDispatcher(STATS_PAGE)
        .include(req, resp);

  }
 

  @Override
  public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String action = null;
    String result = "";

    resp.setContentType("text/html");
    try {
      action = req.getParameter("action");
      if (action == null) {
        doConfigDisplay(req, resp, result + "<b><br>Current DayTrader Configuration:</br></b>");
        return;
      } else if (action.equals("resetTrade")) {
        dbLoader.resetTrade(false);
        result = "DayTrader Databases Reset - ";
        doConfigDisplay(req, resp, result + "Current DayTrader Configuration:");
        return;
      } else if (action.equals("buildDB")) {
        dbLoader.populateDB(resp.getWriter());
        result = "DayTrader Database Built - " + configService.getMaxUsers() + "users created";
      } else if (action.equals("buildDBTables")) {
        dbLoader.createDB();
        result = "DayTrader Databases Created - ";
      }
      doConfigDisplay(req, resp, result + "Current DayTrader Configuration:");
    } catch (Exception e) {
      logService.error(e, "TradeConfigServlet.service(...)", "Exception trying to perform action=" + action);

      resp.sendError(500, "TradeConfigServlet.service(...)" + "Exception trying to perform action=" + action
          + "\nException details: " + e.toString());

    }
  }
}
