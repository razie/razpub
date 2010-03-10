/**  ____    __    ____  ____  ____,,___     ____  __  __  ____
 *  (  _ \  /__\  (_   )(_  _)( ___)/ __)   (  _ \(  )(  )(  _ \           Read
 *   )   / /(__)\  / /_  _)(_  )__) \__ \    )___/ )(__)(  ) _ <     README.txt
 *  (_)\_)(__)(__)(____)(____)(____)(___/   (__)  (______)(____/    LICENSE.txt
 */
package razie.base.scripting;


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
