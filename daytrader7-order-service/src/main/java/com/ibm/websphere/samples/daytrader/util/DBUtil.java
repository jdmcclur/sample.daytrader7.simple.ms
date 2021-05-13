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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * TradeBuildDB uses operations provided by the TradeApplication to (a) create
 * the Database tables (b)populate a DayTrader database without creating the
 * tables. Specifically, a new DayTrader User population is created using
 * UserIDs of the form "uid:xxx" where xxx is a sequential number (e.g. uid:0,
 * uid:1, etc.). New stocks are also created of the form "s:xxx", again where
 * xxx represents sequential numbers (e.g. s:1, s:2, etc.)
 */
@ApplicationScoped
public class DBUtil {

  @Resource(lookup = "jdbc/TradeDataSource")
  private DataSource datasource;
  
  @Inject
  Log Log;
  
  /**
   * Re-create the DayTrader db tables and populate them OR just populate a
   * DayTrader DB, logging to the provided output stream.
   **/
  public void createDB() throws Exception {
              
    // TradeStatistics.statisticsEnabled=false; // disable statistics
    System.out.println("DBUtil: Building DayTrader Order Database... This operation will take several minutes. Please wait...");
    String dbProductName = null;
    String ddlFile = null;
    try {
      dbProductName = checkDBProductName();
    } catch (Exception e) {
      Log.error(e, "TradeBuildDB: Unable to check DB Product name");
      throw(e);
    }
    if (dbProductName.startsWith("DB2/")) {// if db is DB2
      ddlFile = "/dbscripts/db2/Table.ddl";
    } else if (dbProductName.startsWith("DB2 UDB for AS/400")) { // if db is DB2 on IBM i
      ddlFile = "/dbscripts/db2i/Table.ddl";
    } else if (dbProductName.startsWith("Apache Derby")) { // if db is Derby
      ddlFile = "/dbscripts/derby/Table.ddl";
    } else if (dbProductName.startsWith("Oracle")) { // if the Db is Oracle
      ddlFile = "/dbscripts/oracle/Table.ddl";
    } else {// Unsupported "Other" Database, try derby ddl
      ddlFile = "/dbscripts/derby/Table.ddl";

      System.out.println("TradeBuildDB: **** This Database is unsupported/untested use at your own risk ****");
    }

    if (ddlFile != null) {

      boolean success = false;

      Object[] sqlBuffer = null;

      // parse the DDL file and fill the SQL commands into a buffer
      try {
        sqlBuffer = parseDDLToBuffer(ddlFile);
      } catch (Exception e) {
        Log.error(e, "TradeBuildDB: Unable to parse DDL file");
        System.out.println("<BR>TradeBuildDB: **** Unable to parse DDL file for the specified database ****</BR></BODY>");
        return;
      }
      if ((sqlBuffer == null) || (sqlBuffer.length == 0)) {
        System.out.println(
            "<BR>TradeBuildDB: **** Parsing DDL file returned empty buffer, please check that a valid DB specific DDL file is available "
                + "and retry ****</BR></BODY>");
        return;
      }

      // send the sql commands buffer to drop and recreate the Daytrader tables
      System.out.println("DBUtil: **** Dropping and Recreating the Order tables... ****");
      try {
        success = recreateDBTables(sqlBuffer);
      } catch (Exception e) {
        Log.error(e,
            "DButil: Unable to drop and recreate DayTrader Db Tables, please check for database consistency before continuing");
        System.out.println(
            "DBUtil: Unable to drop and recreate DayTrader Db Tables, please check for database consistency before continuing");
        return;
      }
      if (!success) {
        System.out.println(
            "DBUtil: **** Unable to drop and recreate DayTrader Db Order Tables, please check for database "
                + "consistency before continuing ****");
        return;
      }
      System.out.println(
          "DBUtil: **** DayTrader order table successfully created! ****< Please use the \"Repopulate Daytrader Database\" link to populate your database.");
      return;
    } // end of createDBTables
  }

  private Object[] parseDDLToBuffer(String ddlFile) throws Exception {
    BufferedReader br = null;
    // initial capacity 30 assuming we have 30 ddl-sql statements to read
    ArrayList<String> sqlBuffer = new ArrayList<String>(30);

    try {
      if (Log.doTrace()) {
        Log.traceEnter("TradeBuildDB:parseDDLToBuffer - " + ddlFile);
      }

      br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(ddlFile)));
      String s;
      String sql = new String();
      while ((s = br.readLine()) != null) {
        s = s.trim();
        if ((s.length() != 0) && (s.charAt(0) != '#')) { // Empty lines or lines starting with "#" are ignored
          sql = sql + " " + s;
          if (s.endsWith(";")) { // reached end of sql statement
            sql = sql.replace(';', ' '); // remove the semicolon
            sqlBuffer.add(sql);
            sql = "";
          }
        }
      }
    } catch (IOException ex) {
      Log.error("DBUtil:parseDDLToBuffer Exeception during open/read of File: " + ddlFile, ex);
      throw ex;
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException ex) {
          Log.error("DBUtil:parseDDLToBuffer Failed to close BufferedReader", ex);
        }
      }
    }
    return sqlBuffer.toArray();
  }

  public String checkDBProductName() throws Exception {
    Connection conn = null;
    String dbProductName = null;

    try {
      if (Log.doTrace()) {
        Log.traceEnter("DBUtil:checkDBProductName");
      }

      conn = datasource.getConnection();
      DatabaseMetaData dbmd = conn.getMetaData();
      dbProductName = dbmd.getDatabaseProductName();
    } catch (SQLException e) {
      Log.error(e, "DBUtil:checkDBProductName() -- Error checking the Daytrader Database Product Name");
    } finally {
      conn.close();
    }
    return dbProductName;
  }

  private boolean recreateDBTables(Object[] sqlBuffer) throws Exception {
    // Clear MDB Statistics
    // MDBStats.getInstance().reset();

    Connection conn = null;
    boolean success = false;
    try {
      if (Log.doTrace()) {
        Log.traceEnter("DBUtil:recreateDBTables");
      }

      conn = datasource.getConnection();

      Statement stmt = conn.createStatement();
      int bufferLength = sqlBuffer.length;
      for (int i = 0; i < bufferLength; i++) {
        try {
          stmt.executeUpdate((String) sqlBuffer[i]);
          // commit(conn);
        } catch (SQLException ex) {
          // Ignore DROP statements as tables won't always exist.
          if (((String) sqlBuffer[i]).indexOf("DROP TABLE") < 0) {
            Log.error(
                "TradeDirect:recreateDBTables SQL Exception thrown on executing the foll sql command: " + sqlBuffer[i],
                ex);
            System.out.println("SQL Exception thrown on executing the foll sql command: <I>" + sqlBuffer[i]
                + ". Check log for details.");
          }
        }
      }
      stmt.close();
      conn.commit();
      success = true;
    } catch (Exception e) {
      Log.error(e, "DBUtil:recreateDBTables() -- Error dropping and recreating the database tables");
    } finally {
      conn.close();
    }
    return success;
  }

  public void resetTrade(boolean deleteAll) throws Exception {
    // Clear MDB Statistics
    // MDBStats.getInstance().reset();
    // Reset Trade

    // RunStatsDataBean runStatsData = new RunStatsDataBean();
    Connection conn = null;
    try {
      if (Log.doTrace()) {
        Log.traceEnter("TradeDirect:resetTrade deleteAll rows=" + deleteAll);
      }
      conn = datasource.getConnection();
      PreparedStatement stmt = null;

      if (deleteAll) {
        try {
          stmt = getStatement(conn, "delete from orderejb");
          stmt.executeUpdate();
          stmt.close();
          // FUTURE: - DuplicateKeyException - For now, don't start at
          // zero as KeySequenceDirect and KeySequenceBean will still
          // give out
          // the cached Block and then notice this change. Better
          // solution is
          // to signal both classes to drop their cached blocks
          // stmt = getStatement(conn, "delete from keygenejb");
          // stmt.executeUpdate();
          // stmt.close();
          conn.commit();
        } catch (Exception e) {
          Log.error(e,
              "DBUtil:resetTrade(deleteAll) -- Error deleting Trade users and stock from the Trade database");
        }
        return;
      }

      stmt = getStatement(conn, "delete from orderejb where accountId like 'ru:%')");
      stmt.executeUpdate();
      stmt.close();

      // Delete cancelled orders
      stmt = getStatement(conn, "delete from orderejb where orderStatus='cancelled'");
      stmt.close();

      // Delete orders for holding which have been purchased and sold
      stmt = getStatement(conn, "delete from orderejb where holdingid is null");
      stmt.close();

      conn.commit();

      System.out.println("orders reset\n\n");
    } catch (Exception e) {
      Log.error(e, "Failed to reset Trade");
      conn.rollback();
      throw e;
    } finally {
      conn.close();
    }
  }

  private static PreparedStatement getStatement(Connection conn, String sql) throws Exception {
    return conn.prepareStatement(sql);
  }
}