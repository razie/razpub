/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.base.test;

import java.util.Date;

import junit.framework.TestCase;

import com.razie.pub.base.ScriptContext;
import com.razie.pub.base.ScriptJS;
import com.razie.pub.base.TimeOfDay;

public class TestScripting extends TestCase {

   @Override
   public void setUp() {
   }

   public void testJs() {
      String script = "x = 'abc'; y = 3; function f(x){return x+1} f(7)";
         ScriptJS js = new ScriptJS(script);
         String res = js.eval(new ScriptContext.Impl()).toString();
         assertTrue(res.equals("8"));
   }
   
   public void testObj() {
      String script = "TimeOfDay.value()";
         ScriptJS js = new ScriptJS(script);

         ScriptContext ctx = new ScriptContext.Impl();
         ctx.setAttr("TimeOfDay", new TimeOfDay());

         String res = js.eval(ctx).toString();
         System.out.println(res);
         boolean found=false;
         for (String v : TimeOfDay.values)
            if (res.equals(v)) { found=true; break; }
         assertTrue(found);
         
         // script = "com.razie.playground.things.TimeOfDay.calcvalue()";
         // js = new JavaScript(script);
         // System.out.println(js.run(new ScriptContext.Impl()));
   }
}
