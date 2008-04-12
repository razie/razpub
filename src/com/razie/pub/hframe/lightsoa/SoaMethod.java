// ==========================================================================
// FILE INFO
// $Id: ScriptCallback.java,v 1.7 2007-09-26 16:14:58 sorinelc Exp $
//
// REVISION HISTORY
// * Based on CVS log
// ==========================================================================
package com.razie.pub.hframe.lightsoa;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mark the classes implementing callables from by the lightsoa framework
 * 
 * <pre>
 *        &#064;SoaService(name=&quot;network&quot;,descr=&quot;network services&quot;)
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
 * <b>Annotate with {@link SoaStreamable} if your method can stream a longer reply back. OR if you
 * need to change the reply mime-type and processing
 * 
 * <b>Annotate with {@link SoaNotHtml} if your method's return value should not be formatted as
 * html. This should not be used and may in fact be rmeoved, since you can just make the method
 * SoaStreamable.
 * 
 * @author razvanc99
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD })
@Inherited
public @interface SoaMethod {
    /** the value is a description of the method */
    String descr();

    /**
     * if defined, put here the permission to check - the framework will check the current
     * connection and user for this permission and throw AuthException if not permitted
     */
    String auth() default "";

    String[] args() default {};
}
