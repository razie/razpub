/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.assets;

/**
 * the asset mgr is the way to interact with all assets. it can find, query and manage the assets
 * 
 * @version $Revision: 1.63 $
 * @author $Author: davidx $
 * @since $Date: 2005/04/01 16:22:12 $
 */
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
