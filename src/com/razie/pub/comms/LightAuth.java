/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.comms;


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
 * TODO AA should be custom per endpoint (same agent can use other agents with different AA
 * capabilities). Also, the AA can be negociated with the endpoints. Not sure how this will hookup
 * with the comms channel framework.
 * 
 * @author razvanc99
 * 
 */
public class LightAuth {
    protected static LightAuth impl;
    protected String           prefix = "";

    public LightAuth(String prefix) {
        // be nice
        if (prefix.startsWith("/"))
            this.prefix = prefix.substring(1);
        else
            this.prefix = prefix;
    }

    protected static LightAuth singleton() {
        if (impl == null)
            impl = new LightAuth("");
        return impl;
    }

    /** initialize the (for now static) AA used in this server/client */
    public static void init(LightAuth impl) {
        LightAuth.impl = impl;
    }

    /**
     * used by client to prepare the URL just before using it.
     * 
     * in basic HTTP communication, the only way to AA is by messing with the URL - add/remove, add
     * tokens parameters etc
     * 
     * @parm url the url to prepare, full form possibly including server:port
     * @return the URL to use, with AA info inserted
     */
    public static String wrapUrl(String url) {
        return singleton().wrapUrlImpl(url);
    }

    /**
     * used by server to the URL just before using it.
     * <p>
     * in basic HTTP communication, the only way to AA is by messing with the URL - add/remove, add
     * tokens parameters etc
     * <p>
     * THIS is tricky. If there is any AA information to save in a "session" on the server to be
     * used later when processing the URL, you do that any way you see fit. The only thing I will
     * help with right now is to make sure that the request is processed on the same thread that
     * made this call. Go think!
     * <p>
     * NOTE you don't have to authorize right now, since auth is supposed to be done per
     * service/asset/resource type. Just save whatever you need (token) in a session for this
     * request.
     * <p>
     * If you simply don't like this URL at all, then just throw AuthException right here
     * 
     * @parm url the url to prepare, starting with "/" and NOT including server:port
     * @return the URL to process, with AA info removed
     */
    public static String unwrapUrl(String url) throws AuthException {
        return singleton().unwrapUrlImpl(url);
    }

    /** default impl will just prefix the url */
    protected String wrapUrlImpl(String url) {
        String[] ss = url.split("://", 2);

        // be nice if the path is local
        if (ss.length == 1) {
            // if the paht was alreadey prepared, don't do it again...
            if (url != null && (url.startsWith(prefix) || url.startsWith("/" + prefix))) {
                return url;
            }

            return "/" + (prefix.length() > 0 ? (prefix) : "") + (url.startsWith("/") ? url : ("/" + url));
        }

        String[] ss2 = ss[1].split("/", 2);

        // if the paht was alreadey prepared, don't do it again...
        if (ss2[1] != null && ss2[1].startsWith(prefix)) {
            return url;
        }

        return ss[0] + "://" + ss2[0] + "/" + (prefix.length() > 0 ? (prefix + "/") : "") + ss2[1];
    }

    /**
     * default impl will just prefix the url
     * <p>
     * NOTE - derived classes should clear any session context info from previous calls here
     * <p>
     * If you simply don't like this URL at all, then just throw AuthException right here
     * <p>
     * NOTE one thing to remember is the path "/favicon.ico" - this is requested by the browsers
     * directly without asking for auth preparation or whatever, so...you must make sure you serve
     * that if you want to
     */
    protected String unwrapUrlImpl(String url) throws AuthException {
        if (url.equals("/favicon.ico"))
            return url;
        else {
            // TODO remove prefix
            return url;
        }
    }

    /**
     * authorize a request on a given channel. note that this is not agnostic about the particular
     * request/action, so it's only high-level authentication, really
     * 
     * @param string
     * @param socket
     * @throws AuthException
     */
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
