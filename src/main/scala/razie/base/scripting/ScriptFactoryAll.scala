/**  ____    __    ____  ____  ____,,___     ____  __  __  ____
 *  (  _ \  /__\  (_   )(_  _)( ___)/ __)   (  _ \(  )(  )(  _ \           Read
 *   )   / /(__)\  / /_  _)(_  )__) \__ \    )___/ )(__)(  ) _ <     README.txt
 *  (_)\_)(__)(__)(____)(____)(____)(___/   (__)  (______)(____/    LICENSE.txt
 */
package razie.base.scripting

/** add capability to support scala scripts 
 * 
 * Use like so: ScriptFactory.init (new ScriptFactoryAll (ScriptFactory.singleton, true))
 *
 * @author razvanc
 */
class ScriptFactoryAll (val other:ScriptFactory, val dflt:Boolean) extends ScriptFactory {

   override def makeImpl (lang:String, s:String) = {
      (lang, dflt) match {
         case ("scala", _) | ("text/scala", _) | (null, true) => new ScalaScript(s)
         case ("cmd", _) | ("text/shell", _) => new ScriptCmd(s)
         case ("js", _) | ("text/javascript", _) => new ScriptJS(s)
         case _ => 
           if (other != null) other.makeImpl (lang, s) 
           else throw new IllegalArgumentException ("Unsuported scrip lang: " + lang)
      }
   }
}
