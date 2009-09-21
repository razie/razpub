/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.events;

import com.razie.pub.base.AttrAccess;
import java.util.Map;

/**
 * all event types must derive from here - easy way to find all event types in a large distributed system
 * 
 * @author razvanc99
 * 
 */
public interface EvTypes {
   public AttrAccess getAttributes();

   // TODO use this...
   static public class Impl implements EvTypes {
           protected AttrAccess aa;

           public AttrAccess getAttributes() {
                   if (aa == null) aa=new AttrAccess.Impl();
                   return aa;
           }

   }
}
