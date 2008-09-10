/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pubstage;

import com.razie.pub.assets.AssetKey;

/**
 * instead of passing explicit huge keys in url's - make the pages smaller by caching them and
 * passing a local cache index
 * 
 * TODO does this make sense?
 * 
 * TODO how do i deal with remote refs which i need to forward? thei still need an agent ref..
 * 
 * TODO implement
 * 
 * @author razvanc99
 */
public class AssetKeyCache {
    public String put(AssetKey k) {
        return k.toUrlEncodedString();
    }

    public AssetKey get(String i) {
        return AssetKey.fromString(i);
    }
}