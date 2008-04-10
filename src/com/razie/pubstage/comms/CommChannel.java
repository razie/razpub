/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pubstage.comms;

import java.net.URL;

/**
 * a communication channel serves two endpoints.
 * 
 * it can serve multiple streams between the two end-points. can have state and not reconnect. it
 * can reconnect automatically etc. The idea is that the client code doesn't care about the actual
 * communication channel technology and optimizations/implementation, i.e. SCTP vs TCP. $
 * 
 * @author razvanc99
 * 
 */
public class CommChannel {
    ChannelEndPoint from, to;

    /**
     * need to figure out the commonalities of channel end-points. at the very least, these will be
     * abstract logical constructs and have nothing to do with phisical
     * node/hardware/software/services/network deployment.
     */
    public static class ChannelEndPoint {
    }

    /**
     * simple URL based channel end=point. The channel is only interested in the server:port part
     */
    public static class URLChannelEndPoint extends ChannelEndPoint {
        URL    url;
        String surl;

        public URLChannelEndPoint(String surl) {
            super();
            this.surl = surl;
        }

        public URLChannelEndPoint(URL url) {
            super();
            this.url = url;
        }
    }

    /**
     * simple URL based channel end=point. The channel is only interested in the server:port part
     */
    public static class MutantChannelEndPoint extends ChannelEndPoint {
        URL    url;
        String surl;

        public MutantChannelEndPoint(String surl) {
            super();
            this.surl = surl;
        }

        public MutantChannelEndPoint(URL url) {
            super();
            this.url = url;
        }
    }
}
