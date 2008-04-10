//==========================================================================
// $Id: Svc.java,v 1.63 2005/04/01 16:22:12 davidx Exp $
// (@) Copyright Sigma Systems (Canada) Inc.
//==========================================================================
package com.razie.pub.hframe.lightsoa.test;

import com.razie.pub.hframe.assets.AssetKey;
import com.razie.pub.hframe.assets.BaseAssetMgr;

public class SampleAssetMgr extends BaseAssetMgr {
    public static void init() {
        impl = new SampleAssetMgr();
    }

    @Override
    public Object getAssetImpl(AssetKey key) {
        return new SampleAsset(key);
    }

}
