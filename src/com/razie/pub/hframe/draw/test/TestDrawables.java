/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.draw.test;

import junit.framework.TestCase;

import com.razie.pub.hframe.base.log.Log;
import com.razie.pub.hframe.draw.Drawable;
import com.razie.pub.hframe.draw.Renderer;

public class TestDrawables extends TestCase {

    public void setUp() {
    }

    public void testToString() {
        // nasty way to get "13"... :))
        String s = (String) new Drawable.ToString(new Integer(13)).render(Renderer.Technology.ANY, null);
        assertTrue("13".equals(s));
    }

    public void testDrawError() {
        try {
            String s = new String("gg");
            if ("gg".equals(s))
                throw new IllegalArgumentException("you'd wish...");
        } catch (Exception e) {
            String s = (String) new Drawable.DrawError(e).render(Renderer.Technology.ANY, null);
            assertTrue(s.contains("IllegalArgumentException"));
            return;
        }

        assertTrue(false);
    }

    static final Log logger = Log.Factory.create(TestDrawables.class.getName());
}
