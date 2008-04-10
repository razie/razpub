/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.events;

/**
 * a listener for events - must be registered with the PostOffice
 * 
 * @author razvanc99
 * 
 */
public interface EvListener {
    public String[] interestedIn();

    public void eatThis(String eventId, Object... args);
}
