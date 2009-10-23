/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.agent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.razie.pub.base.NoStatics;
import com.razie.pub.base.exceptions.CommRtException;

/**
 * basic file services - read/write files...since agents can run all over the place, don't count on
 * proper file system functionality
 * 
 * @author razvanc
 * @version $Id$
 * 
 */
public class AgentFileService extends AgentService {
    StatusReport status = new StatusReport();
    String       path;

    public AgentFileService(String path) {
        this.path = path;

        AgentFileService singleton = (AgentFileService) NoStatics.get(AgentFileService.class);

        if (singleton != null) {
            status.lastError = new IllegalStateException("ERR_SVC_INIT AgentFileService already initialized!");
            throw (IllegalStateException) status.lastError;
        }

        NoStatics.put(AgentFileService.class, this);

        status.ok();
    }

    /** factory stub */
    public static AgentFileService getInstance() {
        AgentFileService f = (AgentFileService) NoStatics.get(AgentFileService.class);
        if (f == null)
            throw new IllegalStateException("AgentFileService not initialized!");
        return f;
    }

    /** default path for files - use this to store files */
    public String basePath() {
        return path;
    }

    protected void onShutdown() {
    }

    protected void onStartup() {
    }

    /** get status and report */
    public StatusReport status() {
        return status;
    }

    public static void copyStream(InputStream is, OutputStream fos) {
        try {
            byte[] buf = new byte[4 * 1024 + 1];
            int n;
            while ((n = is.read(buf, 0, 4096)) > 0) {
                fos.write(buf, 0, n);
            }
            fos.close();
            is.close();
        } catch (IOException e1) {
            throw new CommRtException("Copystream failed: ", e1);
        }
    }
}
