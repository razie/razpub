/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.lightsoa;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.razie.pub.assets.AssetKey;
import com.razie.pub.assets.AssetMgr;
import com.razie.pub.base.AttrAccess;
import com.razie.pub.draw.DrawStream;

/**
 * lightsoa services need binding to a certain protocol...bindings are instances of this. You can
 * bind a service to more than one protocols...
 * 
 * each binding must support methods returning: SoaResponse, void and String
 * 
 * Also, each binding will escape and un-escape each input and output argument properly
 * 
 * <p>
 * This base class just implements some common functionality, it has no knowledge of a particular
 * protocol.
 * 
 * @author razvanc99
 * 
 */
public class SoaBinding {
    protected Object              service;
    protected String              serviceName;
    protected Map<String, Method> methods = new HashMap<String, Method>();
    // if not null wants to sink all calls
    protected Method              sink    = null;

    /** build for a service object */
    public SoaBinding(Object service, String serviceName) {
        this(service.getClass(), serviceName);
        this.service = service;
    }

    /** build for an asset class */
    public SoaBinding(Class<?> assetClass, String serviceName) {
        this.serviceName = serviceName;

        // allow hacks with no methods - don't have to blow up...
        if (assetClass != null)
            for (Method m : assetClass.getMethods()) {
                if (m.getAnnotation(SoaMethod.class) != null) {
                    methods.put(m.getName(), m);
                }
                if (m.getAnnotation(SoaMethodSink.class) != null) {
                    // there's just one sink
                    sink = m;
                }
            }
    }

    public Set<String> getSoaMethods() {
        return methods.keySet();
    }

    /** invoke a lightsoa method on a given service */
    public Object invoke(String action, AttrAccess inparms) {
        return this.invoke(this.service, action, inparms);
    }

    /** invoke a lightsoa method on a given service */
    public Object invoke(AssetKey key, String action, AttrAccess inparms) {
        Object asset = AssetMgr.getAsset(key);
        if (asset != null)
            return this.invoke(asset, action, inparms);
        else
            throw new IllegalArgumentException("Asset not found by key: " + key);
    }

    /** invoke a lightsoa method on a given service */
    public Object invoke(Object target, String action, AttrAccess inparms) {
        Method toinvoke = methods.get(action);
        if (toinvoke == null && sink != null) {
            toinvoke = sink;
            inparms.set(SoaMethodSink.SOA_METHODNAME, action);
        }

        if (toinvoke == null) {
            throw new IllegalArgumentException("ERR_SOA cannot find method=" + action + " on target class="
                    + service.getClass().getName());
        }

        // setup the parms
        List<Object> args = new ArrayList<Object>();
        SoaMethod mdesc = toinvoke.getAnnotation(SoaMethod.class);

        if (toinvoke.getAnnotation(SoaAllparms.class) != null) {
            args.add(inparms);
        } else
            for (String arg : mdesc.args()) {
                args.add(inparms.getAttr(arg));
            }

        if (toinvoke.getAnnotation(SoaStreamable.class) != null) {
            // TODO should throwup since you're supposed to call invikeStreamable...
        }

        // actual invocation
        Object res = null;
        try {
            res = toinvoke.invoke(target, args.toArray());
        } catch (Exception e) {
            throw new RuntimeException("ERR_INVOKING_SOA " + mdesc.toString() + "\n ARGS: "
                    + inparms.toString(), e);
        }

        // so void methods don't need to do anything...
        return res;
    }

    /**
     * invoke a streamable lightsoa method on a given service...sorry, but you have to look for
     * streamables yourself
     */
    public Object invokeStreamable(String action, DrawStream stream, AttrAccess inparms) {
        return this.invokeStreamable(this.service, action, stream, inparms);
    }

    /** invoke a lightsoa method on a given service */
    public Object invokeStreamable(AssetKey key, String action, DrawStream stream, AttrAccess inparms) {
        Object asset = AssetMgr.getAsset(key);
        if (asset != null)
            return this.invokeStreamable(asset, action, stream, inparms);
        else
            throw new IllegalArgumentException("Asset not found by key: " + key);
    }

    /**
     * invoke a streamable lightsoa method on a given service...sorry, but you have to look for
     * streamables yourself
     */
    public Object invokeStreamable(Object target, String action, DrawStream stream, AttrAccess inparms) {
        Method toinvoke = methods.get(action);
        if (toinvoke == null && sink != null)
            toinvoke = sink;

        if (toinvoke == null) {
            throw new IllegalArgumentException("ERR_SOA cannot find method=" + action + " on target class="
                    + service.getClass().getName());
        }

        if (toinvoke.getAnnotation(SoaStreamable.class) == null) {
            throw new IllegalArgumentException("ERR_SOA method not streamable method=" + action
                    + " on target class=" + service.getClass().getName());
        }

        // setup the parms
        List<Object> args = new ArrayList<Object>();
        SoaMethod mdesc = toinvoke.getAnnotation(SoaMethod.class);

        // the first argument is a stream and not in the description
        args.add(stream);

        if (toinvoke.getAnnotation(SoaAllparms.class) != null) {
            args.add(inparms);
        } else
            for (String arg : mdesc.args()) {
                args.add(inparms.getAttr(arg));
            }

        // actual invocation
        Object res = null;
        try {
            res = toinvoke.invoke(target, args.toArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // so void methods don't need to do anything...
        return res;
    }

    public String getServiceName() {
        return this.serviceName;
    }
}
