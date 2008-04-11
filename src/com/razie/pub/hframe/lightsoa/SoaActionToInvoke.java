package com.razie.pub.hframe.lightsoa;

import com.razie.pub.hframe.base.ActionItem;
import com.razie.pub.hframe.base.ActionToInvoke;
import com.razie.pub.hframe.http.Agents;

/**
 * this is an instance of an action, meant to be invoked on a service. It is prepared by someone and
 * can be executed on the spot OR presented to the user as a menu and invoked later.
 * 
 * $
 * @author razvanc99
 * 
 */
public class SoaActionToInvoke extends ActionToInvoke {
    public String url;
    public String service;

    public SoaActionToInvoke(String url, String service, ActionItem item, Object... pairs) {
        super(item, pairs);
        this.url = url;
        this.target = url + "/mutant";
        this.service = service;
    }

    public SoaActionToInvoke(String service, ActionItem item, Object... pairs) {
        this(Agents.agent(Agents.getMyHostName()).url, service, item, pairs);
    }

    public SoaActionToInvoke clone() {
        return new SoaActionToInvoke(this.url, this.service, this.actionItem.clone(), this.toPairs());
    }
}
