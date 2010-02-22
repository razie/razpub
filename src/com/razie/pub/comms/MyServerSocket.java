/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.comms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import razie.base.AttrAccess;
import razie.draw.HttpDrawStream;

/**
 * a wrapper for connections (from/to clients) so i can add functionality as i please and not depend
 * on some freaky Java API
 * 
 * @author razvanc99
 */
public class MyServerSocket extends CommChannel {
    public Socket server;
    AttrAccess httpArgs;

    public MyServerSocket(Socket s) {
        super(LightAuth.instance().iauthorize(s, null, null));
        this.server = s;
        this.from = new SocketEndPoint(server);
        this.to = null;// TODO this is me...
    }

    public void setHttp(String url, AttrAccess httpArgs) {
       auth = LightAuth.instance().iauthorize(server, url, httpArgs);
       this.httpArgs=httpArgs;
    }
    
    public AttrAccess getHttp() {
      return this.httpArgs; 
    }
    
    /**
     * authorize a request on a given channel. note that this is not agnostic about the particular
     * request/action, so it's only high-level authentication, really
     * 
     * @param perm
     * @throws AuthException
     */
    public void auth(PermType perm) throws AuthException {
        LightAuth.AuthType minAuth = LightAuth.mapAuth(perm);

        if (level.get(getAuth()) >= level.get(minAuth))
            return;

        throw new AuthException("Not enough karma: "+getAuth() + " < "+minAuth + " YOU ARE: " + this.from.toString());
        
    }

    public InputStream getInputStream() throws IOException {
        return server.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return server.getOutputStream();
    }

    public String client() {
        return server.getInetAddress().getHostAddress();
    }

    public void close() throws IOException {
        server.close();
    }

    static Map<LightAuth.AuthType, Integer> level = new HashMap<LightAuth.AuthType, Integer>();
    static {
        level.put(LightAuth.AuthType.ANYBODY, 0);
        level.put(LightAuth.AuthType.FRIEND, 1);
        level.put(LightAuth.AuthType.INCLOUD, 2);
        level.put(LightAuth.AuthType.SHAREDSECRET, 3);
        level.put(LightAuth.AuthType.INHOUSE, 4);
    }
    
    public HttpDrawStream mkStream () { return mkStream (true); }
    public HttpDrawStream mkStream (boolean shouldClose) {
       try {
         return new HttpDrawStream (this.from, this.getOutputStream(), shouldClose);
      } catch (IOException e) {
         throw new RuntimeException (e);
      }
    }
}
