/**  ____    __    ____  ____  ____,,___     ____  __  __  ____
 *  (  _ \  /__\  (_   )(_  _)( ___)/ __)   (  _ \(  )(  )(  _ \           Read
 *   )   / /(__)\  / /_  _)(_  )__) \__ \    )___/ )(__)(  ) _ <     README.txt
 *  (_)\_)(__)(__)(____)(____)(____)(___/   (__)  (______)(____/    LICENSE.txt
 */
package razie.base.scripting

import razie.actionables.util.WinExec
import razie.base.{ActionContext => AC}

/** a windows cmd script */
class ScriptCmd (val script:String) extends RazScript {

    /** @return the statement */
    override def toString() = "cmd:\n" + script

    /**
     * execute the script with the given context
     * 
     * @param c the context for the script
     */
   override def eval(c:AC) : RazScript.RSResult[Any] = {
      try {
    	   RazScript succ WinExec.execAndWait (script, "")
        } catch {
          case e:Exception =>
            RazScript.err("While processing script: " + this.script + " Exception: " + e);
        }
    }
    
  override def interactive (ctx:AC) : RazScript.RSResult[Any] = RazScript.RSUnsupported ("todo")
  override def compile (ctx:AC) : RazScript.RSResult[Any] = RazScript.RSUnsupported ("todo")
}

object ScriptCmdTestApp extends Application{
    var script = "cmd /C dir";
    var js = new ScriptCmd(script);
    System.out.println(js.eval(ScriptFactory.mkContext()));
}
