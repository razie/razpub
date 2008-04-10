/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import com.razie.pub.hframe.base.data.HttpUtils;

/**
 * simple attribute access interface and implementation - a bunch of name-value pairs with many
 * different constructors - everything these days has attributes.
 * 
 * it is used throughout to access parms
 * 
 * $
 * @author razvanc99
 * 
 */
public interface AttrAccess {
    /** @return the value of the named attribute */
    public Object getAttr(String name);

    /** set the value of the named attribute */
    public void setAttr(String name, Object value);

    /**
     * @parm pairs are pais of name/value, i.e. "car", "lexus" OR a Properties, OR another
     *       AttrAccess OR a Map<String,String>
     */
    public void setAttr(Object... pairs);

    /** the number of populated attributes */
    public int size();

    /** iterate through the populated attributes */
    public Iterable<String> getPopulatedAttr();

    /** check if an attribute is populated */
    public boolean isPopulated(String name);

    /** some random xml format */
    public String toXml();

    public JSONObject toJson(JSONObject obj);

    /**
     * add these attributes to an url, respecting the url parm format, i.e.
     * getMovie?name=300.divx&producer=whoknows
     */
    public String addToUrl(String url);

    /** simple base implementation */
    public class Impl implements AttrAccess {
        // lazy
        protected Map<String, Object> parms = null;

        /** dummy */
        public Impl() {
        };

        /**
         * build from sequence of parm/value pairs or other stuff
         * 
         * @parm pairs are pais of name/value, i.e. "car", "lexus" OR a Properties, OR another
         *       AttrAccess OR a Map<String,String>
         */
        public Impl(Object... pairs) {
            this.setAttr(pairs);
        }

        public void setAttr(String name, Object value) {
            checkMap();
            this.parms.put(name, value);
        }

        public Object getAttr(String name) {
            return this.parms != null ? this.parms.get(name) : null;
        }

        /**
         * @parm pairs are pais of name/value, i.e. setAttr("car", "lexus") OR a Properties, OR
         *       another AttrAccess OR a Map<String,String>
         */
        public void setAttr(Object... pairs) {
            if (pairs != null && pairs.length == 1 && pairs[0] instanceof Map) {
                Map<String, String> m = (Map<String, String>) pairs[0];
                for (String s : m.keySet()) {
                    this.setAttr(s, m.get(s));
                }
            } else if (pairs != null && pairs.length == 1 && pairs[0] instanceof Properties) {
                Properties m = (Properties) pairs[0];
                for (Object s : m.keySet()) {
                    this.setAttr((String) s, m.get((String) s));
                }
            } else if (pairs != null && pairs.length == 1 && pairs[0] instanceof AttrAccess) {
                AttrAccess m = (AttrAccess) pairs[0];
                for (String s : m.getPopulatedAttr()) {
                    this.setAttr((String) s, m.getAttr((String) s));
                }
            } else if (pairs != null && pairs.length > 1) {
                for (int i = 0; i < pairs.length / 2; i++) {
                    String name = (String) pairs[2 * i];
                    this.setAttr(name, pairs[2 * i + 1]);
                }
            }
        }

        private void checkMap() {
            if (this.parms == null) {
                this.parms = new HashMap<String, Object>();
            }
        }

        public Iterable<String> getPopulatedAttr() {
            return this.parms == null ? Collections.EMPTY_LIST : this.parms.keySet();
        }

        public int size() {
            return this.parms == null ? 0 : this.parms.size();
        }

        public boolean isPopulated(String name) {
            return this.parms != null && this.parms.containsKey(name);
        }

        public JSONObject toJson(JSONObject obj) {
            try {
                if (obj == null)
                    obj = new JSONObject();
                for (String name : this.getPopulatedAttr()) {
                    obj.put(name, this.getAttr(name));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return obj;
        }

        /** TODO implement */
        public static AttrAccess fromJson() {
            // TODO
            AttrAccess a = new Impl();
            return a;
        }

        public String toXml() {
            String s = "";
            for (String name : this.getPopulatedAttr()) {
                s += "<" + name + ">" + this.getAttr(name) + "</" + name + ">";
            }
            return s;
        }

        /** TODO implement */
        public final static AttrAccess reflect(Object o) {
            // TODO
            throw new UnsupportedOperationException("TODO");
        }

        public String addToUrl(String url) {
            String newurl = url;
            for (String a : getPopulatedAttr()) {
                if (!newurl.endsWith("?") && !newurl.endsWith("&")) {
                    newurl += newurl.contains("=") ? "&" : "?";
                }
                newurl += HttpUtils.toUrlEncodedString(a) + "="
                        + HttpUtils.toUrlEncodedString(getAttr(a).toString());
            }
            return newurl;
        }
    }

}