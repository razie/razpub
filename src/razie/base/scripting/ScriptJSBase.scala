/**  ____    __    ____  ____  ____,,___     ____  __  __  ____
 *  (  _ \  /__\  (_   )(_  _)( ___)/ __)   (  _ \(  )(  )(  _ \           Read
 *   )   / /(__)\  / /_  _)(_  )__) \__ \    )___/ )(__)(  ) _ <     README.txt
 *  (_)\_)(__)(__)(____)(____)(____)(___/   (__)  (______)(____/    LICENSE.txt
 */
package razie.base.scripting

import razie.base.{ActionContext => AC}

/** wrapping java impotence... */
abstract class ScriptJSBase extends RazScript {
  
  override def eval (ctx:AC) : RazScript.RSResult[AnyRef] = 
     try {
        RazScript succ ieval (ctx)
     } catch {
        case e : Throwable => RazScript err e.toString
     }
  
  override def interactive (ctx:AC) = RazScript.RSUnsupported

  override def compile (ctx:AC) = RazScript.RSUnsupported
  
  protected[this] def ieval (s:AC) : AnyRef
}
