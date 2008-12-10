/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base.actions;

import com.razie.pub.base.ScriptContext;

/**
 * the simplest interface for an actionable...
 * 
 * @author razvanc99
 */
public interface IActionable {
    /**
     * execute this action in a given context. The context must include me as well?
     * 
     * default implementation assumes i need to call an url and get the first line of response
     */
    public Object act(ScriptContext ctx);
}
