package com.razie.pub.assets;

import java.util.Collection;
import java.util.List;

import com.razie.pub.draw.DrawStream;
import com.razie.pub.draw.Drawable;
import com.razie.pubstage.UserPrefs;

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

    /**
     * put a list of assets into drawable
     * 
     * @param movies a collection of assets
     * @param stream the stream to draw on
     * @param context current context for actions
     * @param visuals visual preferences
     * @return the resulting drawable - note that it has already been drawn on the stream, if any stream was passed in
     */
    public abstract Drawable toDrawable(Collection<AssetBrief> movies, DrawStream stream,
            ContextActionFactory context, UserPrefs.AssetListVisual... visuals);

    public abstract List<Drawable> makeAllButtons(AssetBrief movie, boolean drawTiny);
}
