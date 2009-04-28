/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.base.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * simple in-memory database: each K has a list of V
 * 
 * @author razvanc99
 * 
 * @param <K>
 * @param <V>
 */
public class MemDb<K, V> {
    Map<K, List<V>> db = new HashMap<K, List<V>>();

    public void put(K k, V v) {
        List<V> l = db.get(k);
        if (l == null) {
            l = new ArrayList<V>();
            db.put(k, l);
        }
        l.add(v);
    }

    /** @return the list for K or an empty list - never null */
    public List<V> get(K k) {
        List<V> l = db.get(k);
        if (l == null) {
            l = Collections.EMPTY_LIST;
        }
        return l;
    }
}
