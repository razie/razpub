/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.assets;

import org.json.JSONString;

import com.razie.pub.base.AttrAccess;
import com.razie.pub.draw.Drawable;

/**
 * contains info specific to file-based assets
 * 
 * TODO AssetBrief should be final and immutable - for now i still use the SdkAssetBrief in the
 * mutant...
 * 
 * TODO split into a generic assetbrief and a fileassetbrief - for files, with directories etc.
 * these are not applicable to say web assets...
 * 
 * @author razvanc99
 */
public class FileAssetBrief extends AssetBrief implements AttrAccess, Drawable, JSONString {

    public FileAssetBrief() {
    }

    public FileAssetBrief(String name) {
        super(name);
    }
}
