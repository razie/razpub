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

import com.razie.pub.base.ActionItem;
import com.razie.pub.http.SoaNotHtml;

/**
 * See SoaMethod - this is a sink for any other url the sink must have all args and the first arg is
 * actual method name
 * 
 * <code>public mysink (AttrAccess args);</code>
 * 
 * NOTE that sink methods must also have the SoaMethod annotation and are subject to all other
 * Soaxxx annotations. You should use SoaAllParms to make sure you really sink anything.
 * 
 * When using SoaAllParms, the original method name is available as SOA_METHODNAME.
 * 
 * @author razvanc99
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD })
@Inherited
public @interface SoaMethodSink {
    public static final String SOA_METHODNAME = "SOA_METHODNAME";
    
    /**
     * if defined, put here the permission to check - the framework will check the current
     * connection and user for this permission and throw AuthException if not permitted
     */
    PermType perm() default PermType.ANYBODY;

    /**
     * action type may dictate if it's ACT/GET/POST/PUT/DELETE
     * 
     * TODO did i actually end up using this?
     */
    ActionItem.ActionType actionType() default ActionItem.ActionType.A;

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

    /** auth types */
    enum AuthType {
        ANYBODY, FRIEND,
        /** shared same secret anywhere */
        SHAREDSECRET,
        /** only in-house */
        INHOUSE
    }
}
