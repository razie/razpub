/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.lightsoa.test;

import com.razie.pub.assets.AssetKey;
import com.razie.pub.draw.DrawStream;
import com.razie.pub.lightsoa.SoaAsset;
import com.razie.pub.lightsoa.SoaMethod;
import com.razie.pub.lightsoa.SoaResponse;
import com.razie.pub.lightsoa.SoaStreamable;

/**
 * fully annotated sample asset class
 * 
 * @author razvanc99
 */
@SoaAsset(meta = "raz.test.Player", descr = "test player asset")
public class SampleAsset {
    AssetKey key;

    public SampleAsset(AssetKey key) {
        this.key = key;
    }

    @SoaMethod(descr = "play a movie", args = { "movie" })
    public String play(String movie) {
        AssetKey key = AssetKey.fromString(movie);
        if (key == null) {
            throw new IllegalArgumentException("Movie not found: " + movie);
        }
        return key.getId();
    }

    @SoaMethod(descr = "concatenate two values", args = { "parm1", "parm2" })
    public SoaResponse concatenate(String parm1, String parm2) {
        // that's how it's done in UPnP
        return new SoaResponse("Result", parm1 + parm2);
    }

    @SoaMethod(descr = "does nothing", args = { "parm1", "parm2" })
    public void doNothing(String parm1, String parm2) {
    }

    /** note that it cant have the same name as the other one */
    @SoaMethod(descr = "concatenate two values", args = { "parm1", "parm2" })
    @SoaStreamable
    public void concatenateStream(DrawStream out, String parm1, String parm2) {
        out.write(parm1 + parm2);
    }

}
