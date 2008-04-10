/**
Basic Drawables and streamables

<p>This package has two main ideas: rendering and streaming of
objects.
<h1>Drawables and rendering</h1>

<p>The idea is that all objects are Drawables and as such, can be
rendered onto some technology. The rendering logic rests with the
developer of the object and can also be added from the outisde, when a
new rendering technology is added for existing objects.
<p>Rendering can be considered the same as Serialization (render
onto a JSON stream for instance).
<p>To add a renderer for an existing object, just register it in the
Renderer with the class name you want to render and technology.
<p>Wrap streams in the order of the protocol stack. For instance, to
stream a DIDL to a HTTP reply socket, use DIDLStream
(HttpStream(Socket)).
<p>Main rendering helper is Renderer.Helper.draw()
<h2>Streamables</h2>

<p>The usual functional approach <code>result = search(x)</code>
implies that clients wait for all the processing to complete before
receiving anything. Often times, that is both unacceptable and wasteful,
for no good reason, other than suitable for lazy/junior programmers.
<p>Instead, we propose a simple streaming model, where the
equivalent is <code>search (resultStream, x)</code> which doesn't
complicate either implementation nor client. Since we're talking
multiple results, the client code can be a simple for loop but now it
has the option of mounting processors on the stream and do more stuff,
incuding paralle processing etc. Likewise, the results for instance can
be streamed on screen (or html page) as they're found, so the clients
don't have to wait for all 1000 movies to be found when they care about
the second...
 * 
 * STATE: concept, changes often
 * 
 * @version $Id: package-info.java,v 1.1 2007-10-02 11:54:36 razvanc Exp $
 */
package com.razie.pub.hframe.draw;

