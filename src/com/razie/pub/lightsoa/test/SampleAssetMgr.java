/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.lightsoa.test;

import com.razie.pub.assets.AssetKey;
import com.razie.pub.assets.AssetMgr;
import com.razie.pub.base.ScriptContext;

/**
 * dumb asset manager
 * 
 * @author razvanc
 */
public class SampleAssetMgr extends AssetMgr {
    @Override
    public Object getAssetImpl(AssetKey key) {
        return new SampleAsset(key);
    }

    @Override
    protected Meta metaImpl(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Object executeCmdImpl(String cmd, AssetKey ref, ScriptContext... ctx) {
        // TODO Auto-generated method stub
        return null;
    }

}
