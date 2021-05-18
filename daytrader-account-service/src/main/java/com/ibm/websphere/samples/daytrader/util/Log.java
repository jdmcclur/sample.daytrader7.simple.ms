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

package com.ibm.websphere.samples.daytrader.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Log {

  private final Logger log = Logger.getLogger("daytrader");

  @Inject @ConfigProperty(name = "TRACE", defaultValue = "false")
  private Boolean trace;

  @Inject @ConfigProperty(name = "ACTION_TRACE", defaultValue = "false")
  private Boolean actionTrace;

  // A general purpose, high performance logging, tracing, statistic service

  public void log(String message) {
    log.log(Level.INFO, message);
  }

  public void log(String msg1, String msg2) {
    log(msg1 + msg2);
  }

  public void log(String msg1, String msg2, String msg3) {
    log(msg1 + msg2 + msg3);
  }

  public void error(String message) {
    message = "Error: " + message;
    log.severe(message);
  }

  public void error(String message, Throwable e) {
    error(message + "\n\t" + e.toString());
    e.printStackTrace(System.out);
  }

  public void error(String msg1, String msg2, Throwable e) {
    error(msg1 + "\n" + msg2 + "\n\t", e);
  }

  public void error(String msg1, String msg2, String msg3, Throwable e) {
    error(msg1 + "\n" + msg2 + "\n" + msg3 + "\n\t", e);
  }

  public void error(Throwable e, String message) {
    error(message + "\n\t", e);
    e.printStackTrace(System.out);
  }

  public void error(Throwable e, String msg1, String msg2) {
    error(msg1 + "\n" + msg2 + "\n\t", e);
  }

  public void error(Throwable e, String msg1, String msg2, String msg3) {
    error(msg1 + "\n" + msg2 + "\n" + msg3 + "\n\t", e);
  }

  public void trace(String message) {
    log.log(Level.FINE, message + " threadID=" + Thread.currentThread());
  }

  public void trace(String message, Object parm1) {
    trace(message + "(" + parm1 + ")");
  }

  public void trace(String message, Object parm1, Object parm2) {
    trace(message + "(" + parm1 + ", " + parm2 + ")");
  }

  public void trace(String message, Object parm1, Object parm2, Object parm3) {
    trace(message + "(" + parm1 + ", " + parm2 + ", " + parm3 + ")");
  }

  public void trace(String message, Object parm1, Object parm2, Object parm3, Object parm4) {
    trace(message + "(" + parm1 + ", " + parm2 + ", " + parm3 + ")" + ", " + parm4);
  }

  public void trace(String message, Object parm1, Object parm2, Object parm3, Object parm4, Object parm5) {
    trace(message + "(" + parm1 + ", " + parm2 + ", " + parm3 + ")" + ", " + parm4 + ", " + parm5);
  }

  public void trace(String message, Object parm1, Object parm2, Object parm3, Object parm4, Object parm5,
      Object parm6) {
    trace(message + "(" + parm1 + ", " + parm2 + ", " + parm3 + ")" + ", " + parm4 + ", " + parm5 + ", " + parm6);
  }

  public void trace(String message, Object parm1, Object parm2, Object parm3, Object parm4, Object parm5, Object parm6,
      Object parm7) {
    trace(message + "(" + parm1 + ", " + parm2 + ", " + parm3 + ")" + ", " + parm4 + ", " + parm5 + ", " + parm6 + ", "
        + parm7);
  }

  public void traceEnter(String message) {
    log.log(Level.FINE, "Method enter --" + message);
  }

  public void traceExit(String message) {
    log.log(Level.FINE, "Method exit  --" + message);
  }

  public void stat(String message) {
    log(message);
  }

  public void debug(String message) {
    log.log(Level.INFO, message);
  }

  public void print(String message) {
    log(message);
  }

  public void printObject(Object o) {
    log("\t" + o.toString());
  }

  public void printCollection(Collection<?> c) {
    log("\t---Log.printCollection -- collection size=" + c.size());
    Iterator<?> it = c.iterator();

    while (it.hasNext()) {
      log("\t\t" + it.next().toString());
    }
    log("\t---Log.printCollection -- complete");
  }

  public void printCollection(String message, Collection<?> c) {
    log(message);
    printCollection(c);
  }

  public boolean doActionTrace() {
    return getTrace() || getActionTrace();
  }

  public boolean doTrace() {
    return getTrace();
  }

  public boolean doDebug() {
    return true;
  }

  public boolean doStat() {
    return true;
  }

  /**
   * Gets the trace.
   *
   * @return Returns a boolean
   */
  public boolean getTrace() {
    return trace;
  }

  /**
   * Gets the trace value for Trade actions only.
   *
   * @return Returns a boolean
   */
  public boolean getActionTrace() {
    return actionTrace;
  }

  /**
   * Sets the trace.
   *
   * @param traceValue The trace to set
   */
  public void setTrace(boolean traceValue) {
    trace = traceValue;
  }

  /**
   * Sets the trace value for Trade actions only.
   *
   * @param traceValue The trace to set
   */
  public void setActionTrace(boolean traceValue) {
    actionTrace = traceValue;
  }
}
