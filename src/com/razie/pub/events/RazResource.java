/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.events;

import java.util.HashMap;
import java.util.Map;

import razie.base.ActionItem;

/**
 * a resource in this environment - registered in the naming system
 * 
 * resources are: database connections, JMS destinations, god knows what exactly
 * 
 * @version $Revision: 1.63 $
 */
public interface RazResource {
    public static final String     sCLASS = "Resource.razie";
    public static final ActionItem META   = new ActionItem(sCLASS, "/mutant/pics/IceAgeScrat.png");

    public String getResName();

    /**
     * environments manage and must be able to locate resources. To use resources, you need access
     * to a resource locator which is hooked up to a resource manager.
     * 
     * @version $Revision: 1.63 $
     * @author $Author: davidx $
     * @since $Date: 2005/04/01 16:22:12 $
     */
    public interface RazResourceLocator {
        public RazResource locate(String name);

        public void register(String name, RazResource res);
    }

    /** stupid default resource manager */
    public static class RazResourceManager implements RazResourceLocator {
        Map<String, RazResource> resources = new HashMap<String, RazResource>();

        // TODO 3- change this to simply find the smart asset resource...
        public RazResource locate(String name) {
            return resources.get(name);
        }

        public void register(String name, RazResource res) {
            resources.put(name, res);
        }
    }
}
