/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.base.data;

/**
 * simple dynamic byte array
 * 
 * TODO make this class dissapear - use buffered inputs etc instead
 */
public class ByteArray {
    public static final int BUFF_QUOTA = 4096 + 4096; 
    private int             size;
    private byte[]          data;

    public ByteArray() {
        super();
        reset();
    }

    public void append(byte[] newData, int lenght) {
        if (data.length - size < lenght) { // expand it...
            int n = size + lenght;
            int r = n / BUFF_QUOTA;
            int m = n % BUFF_QUOTA;
            if (m != 0)
                r++;
            byte[] ext = new byte[r * BUFF_QUOTA];
            System.arraycopy(data, 0, ext, 0, size);
            data = ext;
        }
        System.arraycopy(newData, 0, data, size, lenght);
        size += lenght;
    }

    public String toString() {
        return new String(data, 0, size);
    }

    public void reset() {
        data = new byte[BUFF_QUOTA];
        size = 0;
    }
}
