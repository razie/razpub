/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.http;

/**
 * auth providers have, for me, several responsibilities - see the static methods below
 * 
 * this default will do nothing
 * 
 * NOTE that authentication providers are supposed to understand different remote targets and
 * properly massage the urls accordingly. for instance, one remote could be a lightsoa server while
 * another could be a bank that needs a session token appended etc. That mechanism is implemented in
 * the razagents framework, not here. Feel free to write your own...
 * 
 * 
 * @author razvanc99
 * 
 */
public class LightAuth {
    protected static LightAuth impl;
    protected String           prefix = "";

    public LightAuth(String prefix) {
        this.prefix = prefix;
    }

    protected static LightAuth singleton() {
        if (impl == null)
            impl = new LightAuth("");
        return impl;
    }

    public static void init(LightAuth impl) {
        LightAuth.impl = impl;
    }

    public static String prepareUrl(String url) {
        return singleton().prepareUrlImpl(url);
    }

    protected String prepareUrlImpl(String url) {
        String[] ss = url.split("://", 2);
        String[] ss2 = ss[1].split("/", 2);

        // if the paht was alreadey prepared, don't do it again...
        if (ss2[1] != null && ss2[1].startsWith(prefix)) {
            return url;
        }

        return ss[0] + "://" + ss2[0] + "/" + (prefix.length() > 0 ? (prefix + "/") : "") + ss2[1];
    }

    public static void authorize(String string, MyServerSocket socket) throws AuthException {
        String clientip = socket.client();

        // if Agents doesn't know myself, this should succeed, it's not a proper server but maybe
        // some sort of a test???

        // TODO this auth is really weak anyways...
        if (clientip.startsWith(Agents.getHomeNetPrefix()) || "127.0.0.1".equals(clientip)
                || Agents.agent(Agents.getMyHostName()) == null
                || clientip.equals(Agents.agent(Agents.getMyHostName()).ip)) {
            return;
        }
        throw new AuthException();
    }
}
