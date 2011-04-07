/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pubstage.life;


/**
 * long-running threads should check the MTWrkRq.shutdownRequested() and throw this exception - it
 * should just flow right back to here...
 * 
 * @author razvanc99
 * 
 * @stereotype exception
 */
public class BeingDyingRtException extends RuntimeException {
    /**
     * Constructs a new exception with null as its detail message. The cause is not initialized, and
     * may subsequently be initialized by a call to Throwable.initCause(java.lang.Throwable).
     */
    public BeingDyingRtException() {
        super("Graceful thread stop requested...");
    }

}
