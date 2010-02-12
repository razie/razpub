package com.razie.pub.assets;

import razie.assets.AssetBrief;
import razie.assets.AssetKey;
import razie.assets.AssetLocation;
import razie.assets.AssetMap;
import razie.assets.Meta;
import razie.assets.QueryCriteria;
import razie.base.ActionItem;
import razie.base.ScriptContext;


/**
 * inventories manage entities - we need invnetories to GET entities from a key, or QUERY/FIND
 * entities
 * 
 * invnetories are indexed by managed entity types in the AssetMgr
 * 
 * inventories manage entities - basic functionality is locating assets
 * 
 * inventories should not be accessed directly, but via the AssetMgr.
 * 
 */
public interface JavaAssetInventory extends razie.assets.AssetInventory {
   
   //----------------------- restate the base inherited methods, for java-only folk
   
    /** get an asset by key - it should normally be AssetBase or SdkAsset */
    public Object getAsset(AssetKey ref);

    /**
     * get/make the brief for an asset given its key. The idea around briefs is that I don't always
     * need the full asset - often i can get around by just a proxy brief
     */
    public AssetBrief getBrief(AssetKey ref);

    /** execute command on asset. the asset can be local or remote */
    public Object doAction(String cmd, AssetKey ref, ScriptContext ctx);

    public ActionItem[] getSupportedActions(AssetKey ref);

    /** initialize this instance for use with this Meta */
    public void init (Meta meta);

    //--------------------- add the java version for finder
    
//    /** list all assets of the given type at the given location */
//    public Map<AssetKey, AssetBrief> find(String type, AssetLocation env, boolean recursive);
    /** queries can run in the background, they are multithreaded safe etc */
    public AssetMap query(QueryCriteria criteria, AssetLocation env, boolean recurse, AssetMap toUse);

}
