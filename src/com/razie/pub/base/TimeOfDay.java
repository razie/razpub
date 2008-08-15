/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base;

import java.util.Date;

import com.razie.pub.base.data.RangeSel;

/**
 * date-time related stuff
 * 
 * TODO detailed docs
 * 
 * @author razvanc
 * 
 */
public class TimeOfDay {

    public static String calcvalue() {return new TimeOfDay().value();}
    
    public TimeOfDay() {
        if (aivalues == null) {
            aivalues = new ActionItem[values.length];

            for (int i = 0; i < values.length; i++)
                aivalues[i] = new ActionItem(values[i]);
        }
    }

    public String[] tags() {
        return TAGS;
    }

    public ActionItem[] values() {
        return aivalues;
    }

    public String value() {
        Date dt = new Date();
        return new RangeSel<String>().rangeSel(dt.getHours(), rangei, values, dflt);
    }

    public ActionItem aivalue() {
        Date dt = new Date();
        return new RangeSel<ActionItem>().rangeSel(dt.getHours(), rangei, aivalues, aidflt);
    }

    static String[] TAGS   = { "time" };

    int[][]         rangei = { { 0, 6 }, { 7, 11 }, { 12, 19 }, { 20, 23 } };
    String[]        values = { "night", "morning", "day", "evening" };
    String          dflt   = "?";

    ActionItem[]    aivalues;
    ActionItem      aidflt = new ActionItem("?");

    String[][]      ranges = { { "0", "6", "night" }, { "7", "11", "morning" }, { "12", "19", "day" },
            { "20", "23", "evening" } };
}