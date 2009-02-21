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

import com.razie.pub.http.SoaNotHtml;

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
    PermType perm() default PermType.ANYBODY;

    String[] args() default {};

    /**
     * permissions
     * 
     * TODO need mapping to the AUTH types
     */
    enum PermType {
        /** highest permission: includes upgrades and code changes */
        ADMIN, /** allows control of play/preferences etc */
        CONTROL, /** just query and view */
        VIEW, /** what you want anybody to be able to do */
        ANYBODY
    }

    /**auth types*/
    enum AuthType {
        ANYBODY, FRIEND,
        /** shared same secret anywhere */
        SHAREDSECRET,
        /** only in-house */
        INHOUSE
    }
}
