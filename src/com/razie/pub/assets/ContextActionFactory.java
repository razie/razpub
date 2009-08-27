/**
 * Razvan's code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.assets;

import java.util.List;

import com.razie.pub.assets.AssetKey;
import com.razie.pub.comms.ActionToInvoke;

public abstract class ContextActionFactory {
    public abstract List<ActionToInvoke> make(AssetKey assetInQuestion);
}