/**
 * Razvan's public code.
 * 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.events.test;

import com.razie.pub.base.AttrAccess;
import com.razie.pub.events.EvListener;

/**
 * our sample listener will simply assert if the event it gets is different than the event passed in
 * at creation time
 * 
 * @version $Id: 1.63 $
 */
public class SampleListener implements EvListener {
    String     ev;
    AttrAccess aa;
    int        count     = 0;

    public SampleListener(String ev, Object... parms) {
        aa = new AttrAccess.Impl(parms);
        this.ev=ev;
    }

    public void eatThis(String srcID, String eventId, AttrAccess info) {
        assert (eventId.equals(ev));
        assert (aa.equals(info));
        count++;
    }

    public String[] interestedIn() {
        String[] s = new String[1];
        s[0] = ev;
        return s;
    }
}
