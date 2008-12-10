/**
 * Razvan's code. 
 * Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.http.test;

import com.razie.pub.http.LightServer;

/**
 * this is a server that doesn't want to use threads, will handle one request at a time...not sure
 * why you'd do that, but hey...
 *  $
 * @author razvanc99
 * 
 */
public class SampleNoThreadsServer extends LightServer {

    public SampleNoThreadsServer(int port) {
        super(port, null);
    }

    /** if you have a special thread handling, overload this and use your own threads */
    @Override
    public void runReceiver(Receiver conn_c) {
        conn_c.run();
    }
}
