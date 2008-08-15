/**
 * Easy way to decouple code - simple event/messaging system.
 * 
 * I wanted a simple messaging/event system. As JMS is a very good model, that's what we follow,
 * except we try to optimize and simplify its usage as much as possible: most applications send
 * simple events with just a few properties passed around for either notifications
 * (events/topics/publish-subscribe) or processing (messages/queues/p2p).
 * 
 * Message destinations are queues or topics and their creation/management is separate from their
 * use.
 * 
 * Destinations are resources, which are managed by an environment outside the client (either sender
 * or receiver). Their only link to the actual resources are via: the symbolic name of the
 * destination and a handle to the environment/context that manages them).
 * 
 * @version $Id: package-info.java,v 1.1 2007-10-02 11:54:36 razvanc Exp $
 */
package com.razie.pub.events;

