/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import com.razie.pub.base.data.HttpUtils;

/**
 * simple attribute access interface and implementation - a bunch of name-value pairs with many
 * different constructors - everything these days has attributes.
 * 
 * it is used throughout to access parms in a unified manner: from http requests, method arguments,
 * properties etc
 * 
 * <p>
 * It has a skeleton type definition.
 * 
 * <p>
 * Note the funny behavior of setAttr ("attrname:type", value)...
 * 
 * @author razvanc99
 * 
 */
public interface AttrAccess {
    /** these types MUST be supported by forms for capture, not necessarily by displays */
    public static enum AttrType {
        STRING, MEMO, SCRIPT, INT, FLOAT, DATE
    };

    /** @return the value of the named attribute */
    public Object getAttr(String name);

    /** set the value of the named attribute + the name can be of the form name:type */
    public void setAttr(String name, Object value);

    /**
     * set the value of one or more attributes
     * 
     * @parm pairs are pais of name/value, i.e. "car", "lexus" OR a Properties, OR another
     *       AttrAccess OR a Map<String,String>. Note that the parm name can contain the type, i.e.
     *       "name:string".
     */
    public void setAttr(Object... pairs);

    /** the number of populated attributes */
    public int size();

    /** iterate through the populated attributes */
    public Iterable<String> getPopulatedAttr();

    /** check if an attribute is populated */
    public boolean isPopulated(String name);

    /**
     * @return the type of the named attribute OR null if not known. Default is bny convention
     *         String
     */
    public AttrType getAttrType(String name);

    /**
     * set the type of the named attribute
     */
    public void setAttrType(String name, AttrType type);

    /** some random xml format */
    public String toXml();

    /** same pairs format name,value,name,value... */
    public Object[] toPairs();

    public JSONObject toJson(JSONObject obj);

    /**
     * add these attributes to an url, respecting the url parm format, i.e.
     * getMovie?name=300.divx&producer=whoknows
     */
    public String addToUrl(String url);

    /** simple base implementation */
    public class Impl implements AttrAccess {
        // lazy
        protected Map<String, Object>   parms = null;
        protected Map<String, AttrType> types = null;
        protected List<String>          order = null;

        /** dummy */
        public Impl() {
        };

        /**
         * build from sequence of parm/value pairs or other stuff
         * 
         * @parm pairs are pais of name/value, i.e. "car", "lexus" OR a Properties, OR another
         *       AttrAccess OR a Map<String,String>. Note the parm names can contain type: "name:string"
         */
        public Impl(Object... pairs) {
            this.setAttr(pairs);
        }

        public void setAttr(String name, Object value) {
            checkMap();
            // check name for type definition
            if (name.contains(":")) {
                // type defn can be escaped by a \
                String[] n = name.split("[^\\\\]:", 2);
                name = n[0].replaceAll("\\\\:", ":");
                if (n.length > 1)
                    this.setAttrType(name, n[1]);
            }
            if (!this.parms.containsKey(name))
                this.order.add(name);
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
                this.types = new HashMap<String, AttrType>();
                this.order = new ArrayList<String>();
            }
        }

        public Iterable<String> getPopulatedAttr() {
            return this.parms == null ? Collections.EMPTY_LIST : this.order;
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

        /** same pairs format name,value,name,value... */
        public Object[] toPairs() {
            int size = this.parms == null ? 0 : this.parms.size();
            Object[] ret = new Object[size * 2];

            int i = 0;
            for (String name : this.getPopulatedAttr()) {
                ret[i] = name;
                ret[i + 1] = getAttr(name);
                i += 2;
            }
            return ret;
        }

        /** TODO implement */
        public static AttrAccess fromJson() {
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
            // TODO implement reflection
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

        public AttrType getAttrType(String name) {
            return this.types != null ? types.get(name) : null;
        }

        public void setAttrType(String name, String type) {
            this.setAttrType(name, AttrType.valueOf(type.toUpperCase()));
        }

        public void setAttrType(String name, AttrType type) {
            checkMap();
            // TODO maybe it's too slow this toString?
            this.types.put(name, type);
        }
    }

    /** hierarchical implementation */
    public class TreeImpl extends Impl {
        AttrAccess parent;

        /** dummy */
        public TreeImpl(AttrAccess parent) {
            super();
            this.parent = parent;
        };

        /**
         * build from sequence of parm/value pairs or other stuff
         * 
         * @parm pairs are pais of name/value, i.e. "car", "lexus" OR a Properties, OR another
         *       AttrAccess OR a Map<String,String>
         */
        public TreeImpl(AttrAccess parent, Object... pairs) {
            this(parent);
            this.setAttr(pairs);
        }

        public Object getAttr(String name) {
            Object o = this.parms != null ? this.parms.get(name) : null;
            return o != null ? o : (parent != null ? parent.getAttr(name) : null);
        }

        public boolean isPopulated(String name) {
            boolean b = this.parms != null && this.parms.containsKey(name);
            return b ? true : (parent != null ? parent.isPopulated(name) : false);
        }
    }
}