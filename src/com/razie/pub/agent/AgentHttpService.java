/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.agent;

import java.util.List;

import com.razie.pub.agent.Agent;
import com.razie.pub.agent.AgentService;
import com.razie.pub.base.NoStatics;
import com.razie.pub.base.log.Log;
import com.razie.pub.http.LightCmdGET;
import com.razie.pub.http.LightServer;
import com.razie.pub.http.SocketCmdListener;
import com.razie.pub.lightsoa.HttpSoaBinding;

/**
 * this is the main web server - required by the Agent itself. Note that you
 * have to create an actual server and initialize it before mounting this
 * service...
 * 
 * As you can see from the code, it simply abstracts the soa registration to an
 * agent service level. It also includes lifecycle management for the server.
 * 
 * provides: web server (http implementation) with lightsoa,
 * 
 * @author razvanc
 * @version $Id$
 * 
 */
public class AgentHttpService extends AgentService {
	public Thread serverThread;
	LightServer server;
	LightCmdGET cmdGET = null;
	StatusReport status = new StatusReport();

	public AgentHttpService(Agent agent, LightServer serverToUse) {
		this.agent = agent;
		this.server = serverToUse;

		AgentHttpService singleton = (AgentHttpService) NoStatics
				.get(AgentHttpService.class);
//		if (singleton != null) {
//			status.lastError = new IllegalStateException(
//					"ERR_SVC_INIT AgentWebSrevice already initialized!");
//			throw (IllegalStateException) status.lastError;
//		}
		NoStatics.put(AgentHttpService.class, this);

		// the server must have a GET command for thise service...
		for (SocketCmdListener l : server.getListeners())
			if (l instanceof LightCmdGET) {
				cmdGET = (LightCmdGET) l;
				break;
			}

		if (cmdGET == null) {
			status.lastError = new IllegalStateException(
					"AgentHttpService needs a LightCmdGET listener initialized with the server");
			throw (IllegalStateException) status.lastError;
		}

		status.ok();
	}

	/** factory stub */
	public static AgentHttpService getInstance() {
		return (AgentHttpService) NoStatics.get(AgentHttpService.class);
	}

	public static void registerSoa(HttpSoaBinding c) {
		getInstance().cmdGET.registerSoa(c);
	}

	public static List<SocketCmdListener> getListeners() {
		return getInstance().server.getListeners();
	}

	public static List<HttpSoaBinding> getBindings() {
		return getInstance().cmdGET.getBindings();
	}

	public void todoEncapsulateSomehowJoin() {
		try {
			serverThread.join();
		} catch (InterruptedException e) {
			// TODO what can I do here?
			Log.logThis("ERR_MUTANT_INTERRUPTED");
		}
	}

	protected void onShutdown() {
		Log.logThis("AGENTWEB_SHUTDOWN");
		server.shutdown();
//		serverThread.interrupt();
		todoEncapsulateSomehowJoin();
	}

	protected void onStartup() {
		Log.logThis("AGENTWEB_STARTUP");
		serverThread = new Thread(server, "AgentServerThread");
		serverThread.start();
	}

	/** get status and report */
	public StatusReport status() {
		StatusReport res = new StatusReport(StatusReport.Status.GREEN);
		return res;
	}

}
