// ==========================================================================
// $Id: Svc.java,v 1.63 2005/04/01 16:22:12 davidx Exp $
// (@) Copyright Sigma Systems (Canada) Inc.
// ==========================================================================
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
        super(port);
    }

    @Override
    public void runReceiver(Receiver conn_c) {
        conn_c.run();
    }
}
