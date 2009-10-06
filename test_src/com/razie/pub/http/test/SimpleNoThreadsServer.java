/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.http.test;

import com.razie.pub.comms.AgentHandle;
import com.razie.pub.http.LightServer;
import com.razie.pub.http.SocketCmdHandler;

/**
 * this is a server that doesn't want to use threads, will handle one request at a time...not sure
 * why you'd do that, but hey...
 *
 * @author razvanc99
 */
public class SimpleNoThreadsServer extends LightServer {

    public SimpleNoThreadsServer(AgentHandle h, SocketCmdHandler... handlers) {
        super(Integer.parseInt(h.port), null);
        for (SocketCmdHandler cmd : handlers)  registerHandler(cmd);
    }

    /** if you have a special thread handling, overload this and use your own threads */
    @Override
    public void runReceiver(Receiver conn_c) {
        conn_c.run();
    }
}
