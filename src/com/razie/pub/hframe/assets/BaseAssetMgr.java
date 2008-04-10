/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.assets;

public abstract class BaseAssetMgr {
    protected static BaseAssetMgr impl;

    public static void init(BaseAssetMgr implToUse) {
        impl = implToUse;
    }

    public static Object getAsset(AssetKey key) {
        return impl.getAssetImpl(key);
    }

    public abstract Object getAssetImpl(AssetKey key);
}
