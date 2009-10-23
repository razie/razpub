/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.assets;

import java.util.Map;

import com.razie.pub.base.ActionItem;
import com.razie.pub.base.NoStatic;
import com.razie.pub.base.ScriptContext;
import com.razie.pub.draw.Drawable;

/**
 * the asset mgr concentrates all interactions will all assets. it can find, query and manage all
 * the assets
 * 
 * <p>
 * Implementation: just derive and implement the xxxImpl methods. A smarter asset manager will use
 * the inventories and be able to handle smart assets...
 * 
 * <p>
 * Where this is useful: it decouples the rest of the code that uses assets (clients) from the
 * actual implementation of an asset. Also, it is augmented by generic REST access to assets etc.
 * 
 * @author razvanc
 */
public abstract class AssetMgr {
    protected static NoStatic<AssetMgr> impl = new NoStatic<AssetMgr> ("AssetMgr", null);

    public static AssetMgr instance() {
        return impl.get();
    }

    public static void init(AssetMgr implToUse) {
        impl.set(implToUse);
    }

    /** locator for assets by key */
    public static Object getAsset(AssetKey key) {
        return instance().getAssetImpl(key);
    }

    /** get the supported metas*/
    public static Iterable<String> metas() {
        return instance().metasImpl();
    }

    /** TODO 1-1 does it belong here? how does one generically register a new type */
    public void register(Meta meta) {
        throw new UnsupportedOperationException ("must implement in derived class");
    }

    /** get the meta description for a certain type */
    public static Meta meta(String name) {
        return instance().metaImpl(name);
    }

    /** a detailed description and commands for an asset */
    public static Drawable details(AssetBrief asset) {
        return instance().detailsImpl(asset);
    }

    public static ActionItem[] supportedActions(AssetKey ref) {
        return instance().supportedActionsImpl(ref);
    }

    public static AssetBrief brief(AssetKey ref) {
        return instance().briefImpl(ref);
    }

    public static Map<AssetKey, AssetBrief> find(String type, AssetLocation env, boolean recurse) {
        return instance().findImpl(type, env, recurse);
    }

    /**
     * execute injected command on given asset
     * 
     * @param action the action/command to execute. Default empty/null means "details" or painting
     *        the asset itself
     * @param ref ref to the object to invoke on
     * @param ctx context with parms etc
     * @return
     */
    public static Object doAction(String action, AssetKey ref, ScriptContext ctx) {
        return instance().doActionImpl(action, ref, ctx);
    }

    public static AssetPres pres() {
        return instance().presImpl();
    }

    protected abstract Object getAssetImpl(AssetKey key);

    protected abstract Drawable detailsImpl(AssetBrief key);

    protected abstract AssetBrief briefImpl(AssetKey key);

    protected abstract ActionItem[] supportedActionsImpl(AssetKey key);

    protected abstract Meta metaImpl(String name);
    protected abstract Iterable<String> metasImpl();

    protected abstract Object doActionImpl(String cmd, AssetKey ref, ScriptContext ctx);

    public abstract Map<AssetKey, AssetBrief> findImpl(String type, AssetLocation env, boolean recurse);

    public abstract AssetPres presImpl();
   
}
