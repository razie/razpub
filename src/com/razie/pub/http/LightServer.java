/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.http;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.razie.pub.base.ActionItem;
import com.razie.pub.base.AttrAccess;
import com.razie.pub.base.ThreadContext;
import com.razie.pub.base.data.HtmlRenderUtils;
import com.razie.pub.base.log.Exceptions;
import com.razie.pub.base.log.Log;
import com.razie.pub.comms.MyServerSocket;
import com.razie.pubstage.life.Worker;

/**
 * Simple/light socket server: spawns receiver threads to process connections. You can use it not
 * just for http...just derive and overwrite the makeReceiver() to use a fancier receiver
 * 
 * <p>
 * As is, you can hook it up with the {@link com.razie.pub.http.LightCmdGET} and implement simple
 * services or lightsoa bindings...that's usually enough. ROADMAP for usage: define your lightsoa
 * classes, and register them with the server and start the server...that's it.
 * 
 * <p>
 * See self-documented samples in {@link com.razie.pub.http.test.TestLightServer} which does
 * everything you can with the server.
 * 
 * <p>
 * derived from kieser.net sample
 * 
 * @author razvanc99
 * @version 1.0
 */
public class LightServer extends Worker {
    protected int           port;
    // TODO find an icon for this
    static final ActionItem ME             = new ActionItem("LightServer");
    protected ThreadContext mainContext;

    /** set this to something nonzero to limit the number of connections accepted in parallel */
    protected int           maxConnections = 0;

    ServerSocket            listener;

    /**
     * construct a server - will open the socket and you'll have to invoke run/process on a thread
     * to start accepting connections
     * 
     * @param port the port number to use
     * @param soaPrefix if not null, it will call the LightSoa bindings when sees this prefix...make
     *        sure it ends in a space. An example is "GET ", see the test
     */
    public LightServer(int port, ThreadContext mainContext) {
        super(ME);
        this.port = port;
        this.mainContext = mainContext;

        // not sure why i try 3 times, but...seems sturdier than not ;)
        for (int i = 0; i < 3; i++) {
            try {
                listener = new ServerSocket(port);
                break;
            } catch (IOException ioe) {
                Log.logThis("HTTP_ERR IOException on socket listen: ", ioe);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // dont care
                    e.printStackTrace();
                }
            }
        }
    }

    // Listen for incoming connections and handle them
    public void process() {
        int i = 0;

        while ((i++ < maxConnections) || (maxConnections == 0)) {
            try {
                // i guess i have to die now...
                if (listener.isClosed()) {
                    return;
                }
                Socket server = listener.accept();
                Receiver conn_c = makeReceiver(this, new MyServerSocket(server));
                Log.logThis("HTTP_CLIENT_IP: " + server.getInetAddress().getHostAddress());
                runReceiver(conn_c);
            } catch (IOException ioe) {
                if (ioe.getMessage().equals("socket closed"))
                    Log.logThis("socket closed...stopped listening: " + ioe.getMessage());
                else
                    Log.logThis("IOException on socket listen: " + ioe, ioe);
            }
        }
    }

    /** main factory method - overload this to create your own receivers */
    public Receiver makeReceiver(LightServer server, MyServerSocket socket) {
        return new Receiver(server, socket);
    }

    /** if you have a special thread handling, overload this and use your own threads */
    public void runReceiver(Receiver conn_c) {
        Thread t = new Thread(conn_c);
        t.setName("AgentReceiver" + t.getName());
        t.start();
    }

    @Override
    public void shutdown() {
        super.shutdown();
        try {
            this.listener.close();
        } catch (IOException ioe) {
            Log.logThis("IOException on socket close: " + ioe);
            ioe.printStackTrace();
        }
    }

    /**
     * receiver: spawned by the server to handle an incoming connection
     * 
     * <p>
     * This default receiver will read lines until it finds a non-empty and then will find the
     * command listener that can handle the first word and invoke it. The result will be written
     * back to the socket. This is good for simple command listeners, including ftp, http...
     * 
     * TODO derive from MTWrkRq
     */
    protected class Receiver implements Runnable {
        protected MyServerSocket socket;
        protected LightServer    server;

        protected Receiver(LightServer server, MyServerSocket socket) {
            this.server = server;
            this.socket = socket;
        }

        public void run() {
            if (mainContext != null)
                mainContext.enter();

            String input = "";

            try {
                // TODO don't know why but i can't use this compliant reader...
                // BufferedReader in = new BufferedReader(new
                // InputStreamReader(socket.getInputStream()));

                // Get input from the client
                DataInputStream in = new DataInputStream(socket.getInputStream());
                PrintStream out = new PrintStream(socket.getOutputStream());

                // TODO why do i flush empty lines?
                // consume the input until a non-emtpty line
				while (input == null || input.length() <= 0) {
					input = in.readLine();
					if (input == null) {
					   logger.alarm("ERR_SOCKET_EOF socket had EOF before any byte...dropping connection");
					   return;
					}
				}

                logger.trace(3, "INPUT:\n", input);

                // finish reading the input stream until there's nothing else...this will get
                // the entire command
                String rest = in.readLine();
                AttrAccess httpattrs = new AttrAccess.Impl();
                while (rest != null && rest.length() > 0) {
                   String s[] = rest.split(": ");
                   httpattrs.setAttr(s[0], s[1]);
                    rest = in.readLine();
                }

					ThreadContext.instance().setLocalAttr("httpattrs", httpattrs);
					logger.trace(3, "   HTTPATTRSINPUT:\n" + httpattrs.toString());

                handleInputLine(out, input, httpattrs);

            } catch (Exception ioe) {
                // must catch all exceptions to avoid screwing up something bad...don't remember
                // what
                logger.log("IOException on socket listen: ", ioe);
                ioe.printStackTrace();
            } finally {
				try {
               socket.close();
            } catch (IOException e) {
                Log.logThis("", e);
            }
                ThreadContext.exit();
            }
        }

        protected void handleInputLine(PrintStream out, String input, AttrAccess httpattrs) {
            String cmd;
            String args;
            // will be null if no listeners found...
            Object reply = null;

            // Now write to the client
            if (input != null) {
                if (input.indexOf(' ') > 0) {
                    cmd = input.substring(0, input.indexOf(' '));
                    args = input.substring(input.indexOf(' ') + 1);
                } else {
                    cmd = input;
                    args = "";
                }

                // TODO document these
                httpattrs.set("lightsoa.methodname", cmd);
                httpattrs.set("lightsoa.path", args);
                
                Log.logThis("HTTP_CLIENT_RQ: " + cmd + " args=" + args);

                for (SocketCmdListener c : server.getListeners()) {
                    for (String s : c.getSupportedCommands()) {
                        if (cmd.equals(s)) {
                            logger.trace(1, "HTTP_FOUND_LISTENER: " + c.getClass().getName());
                            try {
                                reply = c.executeCmdServer(cmd, "", args, new Properties(), socket);
                            } catch (Throwable e) {
                                logger.log("ERR_HTTP_RECEIVER_EXCEPTION: ", e);
                                reply = HtmlRenderUtils.textToHtml(Exceptions.getStackTraceAsString(e));
                            }
                            break;
                        }
                    }
                }
            } else {
                logger.log("BAD_INPUT: null");
            }

            if (reply != null) {
                if (!(reply instanceof StreamConsumedReply))
                    out.print(reply);
                // out.println(reply);
                Log.logThis("HTTP_CLIENT_SERVED");
            } else {
                logger.trace(3, "command listener returned nothing...");
            }
        }

    }

    static final Log logger = Log.Factory.create(Receiver.class.getSimpleName());

    public void registerCmdListener(SocketCmdListener c) {
        getListeners().add(c);
    }

    public void removeCmdListener(SocketCmdListener c) {
        getListeners().remove(c);
    }

    /**
     * @return the listeners
     */
    public List<SocketCmdListener> getListeners() {
        return listeners;
    }

    private List<SocketCmdListener> listeners = new ArrayList<SocketCmdListener>();
}
