/**
 * Razvan's public code. 
 * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.hframe.http;

import java.util.Properties;

import com.razie.pub.hframe.base.log.Log;

/**
 * a command listener listens to commands, executes them and returns an object, normally a Drawable
 * that can be seen by client. This is called by the server to handle a particular command or path
 * 
 * <p>
 * This is a basic handler, no protocol knowledge. For an http implementation, see the LightCmdGET.
 * 
 * @author razvanc99
 * 
 */
public interface SocketCmdListener {
    /**
     * main callback from the server to handle a command send via the socket in the form "COMMAND
     * ARGS"
     * 
     * @param cmdName the command sent in, "GET" is used in http for instance
     * @param protocol the protocol - at this level it is not used actually
     * @param args the arguments passed in
     * @param parms
     * @param socket the command was received with, normaly you have to write something back...
     * @return the result of the command - or StreamedConsumedReply if the stream was filled
     * @throws AuthException
     */
    public Object executeCmdServer(String cmdName, String protocol, String args, Properties parms,
            MyServerSocket socket) throws AuthException;

    public String[] getSupportedCommands();

    /** simple echo listener */
    public static abstract class Impl implements SocketCmdListener {

        public Object executeCmdServer(String cmdName, String protocol, String args, Properties parms,
                MyServerSocket socket) throws AuthException {
            String m = "execute cmdName=" + cmdName + ", protocol=" + protocol + ", args=" + args;
            logger.log(m);
            return args;
        }

        public abstract String[] getSupportedCommands();

        static final Log logger = Log.Factory.create("", Impl.class.getName());
    }
}
