/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.assets;

import java.net.MalformedURLException;
import java.net.URL;

import com.razie.pub.agent.AgentHttpService;
import com.razie.pub.base.ActionItem;
import com.razie.pub.base.ScriptContext;
import com.razie.pub.comms.ActionToInvoke;
import com.razie.pub.comms.Agents;
import com.razie.pub.comms.Comms;
import com.razie.pub.comms.LightAuth;

/**
 * an action to invoke on an asset. See ActionToInvoke for details.
 * 
 * There's a target on top of the asset's own location, since the action can be invoked via a
 * proxy.Also, for remote controlled assets, the commands are invoked on the remote control not the
 * assets' own home.
 * 
 * @author razvanc99
 */
public class AssetActionToInvoke extends ActionToInvoke {
    protected AssetKey key;

    /**
     * constructor
     * 
     * TODO the target should be an object identifying the location to send the command to. For now
     * I use a simple URL
     * 
     * @param the target for this command - a location
     * @param item this is the action, contains the actual command name and label to display
     * @param pairs
     */
    public AssetActionToInvoke(String target, AssetKey key, ActionItem item, Object... pairs) {
        super(target, item, pairs);
        this.key = key;
    }

    /**
     * in this case the target is this agent
     * 
     * @param item this is the action, contains the actual command name and label to display
     * @param pairs
     */
    public AssetActionToInvoke(AssetKey key, ActionItem item, Object... pairs) {
        this(Agents.me().url, key, item, pairs);
    }

    @Override
    public AssetActionToInvoke clone() {
        return new AssetActionToInvoke(this.target, this.key, this.actionItem.clone(), this);
    }

    /**
     * i'm smart and can call local assets without a channel...
     * 
     * default implementation assumes i need to call an url and get the first line of response
     */
    @Override
    public Object act(ScriptContext ctx) {
        if (this.target == null || this.target.length() <= 0) {
            return AgentHttpService.getInstance().assetBinding.invokeLocal(key, actionItem.name, this);
        } else {
            try {
                URL url = new URL(this.makeActionUrl());
                return Comms.readUrl(url.toExternalForm());
            } catch (MalformedURLException e) {
                throw new RuntimeException("while getting the command url: " + this.makeActionUrl(), e);
            }
        }
    }

    @Override
    public String makeActionUrl() {
        String url = target.endsWith("/") ? target : target + "/";
        url += "asset/" + key.toUrlEncodedString() + "/";
        url += actionItem.name;
        url = addToUrl(url);
        return LightAuth.wrapUrl(url);
    }

    public static AssetActionToInvoke fromActionUrl(String url) {
        return null;
    }
}
