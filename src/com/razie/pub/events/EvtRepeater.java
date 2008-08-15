/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.events;

import com.razie.pub.base.AttrAccess;

/**
 * broadcast events to remote sources
 * 
 * @version $Revision: 1.63 $
 * @author $Author: davidx $
 * @since $Date: 2005/04/01 16:22:12 $
 */
public class EvtRepeater implements EvListener {
    String evType[] = new String[1];
    String target;

    /**
     * create an event forwarding for the given type
     * 
     * @param evType type of event to forward
     * @param target
     */
    public EvtRepeater(String evType, String target) {
        this.evType[0] = evType;
        this.target = target;
    }

    public void eatThis(String srcID, String eventId, AttrAccess info) {
        // TODO
    }

    public String[] interestedIn() {
        return this.evType;
    }

}
