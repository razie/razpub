/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.lightsoa.test;

import java.util.Map;

import com.razie.pub.assets.AssetBrief;
import com.razie.pub.assets.AssetKey;
import com.razie.pub.assets.AssetLocation;
import com.razie.pub.assets.AssetMgr;
import com.razie.pub.assets.AssetPres;
import com.razie.pub.base.ActionItem;
import com.razie.pub.base.ScriptContext;
import com.razie.pub.draw.Drawable;

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
        // should implement in a proper asset mgr
        return null;
    }

    @Override
    protected Object doActionImpl(String cmd, AssetKey ref, ScriptContext ctx) {
        // should implement in a proper asset mgr
        return null;
    }

    @Override
    protected AssetBrief briefImpl(AssetKey key) {
        // should implement in a proper asset mgr
        return null;
    }

    @Override
    protected Drawable detailsImpl(AssetBrief key) {
        // should implement in a proper asset mgr
        return null;
    }

    @Override
    protected ActionItem[] supportedActionsImpl(AssetKey key) {
        // should implement in a proper asset mgr
        return null;
    }

    @Override
    public Map<AssetKey, AssetBrief> findImpl(String type, AssetLocation env, boolean... recurse) {
        // should implement in a proper asset mgr
        return null;
    }

    @Override
    public AssetPres presImpl() {
        // should implement in a proper asset mgr
        return null;
    }

}