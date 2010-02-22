/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.events;

import java.lang.ref.WeakReference;

import razie.assets.AssetBase;
import razie.assets.AssetBaseImpl;
import razie.assets.AssetKey;
import razie.assets.AssetBriefImpl;

import razie.base.ActionItem;
import com.razie.pub.base.data.MemDb;

/**
 * a destination for messages - basically a queue or a topic...
 * 
 * @version $Revision: 1.63 $
 * @author $Author: davidx $
 * @since $Date: 2005/04/01 16:22:12 $
 */
public abstract class RazDestination extends AssetBaseImpl implements RazResource, AssetBase {
    public static final String                sCLASS    = "MsgDestination.razie";
    public static final ActionItem            META      = new ActionItem(sCLASS,
                                                                "/public/pics/IceAgeScrat.png");
    // public static Meta MMETA = new Meta(META, "", "", ResourceInventory.class.getName());

    static MemDb<String, WeakReference<EvListener>> listeners = new MemDb<String, WeakReference<EvListener>>();

    protected String                          resName;
    protected boolean                         distributed;
    protected QOS                             qos;

    public static enum QOS {
        VOLANS, PERSIST
    };

    protected RazDestination(String name, boolean distributed, RazDestination.QOS qos) {
        super(new AssetBriefImpl());
        this.resName = name;
        this.setKey(new AssetKey(sCLASS, resName));
        this.distributed = distributed;
        this.qos = qos;
    }

    /**
     * accept a message for this destination
     * 
     * @param srcId the id of the source of the event. normally of the form: "agentNm"
     * @param eventId the id of the event
     * @param args optional name-value pairs, i.e. "someparm", "somevalue", ... IF you only want to
     *        pass an object which is the subject of the event, the convention is that it should be
     *        named "subject".
     */
    public abstract void send(String srcId, String eventId, Object... args);

    public String getResName() {
        return resName;
    }

    /**
     * @return true if destination is distributed - implies all sent to it is replicated/distributed
     *         to cluster
     */
    public boolean isDistributed() {
        return this.distributed;
    }

    /**
     * register an event listener
     * 
     * @param l the listener
     */
    public void register(String eventId, EvListener l) {
        synchronized (listeners) {
            listeners.put(eventId, new WeakReference<EvListener>(l));
        }
    }

}
