package com.razie.pub.webui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import razie.base.ScriptContext;

import com.razie.pub.comms.Comms;
import com.razie.pub.comms.SedFilter;

/**
 * decouple a mutant presentation artifact
 * 
 *TODO this entire page filtering thing - make it like streamable and not per line but per html tags, since it's logical, eh?
 * 
 * @author razvanc
 */
public abstract class PageMaker extends ScriptContext.Impl implements SedFilter {
    protected PageMaker(ScriptContext parent) {
        super(parent);
    }

    public abstract String page(String name);

    @Override
    public String filter(String line) {
        // TODO these replace only if they're alone on one line...

        // this is for serving HTML files that reference scripts (for now only pages)
        // TODO make this generic
        if (line.matches(".*\\$page\\..*")) {
            String a = line.substring(line.indexOf('$') + 1);
            return page(a);
        } else if (line.matches("<com.razie.include .*")) {
           // TODO this doesn't work - fix it using the SampleWebServer example...
            Matcher m = p1.matcher(line);
            int i = m.groupCount();
            String url = m.group(1);
            return Comms.readUrl(url);
        } else
            return line;
    }

     //example <com.razie.include url="/lightsoa/webui/serveClass?name=com.razie.pub.agent.PageServices"/>
    static Pattern p1 = Pattern.compile("<com.razie.include url=\"([^\"]*)\".*");
}
