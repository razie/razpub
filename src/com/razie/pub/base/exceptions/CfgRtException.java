/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.base.exceptions;

/**
 * configuration related exception
 */
@SuppressWarnings("serial")
public class CfgRtException extends RuntimeException {

    /**
     * 
     */
    public CfgRtException() {
    }

    /**
     * @param arg0
     */
    public CfgRtException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public CfgRtException(Throwable arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public CfgRtException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}
