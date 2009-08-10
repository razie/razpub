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

import com.razie.pub.UnknownRtException;
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
 * <p>
 * Note the funny behavior of setAttr ("attrname:type=value,attrname2:type=value")...
 * 
 * @author razvanc99
 */
public interface AttrAccess {
    /** these types MUST be supported by forms for capture, not necessarily by displays */
    public static enum AttrType {
        STRING, MEMO, SCRIPT, INT, FLOAT, DATE
    };

    /** @return the value of the named attribute */
    public Object getAttr(String name);
    /** I'm really starting to hate typing... */
    public Object a(String name);
    /** most of the time they're just strings - i'll typecast here... this is a() typcast to String */
    public String sa(String name);

    /** set the value of the named attribute + the name can be of the form name:type */
    public void set(String name, Object value);
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
     * @return the type of the named attribute OR null if not known. Default is by convention String
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

    /** add my attributes to the JSONObject passed in. If null passed in, empty object is created first */
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
         *       AttrAccess OR a Map<String,String>. Note the parm names can contain type:
         *       "name:string"
         */
        public Impl(Object... pairs) {
            this.setAttr(pairs);
        }

        /* TODO should setAttr(xx,null) remove it so it's not populated? */
        public void set(String name, Object value) { setAttr(name,value); }
        
        /* TODO should setAttr(xx,null) remove it so it's not populated? */
        public void setAttr(String name, Object value) {
            checkMap();
            // check name for type definition
            if (name.contains(":")) {
                String n[];

                // type defn can be escaped by a \
                int idx = name.indexOf("\\:");
                if (idx >= 0 && idx == name.indexOf(":") - 1) {
                    n = new String[2];
                    
                    // let's see if it does have a type...
                    String s2 = name.substring(idx + 2);
                    int idx2 = s2.indexOf(":");
                    if (idx2 >= 0) {
                        n[0] = name.substring(0, idx+2+idx2);
                        n[1] = name.substring(idx+2+idx2+1);
                    } else {
                        n[0] = name;
                        n[1] = null;
                    }
                    
                    name = n[0] = n[0].replaceAll("\\\\:", ":");
                } else
                    n = name.split(":", 2);

                // basically, IF there's a ":" AND what's after is a recognied type...otherwise i'll
                // assume the parm name is "a:b"
                if (n.length > 1 && n[1] != null) {
                    if (AttrType.valueOf(n[1].toUpperCase()) != null) {
                        name = n[0];
                        this.setAttrType(name, n[1]);
                    }
                }
            }
            if (!this.parms.containsKey(name))
                this.order.add(name);
            this.parms.put(name, value);
        }

        public Object getAttr(String name) {
            return this.parms != null ? this.parms.get(name) : null;
        }

        public Object a(String name) {
            return getAttr(name);
        }

        public String sa(String name) {
            return (String)a(name);
        }

        /**
         * @parm pairs are pais of name/value, i.e. setAttr("car", "lexus") OR a Properties, OR
         *       another AttrAccess OR a Map<String,String>
         */
        public void setAttr(Object... pairs) {
            if (pairs != null && pairs.length == 1 && pairs[0] instanceof Map) {
                Map<String, String> m = (Map<String, String>) pairs[0];
                for (Map.Entry<String,String> entry : m.entrySet()) {
                    this.setAttr(entry.getKey(), m.get(entry.getKey()));
                }
            } else if (pairs != null && pairs.length == 1 && pairs[0] instanceof Properties) {
                Properties m = (Properties) pairs[0];
                for (Map.Entry<Object,Object> entry : m.entrySet()) {
                    this.setAttr((String)entry.getKey(), m.get((String)entry.getKey()));
                }
            } else if (pairs != null && pairs.length == 1 && pairs[0] instanceof AttrAccess) {
                AttrAccess m = (AttrAccess) pairs[0];
                for (String s : m.getPopulatedAttr()) {
                    this.setAttr((String) s, m.getAttr((String) s));
                }
            } else if (pairs != null && pairs.length == 1 && pairs[0] instanceof String) {
                /* one line defn of a bunch of parms */
                /*
                 * Note the funny behavior of setAttr
                 * ("attrname:type=value,attrname2:type=value")...
                 */
                String m = (String) pairs[0];
                String[] n = m.split("[,&]");
                for (String s : n) {
                    String[] ss = s.split("=", 2);

                    String val = null;
                    if (ss.length > 1)
                        val = ss[1];

                    String nametype = ss[0];
                    this.setAttr(nametype, val);
                }
            } else if (pairs != null && pairs.length > 1) {
                for (int i = 0; i < pairs.length / 2; i++) {
                    String name = (String) pairs[2 * i];
                    if (name != null)
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

        @SuppressWarnings("unchecked")
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
        public static AttrAccess fromJsonString(String s) {
            try {
               return fromJson(new JSONObject(s));
            } catch (JSONException e) {
               throw new UnknownRtException(e);
            }
        }

        /** TODO implement */
        public static AttrAccess fromJson(JSONObject o) {
            AttrAccess a = new Impl();
            for (String n : o.getNames(o))
               try {
                  a.setAttr(n, o.getString(n));
               } catch (JSONException e) {
               throw new UnknownRtException(e);
               }
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

        protected boolean hasAttrType(String name) {
            return this.types != null && types.get(name) != null;
        }

        public AttrType getAttrType(String name) {
            AttrType t = this.types != null ? types.get(name) : null;
            return t == null ? AttrType.STRING : t;
        }

        public void setAttrType(String name, String type) {
            this.setAttrType(name, AttrType.valueOf(type.toUpperCase()));
        }

        public void setAttrType(String name, AttrType type) {
            checkMap();
            // TODO maybe it's too slow this toString?
            this.types.put(name, type);
        }
        
      @Override
        public String toString () {
            String ret="";
            String comma="";
            for (String a : this.getPopulatedAttr()) {
                ret += comma + a + (this.hasAttrType(a) ? ":"+getAttrType(a):"") + "=" + getAttr(a).toString();
                comma=",";
            }
            return ret;
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

      @Override
        public Object getAttr(String name) {
            Object o = this.parms != null ? this.parms.get(name) : null;
            return o != null ? o : (parent != null ? parent.getAttr(name) : null);
        }

      @Override
        public boolean isPopulated(String name) {
            boolean b = this.parms != null && this.parms.containsKey(name);
            return b ? true : (parent != null ? parent.isPopulated(name) : false);
        }
        
      @Override
        public String toString () {
            String ret= parent != null ? parent.toString() : "";
            return ret + super.toString();
        }
    }
}