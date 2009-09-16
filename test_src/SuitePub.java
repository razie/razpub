/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */


import junit.framework.Test;
import junit.framework.TestSuite;

import com.razie.pub.agent.test.SuitePubAgent;
import com.razie.pub.base.test.SuitePubBase;
import com.razie.pub.draw.test.SuitePubDraw;
import com.razie.pub.events.test.SuitePubEvents;
import com.razie.pub.http.test.SuitePubHttp;
import com.razie.pub.lightsoa.test.SuitePubLightsoa;

/**
 * suite to run all pub tests
 * 
 * @author razvanc99
 */
public class SuitePub extends TestSuite {
    public static Test suite() {
        TestSuite result = new TestSuite(SuitePub.class.getName());

        result.addTest(SuitePubBase.suite());
        result.addTest(SuitePubDraw.suite());
        result.addTest(SuitePubEvents.suite());
        result.addTest(SuitePubHttp.suite());
        result.addTest(SuitePubLightsoa.suite());
        result.addTest(SuitePubAgent.suite());

        return result;
    }

}
