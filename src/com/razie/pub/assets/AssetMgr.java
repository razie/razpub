/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.assets;

import com.razie.pub.base.ActionItem;
import com.razie.pub.base.ScriptContext;

/**
 * the asset mgr is the way to interact with all assets. it can find, query and manage the assets
 * <p>
 * Implementation: just derive and implement the xxxImpl methods. A smarter asset manager will use
 * the inventories and be able to handle smart assets...
 * 
 * @version $Revision: 1.63 $
 * @author $Author: davidx $
 * @since $Date: 2005/04/01 16:22:12 $
 */
public abstract class AssetMgr {
    protected static AssetMgr impl;

    public static void init(AssetMgr implToUse) {
        impl = implToUse;
    }

    /** locator for assets by key */
    public static Object getAsset(AssetKey key) {
        return impl.getAssetImpl(key);
    }

    /** get the meta description for a certain type */
    public static Meta meta(String name) {
        return impl.metaImpl(name);
    }

    /**
     * execute injected command on given asset
     * 
     * @param cmd the command to execute. Default empty/null means "details" or paing the asset
     *        itself
     * @param ref ref to the object to invoke on
     * @param ctx context with parms etc
     * @return
     */
    public static Object executeCmd(String cmd, AssetKey ref, ScriptContext... ctx) {
        return impl.executeCmdImpl(cmd, ref, ctx);
    }

    protected abstract Object getAssetImpl(AssetKey key);

    protected abstract Meta metaImpl(String name);

    protected abstract Object executeCmdImpl(String cmd, AssetKey ref, ScriptContext... ctx);

    /**
     * a meta-description
     * 
     * The inventory and asset class specs are used elsewhere...didn't bother extending...you don't
     * have to use them
     */
    public static class Meta {
        public String     inventory;
        public String     assetCls;
        public String     baseMetaname;
        public ActionItem id;

        public Meta(ActionItem id, String base, String inventory) {
            this.baseMetaname = base;
            this.id = id;
            this.inventory = inventory;
        }

        public Meta(ActionItem id, String base, String assetCls, String inventory) {
            this.baseMetaname = base;
            this.id = id;
            this.inventory = inventory;
            this.assetCls = assetCls;
        }
    }
}