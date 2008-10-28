package com.razie.pub.assets;

import java.util.List;

import com.razie.pub.draw.Drawable;

/**
 * concentrates asset presentation commonalities - are actually implemented in the mutant
 * 
 * to use this class thus, you need the mutant jar file
 * 
 * @author razvanc
 * 
 */
public abstract class AssetPres {

    public static AssetPres instance() {
        return AssetMgr.pres();
    }
    
    public abstract List<Drawable> makeAllButtons(AssetBrief movie, boolean drawTiny) ;
}