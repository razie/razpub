/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.assets;


/**
 * contains info specific to file-based assets
 * 
 * TODO split into a generic assetbrief and a fileassetbrief - for files, with directories etc.
 * these are not applicable to say web assets...
 * 
 * @author razvanc99
 */
public class FileAssetBrief extends AssetBrief {

    public FileAssetBrief() {
    }

    public FileAssetBrief(String name) {
        super(name);
    }
}
