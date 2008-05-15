package com.razie.pub.lightsoa;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.razie.pub.assets.AssetKey;
import com.razie.pub.assets.BaseAssetMgr;
import com.razie.pub.base.AttrAccess;
import com.razie.pub.base.data.HttpUtils;
import com.razie.pub.http.MyServerSocket;

/**
 * there can be only one instance of this, reghstered as the service name "asset"
 * 
 * @author razvanc99
 */
public class HttpAssetSoaBinding extends HttpSoaBinding {
    static Map<String, HttpSoaBinding> bindings = new HashMap<String, HttpSoaBinding>();

    /**
     * create a simple binding - you then have to register it with the server
     */
    public HttpAssetSoaBinding() {
        // dummy - i have no soa methods
        super(HttpAssetSoaBinding.class, "asset");

        // now should register all asset types as listeners...
    }

    public static void register(Class c) {
        bindings.put(((SoaAsset) c.getAnnotation(SoaAsset.class)).type(), new HttpSoaBinding(c, ((SoaAsset) c
                .getAnnotation(SoaAsset.class)).type()));
    }

    public static boolean has(String type) {
        return bindings.containsKey(type);
    }

    public Object executeCmdServer(String actionName, String protocol, String cmdargs, Properties parms,
            MyServerSocket socket) {
        AssetKey key = AssetKey.fromEntityUrl(HttpUtils.fromUrlEncodedString(actionName));

        HttpSoaBinding binding = bindings.get(key.getType());
        if (binding == null)
            throw new IllegalArgumentException("ERR_HTTPSOAASSET type is not bound: " + key.getType());
        return binding.executeCmdServer(actionName, protocol, cmdargs, parms, socket);
    }

    /** invoke a lightsoa method on a given service */
    public static Object invokeLocal(AssetKey key, String action, AttrAccess inparms) {
        HttpSoaBinding binding = bindings.get(key.getType());
        return binding.invoke(key, action, inparms);
    }

}
