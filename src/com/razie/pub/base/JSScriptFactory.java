/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base;

/**
 * minimal factory to decouple scripting
 * 
 * TODO use JSR 264 or whatever the thing is and ditch custom code...
 * 
 * @author razvanc
 */
public class JSScriptFactory extends ScriptFactory {

    @Override
    public RazScript makeImpl(String lang, String script) {
        return new ScriptJS(script);
    }
}
