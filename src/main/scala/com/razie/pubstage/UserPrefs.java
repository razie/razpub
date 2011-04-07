package com.razie.pubstage;

import razie.base.AttrAccess;
import razie.base.AttrAccessImpl;

/**
 * store user preferences
 * 
 * TODO create/maintain file
 * 
 * TODO sync file across agents
 * 
 */
public class UserPrefs {

    private static UserPrefs singleton;

    public static UserPrefs getInstance() {
        if (singleton == null) {
            singleton = new UserPrefs();

            for (String[] pair : defaults) {
                singleton.setPref(pair[0], pair[1]);
            }

            // TODO load from file and overwrite defaults
        }
        return singleton;
    }

    /** preferences are cascaded by default a.b.c defaults to a.b defaults to a */
    public String getPref(String name, String dflt) {
        String parent = name;
        String val = (String) this.parms.getAttr(name);

        while (val == null) {
            parent = parent.replaceFirst("\\..*$", "");
            if (parent == null || parent.length() <= 0 || parent.equals(name)) {
                break;
            }

            val = (String) this.parms.getAttr(parent);
        }

        return val == null ? dflt : val;
    }

    public boolean hasPref(String name) {
        return this.parms.isPopulated(name);
    }

    public void setPref(String name, String value) {
        this.parms.setAttr(name, value);
    }

    AttrAccess              parms        = new AttrAccessImpl();

    static final String[][] defaults     = { { "listvisual", AssetListVisual.LIST.toString() },
            { "listvisual.Movie", AssetListVisual.BRIEFLIST.toString() },
            { "listvisual.Site", AssetListVisual.LIST.toString() },
            { "listvisual.Series", AssetListVisual.DETAILS.toString() } };

    // TODO reflect configuration from here...
    static final String[][] defaultTypes = { { "listvisual", AssetListVisual.class.getName() } };
}
