/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base;

import razie.base.ActionItem;

import com.razie.pub.resources.RazIcons;

/**
 * basic interface for action items (menu items, buttons etc). It has built-in capability for i18n
 * 
 * the icon properties are supposed to be resolved via the resources.RazRes class into a classpath
 * file
 * 
 * for more details, find the project razactionables and read the details there...
 * 
 * TODO 2-2 implement fully, with property bundle for the labels and actions, including tooltips etc
 * 
 * TODO the arguments are patched on...try to nice-fy via constructors maybe?
 * 
 * TODO how do we generically assign permissions to these ?
 * 
 * @author razvanc99
 * @version $Id$
 */
public class ActionItems {
    public static final ActionItem   WARN          = new ActionItem("warn", RazIcons.WARN.name());
    public static final ActionItem   VIEW_DETAILED = new ActionItem("view_detailed", RazIcons.VIEW_DETAILED.name());
    public static final ActionItem   VIEW_LIST     = new ActionItem("view_list", RazIcons.VIEW_LIST.name());
    public static final ActionItem   VIEW_THUMBS   = new ActionItem("view_thumbs", RazIcons.VIEW_THUMBS.name());
}
