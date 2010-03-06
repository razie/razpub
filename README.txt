/**  ____    __    ____  ____  ____/___      ____  __  __  ____
 *  (  _ \  /__\  (_   )(_  _)( ___) __)    (  _ \(  )(  )(  _ \
 *   )   / /(__)\  / /_  _)(_  )__)\__ \     )___/ )(__)(  ) _ <
 *  (_)\_)(__)(__)(____)(____)(____)___/    (__)  (______)(____/
 *                      
 *  Copyright (c) Razvan Cojocaru, 2007+, Creative Commons Attribution 3.0
 */

What's this?
------------
Lots of under-piping code that I wish I didn't have to write but needed to. 

Why?
----
Well, just like i wish I didn't have to write it, there's no point in making others write it, 
should they need it. It's not like it's anything smart so far and possibly not useful, but 
maybe others find it useful. The more the better.


Details
-------
See com.razie.pub's package-info.java and the others, for up-to-date details. Here's an abstract.

The aim is to support a grid/fabric like framework for smart assets. Read-on to figure out the pieces of the puzzle :)

There's a very simple and light and especially embedded http server.

On top of this there's a simple LightSoa framework, which allows you to easily create "services": 
just have a Java class, annotate the methods you want available over http and register it with the server.
While the servlet thing is great, it's not that easy to use and most services/methods are really simple 
and this way you can test them either directly by Java calls or via the http server.

Assets - bastardized word, but here it is. You can think of them as ManagedEntities (see OSS/J) or something 
like stateful beans. Just like services are simple classes (one instance to serve all), the assets are 
individual objects which you can access remotely. They however have state and could be persisted.
They are developed much like the services (annotated methods).

There's some very basic multi-threading support (life), which keeps track of the threads and what they're doing.
I envision some beings jumping up and down inside the program, which need to breathe(). There's also workers which 
only process() something.

Two things I'm investigating are drawing and streaming.

Drawing - basically any code anywhere will draw something. Even if it doesn't, the objects it uses could be seen by 
someone somewhere, be it a web page, AJAX, SVG, Eclipse, SWING ... god knows what. So, here's a simple and 
generic drawing framework. 

Streaming - instead of hard-coding the communication protocols throughout the code, I'm trying to abstract the basics
of a communications framework.


Roadmap
-------
I will only maintain this as I need to. If there's some large user community developing (doubt that, really), 
we'll see - I could co-op some volunteers.

I will add more code as I need it/write it for all kinds of reasons.

