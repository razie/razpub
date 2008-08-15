/**
 * Light SOA reflected wrapping of method calls
 * 
 * <p>
 * This package intends to eliminate much of the crap one has to deal with when writing any kind of
 * service accessible remotely. There's a myriad of protocols today and each has its own quirks.
 * 
 * <p>
 * Just mark a method to be called with the "@SoaMethod", listing the parameter names. Then wrap the
 * object in a binding wrapper and register that with whatever protocol server you need. So far I
 * have http and upnp bindings.
 * 
 * <p>
 * Currently, the parms are just Strings, but since they all get serialized somehow, what's the big
 * deal? Easy way to decouple code - simple event/messaging system.
 * 
 * <h2>Guide</h2>
 * 
 * Create a class and annotate (optional) with {@link SoaService}. Then the different methods you
 * want available remote, annotate with {@link SoaMethod}.
 * 
 * @version $Id: package-info.java,v 1.1 2007-10-02 11:54:36 razvanc Exp $
 */
package com.razie.pub.lightsoa;

