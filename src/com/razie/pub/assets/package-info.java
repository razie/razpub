/**
 * Fairly light and flexible asset management framework
 * 
 * <h1>Vision</h1>
 * <p>
 * There are assets all over the place, like movies, devices, a running windows computer etc. These
 * can be uniquely identified and managed.
 * <p>
 * The more code supports common asset management, the better they can integrate. This entire
 * project is mostly about an asset management framework and related utilities for managing those
 * assets (web servers, soa calls via http or upnp or whatever protocol you'd have).
 * 
 * 
 * <h2>Assets and services</h2>
 * <p>
 * The service-oriented world is good granularity for providing coarse functionality but it misses
 * the point when it comes to programming: you now have to think in terms of controler/stupid data,
 * unless you add a wrapper layer of intelligent objects that delegate their methods to services.
 * <p>
 * That's what this entire framework is about: providing a unified set of frameworks to allow rapid
 * and unified implementation of smart assets which then can be controlled remotely.
 * <p>
 * This is the difference between <code>somerobotservice.move(myrobot, x, y)</code> and
 * <code>myrobot.move(x,y)</code>.
 * <p>
 * An asset-oriented viewpoint is richer than a service-oriented viewpoint since it implies notions
 * of presentation for the assets, containers of assets and management. It also allows clients to look
 * natural.
 * <p>
 * Why not "object-oriented" but rather "asset-oriented"? The notion of "object" is way too generic
 * and fuzzy for what we want here. Assets borrow from service the remoteness and add intelligence,
 * transparent control, and some notional presentation.
 * 
 * <h1>Assets</h1>
 * <p>
 * Assets are uniquely identified by type, key and location.
 * <h1>Frameworks</h1>
 * <p>
 * The lightsoa framework provides the service abstraction, which is good for remote block of
 * functionality. On top of this we add the smart asset objects.
 * 
 * STATE: post-concept, changes but not often
 * 
 * @version $Id: package-info.java,v 1.1 2007-10-02 11:54:36 razvanc Exp $
 */
package com.razie.pub.assets;

