/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.comms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


/**
 * a wrapper for connections (from/to clients) so i can add functionality as i please and not depend
 * on some freaky Java API
 * 
 * @author razvanc99
 * @version $Id$
 * 
 */
public class MyServerSocket extends CommChannel {
    public Socket server;

    public MyServerSocket(Socket s) {
        this.server = s;
        this.from = new SocketEndPoint(server);
        this.to = null;// TODO this is me...
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
}
