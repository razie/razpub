/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.lightsoa.test;

import razie.assets.AssetBase;
import razie.assets.AssetKey;
import razie.assets.HasMeta;
import razie.assets.MetaSpec;
import razie.assets.Referenceable;
import razie.draw.DrawStream;

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
public class SampleAsset implements AssetBase, Referenceable, HasMeta {
    AssetKey key;

    public SampleAsset(String k) {
        key = new AssetKey ("raz.test.Player", k);
    }

   public MetaSpec metaSpec () { return new MetaSpec ("raz.test.Player"); }
   
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

   @Override
   public razie.assets.AssetBrief brief() {
      return null;
   }

   @Override
   public razie.assets.AssetBrief getBrief() {
      return null;
   }

   @Override
   public AssetKey getKey() {
      return key;
   }

   @Override
   public AssetKey key() {
      // TODO Auto-generated method stub
      return key;
   }

}
