/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.hframe.base;

import java.net.URL;

import com.razie.pub.hframe.resources.RazIcons;

/**
 * basic interface for action items (menu items, buttons etc). It has built-in capability for i18n
 * 
 * the icon properties are supposed to be resolved via the resources.RazRes class into a classpath
 * file
 * 
 * TODO implement fully, with property bundle for the labels and actions, including tooltips etc
 * 
 * @author razvanc99
 * @version $Id$
 */
public class ActionItem {
    public static final ActionItem   PLAY          = new ActionItem("play", RazIcons.PLAY);
    public static final ActionItem   STREAM        = new ActionItem("stream", RazIcons.PLAY);
    public static final ActionItem   WARN          = new ActionItem("warn", RazIcons.WARN);
    public static final ActionItem   VIEW_DETAILED = new ActionItem("view_detailed", RazIcons.VIEW_DETAILED);
    public static final ActionItem   VIEW_LIST     = new ActionItem("view_list", RazIcons.VIEW_LIST);
    public static final ActionItem   VIEW_THUMBS   = new ActionItem("view_thumbs", RazIcons.VIEW_THUMBS);

    public static final ActionItem[] NOACTIONS     = {};

    /**
     * overwrite the label of another action item
     * 
     * @param name action name
     * @param newLabel
     */
    public ActionItem(ActionItem other, String newLabel) {
        this.name = other.name;
        this.iconProp = other.iconProp;
        this.label = newLabel;
    }

    /**
     * @param name action name
     * @param icon one of standard icons
     */
    public ActionItem(String name, RazIcons icon) {
        this.name = this.label = name;
        this.iconProp = icon.name();
    }

    /**
     * @param name action name
     * @param icon an icon property - icons are supposed to be mapped via the RazIconRes class
     */
    public ActionItem(String name, String icon) {
        this.name = this.label = name;
        this.iconProp = icon;
    }

    /** this assumes the icon code is the same as the action name */
    public ActionItem(String name) {
        this.name = this.label = name;
        this.iconProp = name;
    }

    /**
     * the icon properties are supposed to be resolved via the resources.RazRes class into a
     * classpath file
     */
    public String getIconProp() {
        return this.iconProp;
    }

    public String getLabel() {
        return this.label;
    }

    public String getTooltip() {
        return this.tooltip;
    }

    public URL getHelpUrl() {
        return this.helpUrl;
    }

    @Override
    public ActionItem clone() {
        ActionItem i = new ActionItem(this.name, this.iconProp);
        i.label = this.label;
        i.tooltip = this.tooltip;
        i.helpUrl = this.helpUrl;
        return i;
    }

    public String name;
    public String iconProp = RazIcons.UNKNOWN.name();
    public String label;
    public String tooltip;
    public URL    helpUrl;
}
