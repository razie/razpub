/**
 * Razie's public code
 * 
 * This is my playground. Lots of basic code that can be reused and doesn't really have any
 * marketable smarts, so why not make it public - I will, at the very least, get some useful
 * comments.
 * 
 * This is how we evolve: a step at a time, enriching borrowed ideas and feedback. The Internet
 * speeds it all up. If you don't care to even comment, maybe there's some new ideas you can pickup.
 * 
 * I generally like to play around with fuzzy concepts and see where they take me. I could, of
 * course, sit down, smoke a lot and just think abstract concepts, but often it is more interesting
 * to just start banging some light prototypes, after a few ideas. At the very least, I end up with
 * a model and the ideas are documented and crystallized that way.
 * 
 * When adding features to base/common code, it generally becomes too coupled. Such as a plain
 * action supporting http thus requiring authentication services...which also know about assets
 * which are drawables etc :)
 * 
 * However, the packages here provide functionality we're most likely to need most of the time, so
 * they can be bundled together.
 * 
 * 
 * <h1>Quality</h1>
 * 
 * One good thing from sharing this code is that I have to make it good enough. Documented,
 * tested...and clean. Any contributors will have to stick the same quality requirements. Of course
 * you'll find hacks here and there, but I'll keep them to a minimum.
 * 
 * 
 * <h1>Lightweight distributed application support</h1>
 * 
 * The main purpose of this code here was to create a framework for developing some smart
 * distributed applications. I needed it to be very lightweight (run the entire server inside a
 * junit test case) and simplify development of future smarts. I generally focus on simple client
 * code rather than simple APIs - the two are not always the same thing.
 * 
 * 
 * <h1>Some principles</h1>
 * 
 * Here are some of my fundamental programming principles.
 * 
 * <h2>Embedded presentation</h2>
 * 
 * All artifacts end up being used by some user. In this framework, presentation is embedded and as
 * generic as possible.
 * 
 * <h2>Performance</h2>
 * 
 * Response time is paramount and this is reflected throughout. All code tries to reply to the user
 * as fast as possible. You can see this from the streamable drawables to the background
 * initialization of irrelevant services.
 * 
 * The idea behind the entire comm package is a future optimization of sequences of calls via new
 * protocols like SCTP or similar.
 * 
 * Note that this performance requirement does not conflict with the use of Java: the code must
 * respond in a timely fashion, but does not have to be the most optimal code from an assembly
 * language perspective. This means that using patterns etc to make the code easier to maintain/flex
 * is more important than using the most efficient constructs all the time.
 * 
 * <h2>Remote management</h2>
 * 
 * All software must be able to be remotely controlled, best option is http (upnp and similar work
 * as well). This is to say that all important internal functionality must be presented via a simple
 * telnet/http interface, for others to automate. No component is limited to just user interaction.
 * 
 * Just think how easy it would be to automate playing a batch of files from a remote server, if
 * only Windows Media Player had a very simple interface for play/pause/etc...
 * 
 * This is an extension of the unix command lines, where you could easily script higher-level
 * functionality from available commands.
 * 
 * @version $Id: package-info.java,v 1.1 2007-10-02 11:54:36 razvanc Exp $
 */
package com.razie.pub;

