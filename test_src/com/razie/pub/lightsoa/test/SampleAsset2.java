/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.lightsoa.test;

import com.razie.pub.assets.AssetKey;
import com.razie.pub.draw.DrawStream;
import com.razie.pub.lightsoa.SoaAsset;
import com.razie.pub.lightsoa.SoaMethod;
import com.razie.pub.lightsoa.SoaResponse;
import com.razie.pub.lightsoa.SoaStreamable;

/**
 * not annotated asset class - you can add annotated methods to any class
 * 
 * this is useful when the metas are not available but you can modify the asset class to add SoaMethod and avoid injection
 * 
 * @author razvanc99
 * 
 */
public class SampleAsset2 {
    AssetKey key;

    public SampleAsset2(AssetKey key) {
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
