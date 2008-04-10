//==========================================================================
// FILE INFO
// $Id: GracefulShutdownRtException.java,v 1.1 2007-01-15 16:13:51 razvanc Exp $
//
//    Copyright (c) 2001 Sigma Systems Group (Canada) Inc.
//    All rights reserved.
//
// REVISION HISTORY
// * Based on CVS log
//==========================================================================

package com.razie.pubstage.life;


/**
 * long-running threads should check the MTWrkRq.shutdownRequested() and throw this exception - it
 * should just flow right back to here...
 * 
 * @see com.sigma.hframe.jmt.LightMTWrkRq#die()
 * 
 * $
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
