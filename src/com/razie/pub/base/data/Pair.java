/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base.data;

import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * a simple pair of objects - use it as a quick tuple in lists or whatever...
 * 
 * @author razie
 */
@SuppressWarnings("unchecked")
public class Pair<A, B> {
    public A a;
    public B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    /** convert a list of pairs to java - the Pair.a must be String */
    public static JSONObject toJson(List<Pair> list, JSONObject obj) {
        try {
            if (obj == null)
                obj = new JSONObject();
            for (Pair p : list) {
                obj.put((String) p.a, p.b);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    /** convert a list of pairs to java - the Pair.a must be String */
   public static List<Pair> fromJson(List<Pair> list, JSONObject obj) {
        try {
            for (Iterator i = obj.keys();i.hasNext();) {
                String s=(String)i.next();
                list.add(new Pair(s, obj.get(s)));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
