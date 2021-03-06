/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.websphere.samples.daytrader.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public class BoundedHashMap extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = 7306671418293201026L;

    private int MAX_ENTRIES = 10000;

    public BoundedHashMap(int maxEntries) {
        super();
        if (maxEntries > 0) {
            MAX_ENTRIES = maxEntries;
        }
    }

    public BoundedHashMap(int initSize, int maxEntries) {
        super(initSize);
        if (maxEntries > 0) {
            MAX_ENTRIES = maxEntries;
        }
    }

    public BoundedHashMap() {
        super();
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String,Object> eldest) {
        return size() > MAX_ENTRIES;
    }

}