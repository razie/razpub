/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.events;

import com.razie.pub.base.AttrAccess;

/**
 * a listener for events - must be registered with the PostOffice
 * 
 * TODO add complex filter conditions for registration
 * 
 * @author razvanc99
 */
public interface EvListener {
    /** @return the list of event types you're interested in or null/empty if interested in all */
    public String[] interestedIn();

    /** main method to be notified about an event */
    public void eatThis(String srcID, String eventId, AttrAccess info);
}
