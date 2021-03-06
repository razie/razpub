/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pubstage.actions;

import razie.base.ActionContext;
import razie.base.ActionItem;
import razie.base.ActionToInvoke;
import razie.base.AttrAccess;
import razie.base.BaseActionToInvoke;
import razie.draw.Drawable;

import com.razie.pub.comms.Agents;
import com.razie.pub.comms.LightAuthBase;

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
        super(Agents.me().url, item, pairs);
    }

    public ActionableSpec clone() {
        return new ActionableSpec(this.target, this.actionItem.clone(), this.toPairs());
    }

    /**
     * should not tie this to actual technology, but URLs are the most common form of invoking
     * actions */ public String makeActionUrl() {
        String url = target.endsWith("/") ? target : target + "/";
        url += actionItem.name;
        url = addToUrl(url);
        return LightAuthBase.wrapUrl(url);
    }

    /**
     * execute this action in a given context. The context must include me as well?
     * 
     * default implementation assumes i need to call an url and get the first line of response
     */
    public Object act(ActionContext ctx) {
         throw new UnsupportedOperationException("ERR_INVOKED_ACTIONABLE Cannot invoke a spec!" + this.makeActionUrl());
    }

      @Override
      public ActionToInvoke args(Object...args) {
        return new ActionableSpec(this.target, this.actionItem.clone(), args);
      }
}
