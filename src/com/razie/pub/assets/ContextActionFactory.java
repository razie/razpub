/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.assets;

import java.util.List;

import razie.assets.AssetKey;
import com.razie.pub.comms.ActionToInvoke;

/**
 * context-specific actions are built here. This is invoked just before those are needed for
 * display. Find usage for details...
 * 
 * @author razvanc
 */
public abstract class ContextActionFactory {
   public abstract List<ActionToInvoke> make(AssetKey assetInQuestion);
}