/**  ____    __    ____  ____  ____,,___     ____  __  __  ____
 *  (  _ \  /__\  (_   )(_  _)( ___)/ __)   (  _ \(  )(  )(  _ \           Read
 *   )   / /(__)\  / /_  _)(_  )__) \__ \    )___/ )(__)(  ) _ <     README.txt
 *  (_)\_)(__)(__)(____)(____)(____)(___/   (__)  (______)(____/    LICENSE.txt
 */
package razie.base.scripting

import razie.base.{ ActionContext => AC }

/** wrapping java impotence... */
abstract class ScriptJSBase extends RazScript {

  override def eval(ctx: AC): RazScript.RSResult[AnyRef] =
    try {
      RazScript succ ieval (ctx)
    } catch {
      case e: Throwable => RazScript err e.toString
    }

  override def interactive(ctx: AC) = RazScript.RSUnsupported

  override def compile(ctx: AC) = RazScript.RSUnsupported

  override def lang = "js"

  protected[this] def ieval(s: AC): AnyRef
}

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Wrapper;

import razie.base.ActionContext;

/**
 * running javascripts
 * 
 * TODO pre-parse the thing
 * 
 * TODO use JSR 264 or whatever the thing is and ditch custom code...
 * 
 * @version $Revision: 1.13 $
 * @author $Author: razvanc $
 * @since $Date: 2007-05-03 13:31:49 $
 */
class ScriptJS(var script: String) extends ScriptJSBase {

  def setScript(s: String) { this.script = s; }
  def getScript(): String = this.script

  /** @return the statement */
  override def toString() = "js:\n" + this.getScript()

  /**
   * execute the script with the given context
   * 
   * @param c the context for the script
   */
  override def ieval(c: ActionContext): AnyRef = { // TODO how the heck can i hide this?
    var result: AnyRef = "";
    val ctx: Context = Context.enter();
    try {
      // Initialize the standard objects (Object, Function, etc.)
      // This must be done before scripts can be executed. Returns
      // a scope object that we use in later calls.
      val scope: Scriptable = ctx.initStandardObjects(null);

      c.foreach { (key, obj) => ScriptableObject.putProperty(scope, key, Context.toObject(obj, scope)) }
      // Now evaluate the script
      result = ctx.evaluateString(scope, script, "<cmd>", 1, null);

      // add JS variables back into ScriptContext
      val ids = scope.getIds();
      ids.foreach { id =>
        val key = String.valueOf(id);
        if ((c.getAttr(key) == null)) {
          var obj = scope.get(String.valueOf(id), scope);
          if (obj != null) {
            // first unwrap object from JS wrapper
            if (obj.isInstanceOf[Wrapper]) {
              obj = obj.asInstanceOf[Wrapper].unwrap()
            }
            // skip JS internal objects (like functions)
            if (!obj.getClass().getName().startsWith("org.mozilla")) {
              // logger.trace(3, "add var to ctx: " + ids[i] + "(" + obj.getClass().getName()
              // + ")");
              c.setAttr(id.toString(), obj);
            }
          }
        }
      }

      // convert to String
      if (result.isInstanceOf[Wrapper]) {
        result = result.asInstanceOf[Wrapper].unwrap()
      } else {
        result = Context.toString(result);
      }
    } catch {
      case e: Exception => throw new RuntimeException("While processing script: " + this.script, e);
    }
    finally {
      // Exit from the context.
      Context.exit();
    }
    result;
  }
}
