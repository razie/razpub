/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.assets;

import java.io.Serializable;

import com.razie.pub.base.data.HttpUtils;

/**
 * Each entity/asset has a unique id, which identifies the asset's type, key and location. Borrowed
 * from OSS/J's ManagedEntityKey (type, key, location), this is lighter and designed to pass through
 * URLs and be easily managed as a string form.
 * 
 * <p>
 * the asset-URI has this format: <code>"<mutant://entityType:entityKey@location>"</code>
 * <ul>
 * <li>type is the type of entity, should be unique among all other types. HINT: do not keep
 * defining "Movie" etc - alsways assume someone else did...
 * <li>key is the unique key of the given entity, unique in this location and for this type
 * <li>location identifies the location of the entity: either URL or folder or a combination
 * </ul>
 * 
 * @author razvanc99
 */
@SuppressWarnings("serial")
public class AssetKey implements Serializable {
    public static final String      PREFIX = "mutant://";

    /** don't want to keep formatting new strings every time, so we cache the last one */
    private String                  cachedUrlForm;

    private String                  type;
    private String                  id;

    /**
     * must be able to identify the app env to the resolver. Null is the same as local or unknown
     */
    private transient AssetLocation location;

    /** full constructor */
    public AssetKey(String type, String id, AssetLocation appEnv) {
        this.setType(type);
        this.setId(id);
        this.setLocation(appEnv == null ? new AssetLocation() : appEnv);

        if (id == null) {
            this.setId(uid());
        }
    }

    /** full constructor */
    public AssetKey(String type, String id) {
        this(type, id, new AssetLocation());
    }

    /** Used in GRef keys compare */
    protected static boolean equalKeys(String key1, String key2) {
        return key1 != null ? key1.equals(key2) : (key2 == null ? true : false);
    }

    /** logical, not phisical equals */
    @Override
    public boolean equals(Object o) {
        if (o instanceof AssetKey) {
            AssetKey r = (AssetKey) o;
            if (r == null) {
                return false;
            }
            return getType().equals(r.getType()) && equalKeys(getId(), r.getId());
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return getType().hashCode() + (getId() != null ? getId().hashCode() : 0);
    }

    /** short descriptive string */
    @Override
    public String toString() {
        if (cachedUrlForm == null) {
            cachedUrlForm = "<" + PREFIX + getType() + ":"
                    + (getId() == null ? "" : HttpUtils.toUrlEncodedString(getId()));
            if (getLocation() != null) {
                cachedUrlForm += "@" + getLocation().toString();
            }
            cachedUrlForm += ">";
        }
        return cachedUrlForm;
    }

    /** short descriptive string */
    public String toSimpleString() {
        String s = getType() + ":" + (getId() == null ? "" : HttpUtils.toUrlEncodedString(getId()));
        if (getLocation() != null) {
            s += "@" + getLocation().toString();
        }
        return s;
    }

    /**
     * Use this method to get a string that is safe to use in a URL. Note that whenever the string
     * is encoded when you want to use it it must be decoded with fromUrlEncodedString(String).
     */
    public String toUrlEncodedString() {
        return HttpUtils.toUrlEncodedString(this.toString());
    }

    /**
     * @param etype
     */
    public void setType(String etype) {
        this.type = etype;
        this.cachedUrlForm = null;
    }

    /**
     * @return the type of the entity referenced, i.e. "Movie"
     */
    public String getType() {
        return type;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
        this.cachedUrlForm = null;
    }

    /**
     * @return the entityKey
     */
    public String getId() {
        return id;
    }

    /**
     * @param appEnv the appEnv to set
     */
    public void setLocation(AssetLocation appEnv) {
        this.location = appEnv;
        this.cachedUrlForm = null;
    }

    /**
     * @return the appEnv
     */
    public AssetLocation getLocation() {
        return location;
    }

    /**
     * just a simple UID implementation, to fake IDs for objects that don't have them.
     */
    public static String uid() {
        return "GUid-" + seqNum++ + "-" + String.valueOf(System.currentTimeMillis());
    }

    /** to allocate next UID...this should be done better */
    protected static int seqNum = 1;

    private String       entityBriefDescription;

    public String getEntityBriefDescription() {
        return entityBriefDescription;
    }

    public void setEntityBriefDescription(String desc) {
        entityBriefDescription = desc;
    }

    public AssetKey clone() {
        AssetKey k = new AssetKey(this.type, this.id, this.location);
        k.entityBriefDescription = this.entityBriefDescription;
        return k;
    }

    /**
     * make up from an entity-URI. see class javadocs for details on URI
     * 
     * TODO it's not efficient - creates too many objects to parse the string
     * 
     * @return the entity-URI
     */
    public static AssetKey fromString(String inurl) {
        // get rid of <>
        // honestly, if it didn't have them i should blow up?
        String url = inurl;

        // sometimes the GUI will put an extra pair of < and >
        // TODO optimize - just decrease index not constantly substring
        while (url.endsWith(">")) {
            url = url.substring(0, url.length() - 1);
        }

        // TODO optimize like above
        while (url.startsWith("<")) {
            url = url.substring(1, url.length());
        }

        String[] map1 = url.split("://", 2);
        
        // with the following, i support also a missing PREFIX, i.e. a simplified KEY with just type:key@loc
        String news = (map1.length > 1 ? map1[1] : (map1.length==1?map1[0]:null));

        if (news != null) {
            // i have a class nm
            String[] map2 = map1[1].split(":", 2);
            if (map2[1] != null) {
                // i have a key/id
                String[] map3 = map2[1].split("@", 2);
                if (map3.length > 1) {
                    // i have an appEnv
                    return new AssetKey(map2[0], HttpUtils.fromUrlEncodedString(map3[0]), new AssetLocation(
                            map3[1]));
                } else {
                    // no appEnv
                    return new AssetKey(map2[0], HttpUtils.fromUrlEncodedString(map3[0]), null);
                }
            } else {
                // no key/id
                return new AssetKey(map2[0], null, null);
            }
        }
        return null;
    }
}
