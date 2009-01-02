/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.assets;

import java.util.Map;

import com.razie.pub.base.ActionItem;
import com.razie.pub.base.ScriptContext;
import com.razie.pub.draw.Drawable;

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

    public static AssetMgr instance () { return impl; }
    
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

    /** a detailed description and commands for an asset */
    public static Drawable details(AssetBrief asset) {
        return impl.detailsImpl(asset);
    }

    public static ActionItem[] supportedActions(AssetKey ref) {
        return impl.supportedActionsImpl(ref);
    }

    public static AssetBrief brief(AssetKey ref) {
        return impl.briefImpl(ref);
    }

    public static Map<AssetKey, AssetBrief> find(String type, AssetLocation env, boolean... recurse) {
        return impl.findImpl(type, env, recurse);
    }

    /**
     * execute injected command on given asset
     * 
     * @param action the action/command to execute. Default empty/null means "details" or paing the asset
     *        itself
     * @param ref ref to the object to invoke on
     * @param ctx context with parms etc
     * @return
     */
    public static Object doAction(String action, AssetKey ref, ScriptContext ctx) {
        return impl.doActionImpl(action, ref, ctx);
    }

    public static AssetPres pres() {return impl.presImpl();}
    
    protected abstract Object getAssetImpl(AssetKey key);

    protected abstract Drawable detailsImpl(AssetBrief key);

    protected abstract AssetBrief briefImpl(AssetKey key);

    protected abstract ActionItem[] supportedActionsImpl(AssetKey key);

    protected abstract Meta metaImpl(String name);

    protected abstract Object doActionImpl(String cmd, AssetKey ref, ScriptContext ctx);

    public abstract Map<AssetKey, AssetBrief> findImpl(String type, AssetLocation env, boolean... recurse);

    public abstract AssetPres presImpl();
    
    /**
     * a meta-description
     * 
     * The inventory and asset class specs are used elsewhere...didn't bother extending...you don't
     * have to use them
     */
    public static class Meta {
        public String       inventory;
        public String       assetCls;
        public String       baseMetaname;
        public ActionItem   id;
        public ActionItem[] supportedActions;

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
