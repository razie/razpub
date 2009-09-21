/**
 * Razvan's code. 
 * Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.comms;

/**
 * just a placeholder for authorization denied exceptions
 * 
 * @author razvanc99
 * @version $Id$
 * 
 */
public class AuthException extends RuntimeException {
    
    public AuthException () {}
    
    public AuthException (String m) { super(m);}

}
