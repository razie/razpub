package com.razie.pub.draw;

/**
  * about technologies...you see a pile of unrelated stuff...
  * 
  * <p>
  * TEXT - simple toString, whatever that does...
  * 
  * <p>
  * HTML - format the objects for writing to html
  * 
  * <p>
  * XML - whatever native format you have for XML...could as well as be HTML if you don't have
  * anything else
  * 
  * <p>
  * JSON - guess what that is
  * 
  * <p>
  * UPNP - works only for UPNP knowledgeable containers and items, otherwise it's XML...a UPNP
  * method knows what it returns...they would also know the difference between META and
  * DATA...For instance, an AssetBrief knows to turn into an UPNP item and an AssetFolder knows
  * to turn into an UPNP container...that's about it :)
  * 
  * <p>
  * ECLIPSE as opposed to SWING and SVG
  * 
  * @author razvanc99
  * 
  */
 public enum Technology {
     TEXT, HTML, SWING, SVG, ECLIPSE, XML, JSON, UPNP, ANY, RSS, MEDIA_RSS
 }