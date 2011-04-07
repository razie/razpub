/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.events.test;

import junit.framework.TestCase;

import com.razie.pub.base.NoStatics;
import com.razie.pub.base.log.Log;
import com.razie.pub.events.PostOffice;
import com.razie.pub.events.RazQueue;
import com.razie.pub.events.RazTopic;
import com.razie.pub.events.RazDestination.QOS;
import com.razie.pub.events.RazResource.RazResourceLocator;

/**
 * test the event/messaging system
 * 
 * @author razvanc99
 * 
 */
public class TestEvents extends TestCase {
    RazResourceLocator            environment;
    NoStatics                     curContext;

    public final static String    TEST_QUEUE  = "razie/jms/queue/TestQueue";

    protected final static String TESTEVENT1Q = "TESTEVENT1Q";
    protected final static String TESTEVENT1T = "TESTEVENT1T";

    public void setUp() {
        // IMPORTANT - services must mount their resources for each context / agent instance
        curContext = NoStatics.instance();
        environment = PostOffice.getInstance();

        environment.register(TEST_QUEUE, new RazQueue(TEST_QUEUE, false, QOS.VOLANS));
    }

    public void testSendMessageQ() {
        NoStatics.enter(curContext);
        SampleListener res = new SampleListener(TESTEVENT1Q, "msg", "msg1");
        PostOffice.register(TEST_QUEUE, res);

        RazQueue q = (RazQueue) environment.locate(TEST_QUEUE);
        q.send("?", TESTEVENT1Q, "msg", "msg1");

        assertTrue (res.count == 1);
    }

    public void testSendMessageT() {
        NoStatics.enter(curContext);
        SampleListener res = new SampleListener(TESTEVENT1T, "msg", "msg1");
        PostOffice.register(PostOffice.DFLT_TOPIC, res);

        RazTopic t = (RazTopic) environment.locate(PostOffice.DFLT_TOPIC);
        t.send("?", TESTEVENT1T, "msg", "msg1");

        assertTrue (res.count == 1);
    }

    //send two but only one should be received
    public void testSendMessageTFilter() {
        NoStatics.enter(curContext);
        SampleListener res = new SampleListener(TESTEVENT1T, "msg", "msg1");
        PostOffice.register(PostOffice.DFLT_TOPIC, res);

        RazTopic t = (RazTopic) environment.locate(PostOffice.DFLT_TOPIC);
        t.send("?", TESTEVENT1T, "msg", "msg1");
        t.send("?", "x"+TESTEVENT1T, "msg", "msg1");

        assertTrue (res.count == 1);
    }

    public void testSendMessage() {
        PostOffice.send(TEST_QUEUE, "?", TESTEVENT1Q, "msg", "msg2");
        PostOffice.send(PostOffice.DFLT_TOPIC, "?", TESTEVENT1T, "msg", "msg2");
    }

    static final Log logger = Log.factory.create(TestEvents.class.getName());
}
