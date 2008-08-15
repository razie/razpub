/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.events;

import java.util.HashMap;
import java.util.Map;

import com.razie.pub.events.RazResource.RazResourceLocator;

/**
 * just a utility to simplify the client code as much as possible...not always optimal from a
 * runtime perspective, but...
 * 
 * @version $Revision: 1.63 $
 * @author $Author: davidx $
 * @since $Date: 2005/04/01 16:22:12 $
 */
public class PostOfficeNew extends PostOffice implements RazResourceLocator {
    Map<String, RazDestination> destinations     = new HashMap<String, RazDestination>();

    /** any context anywhere must have a default topic. This is used for all messages without topic. */
    public final static String  DFLT_TOPIC       = "razie/jms/topic/DefaultTopic";
    public final static String  DFLT_LOCAL_TOPIC = "razie/jms/topic/DefaultLocalTopic";

    /**
     * accept a message for a destination
     * 
     * @param destName the name of the destination
     * @param srcId the id of the source of the event. normally of the form: "agentNm"
     * @param eventId the id of the event
     * @param args optional name-value pairs, i.e. "someparm", "somevalue", ... IF you only want to
     *        pass an object which is the subject of the event, the convention is that it should be
     *        named "subject".
     */
    public static void send(String destName, String srcId, String eventId, Object... args) {
        RazDestination d = getInstance().destinations.get(destName);
        if (d != null) {
            d.send(srcId, eventId, args);
        } else
            throw new IllegalStateException("Destination not found: " + destName);
    }

    public static RazDestination destination(String destName) {
        return getInstance().destinations.get(destName);
    }

    /**
     * register a smart/reflective event listener
     * 
     * @param eventId the ID of the event to listen to
     * @param l the listener
     */
    public static void register(String destinationName, EvListener l) {
        RazDestination d = getInstance().destinations.get(destinationName);
        if (d != null) {
            d.register(l);
        } else
            throw new IllegalStateException("Destination not found: " + destinationName);
    }

    // TODO is this just for testing?
    public RazResource locate(String name) {
        return destinations.get(name);
    }

    public void register(String name, RazResource res) {
        destinations.put(name, (RazDestination) res);
    }
}
