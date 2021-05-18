/*******************************************************************************
 * Copyright (c) 2020, 2021 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package com.ibm.websphere.samples.daytrader.util;

import java.util.Collections;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

public class ClosedOrdersCache {
  protected int entryLimit = 15000;
  protected long timeoutInMilliSeconds = 5 * 60 * 1000;

  protected Timer timer;
  protected Map<String, Object> lookupTable;


  public ClosedOrdersCache(long timeoutInMilliSeconds) {
    this(0, timeoutInMilliSeconds);
  }

  public ClosedOrdersCache(int entryLimit, long timeoutInMilliSeconds) {
    if (entryLimit > 0) {
      this.entryLimit = entryLimit;
    }
    lookupTable = Collections.synchronizedMap(new BoundedHashMap(this.entryLimit));

    if (timeoutInMilliSeconds > 0) {
      this.timeoutInMilliSeconds = timeoutInMilliSeconds;
    }

    scheduleEvictionTask(this.timeoutInMilliSeconds);
  }

  /**
   * Remove an object from the Cache.
   */
  public synchronized void remove(Object key) {
    lookupTable.remove(key);
  }

  /**
   * Find and return the object associated with the specified key.
   */
  public synchronized Object get(String key) {
    return lookupTable.get(key);
  }

  /**
   * Insert the value into the Cache using the specified key.
   */
  public synchronized void put(String key, Object value) {
    lookupTable.put(key, value);
  }

  /**
   * Implementation of the eviction strategy.
   */
  protected synchronized void evictStaleEntries() {
    // TODO - change this
    lookupTable = Collections.synchronizedMap(new BoundedHashMap(this.entryLimit));
  }

  protected synchronized void printEntries() {
    for (Entry<String, Object> entry : lookupTable.entrySet()) {
      System.out.print(entry.getKey() + " = " + entry.getValue());
    }
  }

  /**
   * Implementation of the eviction strategy.
   */
  public synchronized void rescheduleCleanup(long newTimeoutInMillis) {
    if (newTimeoutInMillis > 0) {
      this.timeoutInMilliSeconds = newTimeoutInMillis;
    }
    timer.cancel();
    scheduleEvictionTask(timeoutInMilliSeconds);
  }

  protected void scheduleEvictionTask(long timeoutInMilliSeconds) {
    EvictionTask evictionTask = new EvictionTask();
    timer = new Timer(true);
    long period = timeoutInMilliSeconds;
    long delay = period;
    timer.schedule(evictionTask, delay, period);
  }

  private class EvictionTask extends TimerTask {
    /** {@inheritDoc} */
    @Override
    public void run() {
      evictStaleEntries();
    }

  }

}
