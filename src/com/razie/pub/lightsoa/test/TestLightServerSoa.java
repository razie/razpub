/**
 * Razvan's code. Copyright 2008 based on Apache (share alike) see LICENSE.txt for details.
 */
package com.razie.pub.lightsoa.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;

import com.razie.pub.base.ActionItem;
import com.razie.pub.base.log.Log;
import com.razie.pub.comms.ActionToInvoke;
import com.razie.pub.http.test.TestLightBase;
import com.razie.pub.lightsoa.HttpSoaBinding;

/**
 * test the light server
 * 
 * @author razvanc99
 */
public class TestLightServerSoa extends TestLightBase {

    SampleEchoLightSoaService echo;

    public void setUp() {
        super.setUp();

        if (echo == null) {
            // that's how you start/mount a service
            echo = new SampleEchoLightSoaService();
            HttpSoaBinding soa = new HttpSoaBinding(echo, "echoservice");
            cmdGET.registerSoa(soa);
        }
    }

    /** test the SOA simple echo */
    public void testSoaEcho() throws IOException, InterruptedException {
        // send echo command
        Socket remote = new Socket("localhost", PORT);
        PrintStream out = new PrintStream(remote.getOutputStream());
        // this is how an http request is sent via the socket
        out.println("GET /lightsoa/echoservice/echo?msg=samurai HTTP/1.1");
        out.println("");

        // wait a bit for receiver thread to consume...
        for (long deadline = System.currentTimeMillis() + 20000; deadline > System.currentTimeMillis();) {
            Thread.sleep(100);
            if (echo.input != null)
                break;
        }

        assertTrue(echo.input.contains("samurai"));
    }

    /**
     * test the SOA simple echo via the proper URL reader (check implementation as a proper http
     * server)
     */
    public void testSoaEchoUrl() throws IOException, InterruptedException {
        // send echo command
        URL url = new URL("http://localhost:" + PORT + "/lightsoa/echoservice/echo?msg=samurai");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String result = in.readLine();
        in.close();

        assertTrue(result.contains("samurai"));
    }

    /**
     * test the SOA simple echo via the ActionToInvoke
     */
    public void testSoaEchoAction() throws IOException, InterruptedException {
        // send echo command
        ActionToInvoke action = new ActionToInvoke("http://localhost:" + PORT + "/", new ActionItem(
                "echoservice/echo"), "msg", "samurai");
        String result = (String) action.exec(null);

        assertTrue(result.contains("samurai"));
    }

    static final Log logger = Log.Factory.create(TestLightServerSoa.class.getName());
}
