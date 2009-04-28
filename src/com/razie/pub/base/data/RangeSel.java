/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base.data;


/**
 * date-time related stuff
 * 
 * TODO detailed docs
 * 
 * @author razvanc
 */
public class RangeSel<T> {

    public T rangeSel(int hours, int[][] range, T[] values, T dflt) {
        for (int i = 0; i < range.length; i++)
            if (hours >= range[i][0] && hours <= range[i][1])
                return values[i];
        return dflt;
    }

}
