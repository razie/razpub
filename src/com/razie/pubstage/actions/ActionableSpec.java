/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pubstage.actions;

import com.razie.pub.base.ActionItem;
import com.razie.pub.base.AttrAccess;
import com.razie.pub.base.ScriptContext;
import com.razie.pub.base.actions.BaseActionToInvoke;
import com.razie.pub.comms.LightAuth;
import com.razie.pub.draw.Drawable;

/**
 * specifies an action that can be invoked. The list of attributes only contains name/type and
 * possibly default values
 * 
 * @author razvanc99
 */
public class ActionableSpec extends BaseActionToInvoke implements AttrAccess, Drawable, Cloneable {
    /**
     * constructor
     * 
     * @param target the prefix used depending on the drawing technology - for http, it's the URL to
     *        append to
     * @param item this is the action, contains the actual command name and label to display
     * @param pairs
     */
    public ActionableSpec(String target, ActionItem item, Object... pairs) {
        super(target, item, pairs);
    }

    /**
     * constructor
     * 
     * @param target the prefix used depending on the drawing technology - for http, it's the URL to
     *        append to
     * @param item this is the action, contains the actual command name and label to display
     * @param pairs
     */
    public ActionableSpec(ActionItem item, Object... pairs) {
        super(item, pairs);
    }

    public ActionableSpec clone() {
        return new ActionableSpec(this.target, this.actionItem.clone(), this.toPairs());
    }

    /**
     * should not tie this to actual technology, but URLs are the most common form of invoking
     * actions
     */
    public String makeActionUrl() {
        String url = target.endsWith("/") ? target : target + "/";
        url += actionItem.name;
        url = addToUrl(url);
        return LightAuth.wrapUrl(url);
    }

    /**
     * execute this action in a given context. The context must include me as well?
     * 
     * default implementation assumes i need to call an url and get the first line of response
     */
    public Object act(ScriptContext ctx) {
         throw new UnsupportedOperationException("ERR_INVOKED_ACTIONABLE Cannot invoke a spec!" + this.makeActionUrl());
    }
}
