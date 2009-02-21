/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.lightsoa;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.razie.pub.assets.AssetKey;
import com.razie.pub.base.AttrAccess;
import com.razie.pub.base.data.HttpUtils;
import com.razie.pub.comms.MyServerSocket;

/**
 * there can be only one instance of this, reghstered as the service name "asset"
 * 
 * @author razvanc99
 */
public class HttpAssetSoaBinding extends HttpSoaBinding {
    Map<String, HttpSoaBinding> bindings            = new HashMap<String, HttpSoaBinding>();

    // TODO what the heck is this?
    static HttpSoaBinding       defaultAssetBinding = new HttpSoaBinding((Class) null, "");

    /**
     * create a simple binding - you then have to register it with the server
     */
    public HttpAssetSoaBinding() {
        // dummy - i have no soa methods
        super(HttpAssetSoaBinding.class, "asset");

        // now should register all asset types as listeners...
    }

    public void register(Class c) {
        if (!bindings.containsKey(((SoaAsset) c.getAnnotation(SoaAsset.class)).type()))
            bindings.put(((SoaAsset) c.getAnnotation(SoaAsset.class)).type(), new HttpSoaBinding(c,
                    ((SoaAsset) c.getAnnotation(SoaAsset.class)).type()));
    }

    public boolean has(String type) {
        return bindings.containsKey(type);
    }

    @Override
    public Object executeCmdServer(String actionName, String protocol, String cmdargs, Properties parms,
            MyServerSocket socket) {
        AssetKey key = AssetKey.fromString(HttpUtils.fromUrlEncodedString(actionName));

        HttpSoaBinding binding = bindings.get(key.getType());
        if (binding == null) {
            // throw new IllegalArgumentException("ERR_HTTPSOAASSET type is not bound: " +
            // key.getType());
            // try to call a generic asset via its inventory or other injection mechanism...
            return defaultAssetBinding.executeCmdServer(actionName, protocol, cmdargs, parms, socket);
        } else
            return binding.executeCmdServer(actionName, protocol, cmdargs, parms, socket);
    }

    /** invoke a lightsoa method on a given service */
    public Object invokeLocal(AssetKey key, String action, AttrAccess inparms) {
        HttpSoaBinding binding = bindings.get(key.getType());
        return binding.invoke(key, action, inparms);
    }

}
