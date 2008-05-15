/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub;

/**
 * unknown exception - used temp in development when i can't find a better one
 * 
 * @author razvanc99
 */
public class UnknownRtException extends RuntimeException {

    /**
     * 
     */
    public UnknownRtException() {
    }

    /**
     * @param arg0
     */
    public UnknownRtException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public UnknownRtException(Throwable arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public UnknownRtException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}
