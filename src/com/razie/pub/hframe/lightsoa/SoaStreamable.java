//==========================================================================
// FILE INFO
// $Id: ScriptCallback.java,v 1.7 2007-09-26 16:14:58 sorinelc Exp $
//
// REVISION HISTORY
// * Based on CVS log
//==========================================================================
package com.razie.pub.hframe.lightsoa;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mark the SoaMethod that it can stream. These methods will always be void - they will stream any
 * responses directly into the object stream.
 * 
 * This is extremely useful when you deal with large volume of items (videos) and you don't want to
 * have the user wait until a search is over...it of course depends on how smart the client is, but
 * this allows you to be smart...
 * 
 * <pre>
 *        class ClassA {
 *            ...
 *            &#064;SoaMethod (descr=&quot;the name of the component&quot;, args = {&quot;name&quot;})
 *            public SoaResponse setName(String name) {
 *            }
 *            ...
 *            &#064;SoaMethod (descr=&quot;the name of the component&quot;, args = {&quot;name&quot;})
 *            &#064;SoaStreamable
 *            public void setName(DrawStream out, String name) {
 *            }
 *            ...
 *        }
 * </pre>
 * 
 * @author razvanc99
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD })
@Inherited
public @interface SoaStreamable {
}
