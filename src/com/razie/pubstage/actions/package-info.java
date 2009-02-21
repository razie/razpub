/**
 * The world of Actionables
 * 
 * <p>
 * The basic idea is that the world is a network of objects which interact via "actions". Thus
 * "actionable" is a basic building block for the world at large, let alone the world of virtual,
 * computer-based assets. Being an old-school object-oriented, I do not subscribe to the managed
 * objects concept, but rather see objects as self-suficient agents.
 * 
 * <p>
 * Here's the basic actionable concepts:
 * <ul>
 * <li>Actionable <br>
 * a basic object that supports actions
 * <li>ActionableSpec <br>
 * the specification of object that supports actions - a list of ActionSpecs, really
 * <li>ActionSpec <br>
 * the specification of a particular action item
 * <li>ActionMeta <br>
 * the meta of a partciular action item (category, i.e. "play", "delete")
 * <li>ActionToInvoke <br>
 * the prepared invocation request, i.e.
 * </ul>
 * 
 * <p>
 * What can be done with actions (features of the actionables framework):
 * <ul>
 * <li>specify <br>
 * can specify a possible action
 * <li>prepare <br>
 * can prepare a possible action for invocation
 * <li>invoke <br>
 * invoke a particular action
 * <li>inject <br>
 * inject a new action into an existing system
 * <li>proxy/wrap <br>
 * proxy an actionable with another (the proxy can be complicated, i.e. BPEL)
 * <li>script new action <br>
 * can script a new action in the scripting language of choice
 * <li>script new proxy/injection <br>
 * change behaviour of existing actions via scripted and injected actions
 * </ul>
 * 
 * <p>
 * Actions can be invoked in several ways:
 * <li>direct java/scala code<br>
 * since the current implementation of this is in java, most actionables could be invoked directly
 * from colocated java
 * <li>http<br>
 * simplest univeral protocol today - easiest interaction via urls
 * <li>telnet<br>
 * do not discount this simplest of remote communication protocols just yet
 * <li>command line<br>
 * simple command line
 * <li>other languages/scripts<br>
 * skeletons/stubs can offer simple calls in other scripts. The basis would be a simple http
 * invocation.
 * <li>WebServices, CORBA<br>
 * Really? What are you, a sadistic maniac?
 * 
 * <p>
 * Sync vs async<br>
 * This is a very important concept, of either invoking a function or sending a message...any action
 * must be able to be invoked either way
 * </p>
 * 
 * <p>
 * Completing the framework further, we need simple means to coreograph actionables together, via
 * simple scripting and/or graphical workflows (either unstructured BPM style or structured BPEL).
 * 
 * <p>
 * This also makes it easy to introduce higher level DSL, to simplify things, Scala being critical
 * from this perspective.
 * 
 * 
 * @version $Id: package-info.java,v 1.1 2007-10-02 11:54:36 razvanc Exp $
 */
package com.razie.pubstage.actions;

