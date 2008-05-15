/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.base;

import com.razie.pub.http.LightAuth;

/**
 * this action is on a service - relevant when making up the URL: PREFIX/service/action/...
 * 
 * $
 * @author razvanc99
 * 
 */
public class ServiceActionToInvoke extends ActionToInvoke {
    String service;

    public ServiceActionToInvoke(String target, String service, ActionItem item, Object... pairs) {
        super(target, item, pairs);
        this.service = service;
    }

    public ServiceActionToInvoke(String service, ActionItem item, Object... pairs) {
        super(item, pairs);
        this.service = service;
    }

    public ServiceActionToInvoke clone() {
        return new ServiceActionToInvoke(this.target, this.service, this.actionItem.clone(), this.toPairs());
    }

    public String makeActionUrl() {
        String url = target.endsWith("/") ? target : target + "/";
        url += service + "/";
        url += actionItem.name;
        url = addToUrl(url);
        return LightAuth.wrapUrl(url);
    }

    // TODO implement
    public static ServiceActionToInvoke fromActionUrl(String url) {
        return null;
    }
}
