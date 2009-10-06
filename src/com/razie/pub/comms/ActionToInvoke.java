/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.comms;

import java.net.MalformedURLException;
import java.net.URL;

import com.razie.pub.base.ActionItem;
import com.razie.pub.base.AttrAccess;
import com.razie.pub.base.ScriptContext;
import com.razie.pub.base.actions.BaseActionToInvoke;
import com.razie.pub.draw.Drawable;

/**
 * this is an instance of an action, meant to be invoked. It is prepared by someone and can be
 * executed on the spot OR presented to the user as a menu or some other invokable and invoked
 * later. It contains everything needed to invoke itself. It can be invoked in the same JVM or
 * remotely (from a web page etc).
 * 
 * it can be placed on a menu, web page, dialog as a button etc - it generally represents a menu
 * item or a button.
 *
 * TODO we need a provider mechanism, where depending on the target - i guess - the URLs are formatted differently. So, for instance, an AssetActionToInvoke for viewing a flickr image would be formatted differently from
 * 
 * @author razvanc99
 */
public class ActionToInvoke extends BaseActionToInvoke implements Cloneable, AttrAccess, Drawable {
    /**
     * constructor
     * 
     * @param target the prefix used depending on the drawing technology - for http, it's the URL to
     *        append to
     * @param item this is the action, contains the actual command name and label to display
     * @param pairs
     */
    public ActionToInvoke(String target, ActionItem item, Object... pairs) {
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
    public ActionToInvoke(ActionItem item, Object... pairs) {
        super(item, pairs);
    }

    public ActionToInvoke clone() {
        return new ActionToInvoke(this.target, this.actionItem.clone(), this.toPairs());
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

    /** this will take an URL and parse it into the same action object it created it... could be useful,
     * so we'll implement then - for now it documents the idea */
    public static ActionToInvoke fromActionUrl(String url) {
        // TODO FUTURE-implement
        return null;
    }

    /**
     * execute this action in a given context. The context must include me as well?
     * 
     * default implementation assumes i need to call an url and get the first line of response
     */
    public Object act(ScriptContext ctx) {
        try {
            URL url = new URL(this.makeActionUrl());
            return Comms.readUrl(url.toExternalForm(), LightAuth.impl.httpSecParms(url));
        } catch (MalformedURLException e) {
            throw new RuntimeException("while getting the command url: " + this.makeActionUrl(), e);
        }
    }
}
