/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.lightsoa;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mark the methods callable from by the lightsoa framework, on a service class
 * 
 * asset ulrs are <code>PREFIX/SERVICE/METHOD?parms=values&</code>
 * 
 * <pre>
 *        class ClassA {
 *            ...
 *            &#064;SoaMethod (descr=&quot;the name of the component&quot;)
 *            public SoaResponse getName() {
 *            }
 *            ...
 *            &#064;SoaMethod (descr=&quot;the name of the component&quot;, args = {&quot;name&quot;})
 *            public SoaResponse setName(String name) {
 *            }
 *            ...
 *        }
 * </pre>
 * 
 * These methods can take arguments - limit yourself to String arguments for now. These however will
 * be escaped properly and unescaped properly by each binding, that's a requirement for te binding
 * since the method doesn't know which binding will wrap it.
 * 
 * If a protocol (UPNP) supports returning mutliple arguments, please return a SoaResponse
 * 
 * TODO can be used to figure out name clashes, register services automatically etc
 * 
 * TODO use the bindings...currently it's not used
 * 
 * @author razvanc99
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE })
@Inherited
public @interface SoaService {
    /** the name of the service */
    String name();

    /** the value is a description of the method */
    String descr();

    /** can limit the bindings */
    String[] bindings() default {};
}
