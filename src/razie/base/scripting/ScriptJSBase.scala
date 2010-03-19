/**  ____    __    ____  ____  ____,,___     ____  __  __  ____
 *  (  _ \  /__\  (_   )(_  _)( ___)/ __)   (  _ \(  )(  )(  _ \           Read
 *   )   / /(__)\  / /_  _)(_  )__) \__ \    )___/ )(__)(  ) _ <     README.txt
 *  (_)\_)(__)(__)(____)(____)(____)(___/   (__)  (______)(____/    LICENSE.txt
 */
package razie.base.scripting

import com.razie.pub.WinExec

/** wrapping java impotence... */
abstract class ScriptJSBase extends RazScript {
  
  override def eval (ctx:ScriptContext) : RazScript.RSResult[AnyRef] = 
     try {
        RazScript succ ieval (ctx)
     } catch {
        case e : Throwable => RazScript err e.toString
     }
  
  override def interactive (ctx:ScriptContext) = RazScript.RSUnsupported

  override def compile (ctx:ScriptContext) = RazScript.RSUnsupported
  
  protected[this] def ieval (s:ScriptContext) : AnyRef
}
