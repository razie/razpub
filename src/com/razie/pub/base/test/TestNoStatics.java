/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.base.test;

import junit.framework.TestCase;

import com.razie.pub.base.NoStatic;
import com.razie.pub.base.ThreadContext;
import com.razie.pub.base.log.Log;

/**
 * test the light server
 * 
 * @author razvanc99
 */
public class TestNoStatics extends TestCase {
    ThreadContext            t      = new ThreadContext();
    static String            failed = null;
    static NoStatic<Boolean> STATIC = new NoStatic<Boolean>("testing", new Boolean(true));

    // setup two statics in different threads and make sure they work
    public void testNoStatics() throws InterruptedException {
        // we test this context in the second thread
        t.enter();
        failed = null;

        // the value is set in a differnet context than default...for t2
        STATIC.setThreadValue(new Boolean(false));

        // t1 uses the default
        Thread t1 = new Thread() {
            @Override
            public void run() {
                boolean b = STATIC.value();

                if (!b)
                    failed = "value changed!!!";
            }
        };

        // t2 overwrites default
        Thread t2 = new Thread() {
            @Override
            public void run() {
                t.enter();
                boolean b = STATIC.value();
                if (b)
                    failed = "value DIDNT changed!!!";
                t.exit();
            }
        };

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        assertFalse(failed, failed != null);
    }

    static final Log logger = Log.Factory.create(TestNoStatics.class.getName());
}
