/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.assets;

import com.razie.pub.base.ActionItem;
import com.razie.pub.base.AttrAccess;
import com.razie.pub.comms.ActionToInvoke;

/**
 * this represents a full asset
 * 
 * TODO find a GOOD use for this - I think we need it but not sure if it simplifies clients...
 * 
 * @author razvanc99
 */
public class AssetHandle implements Referenceable {
    protected AssetKey key;

    public AssetHandle(AssetKey key) {
        this.key = key;
    }

    /**
     * convenience method - another form of invoke
     * 
     * @param method
     * @param args
     * @return
     */
    public Object invoke(String method, Object... args) {
        return invoke(method, new AttrAccess.Impl(args));
    }

    /**
     * invoke a local/remote asset
     * 
     * TODO implement sync/async flavors via a messaging service abstraction
     * 
     * 
     * @param method
     * @param args
     * @return
     */
    public Object invoke(String method, AttrAccess aa) {
        // 1. figure out url to home agent
        // 2. invoke remote

        String result = null;

        if (key.getLocation() == null || (key.getLocation().isLocal() || !key.getLocation().isRemote())) {
            ActionToInvoke action = new AssetActionToInvoke(key, new ActionItem(method), aa);
            result = (String) action.act(null);
        } else {
            ActionToInvoke action = new AssetActionToInvoke(key.getLocation().toHttp(), key, new ActionItem(
                    method), aa);
            result = (String) action.act(null);
        }
        return result;
    }

    public AssetKey getKey() {
        return key;
    }
}
