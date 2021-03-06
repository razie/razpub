/**  ____    __    ____  ____  ____,,___     ____  __  __  ____
 *  (  _ \  /__\  (_   )(_  _)( ___)/ __)   (  _ \(  )(  )(  _ \           Read
 *   )   / /(__)\  / /_  _)(_  )__) \__ \    )___/ )(__)(  ) _ <     README.txt
 *  (_)\_)(__)(__)(____)(____)(____)(___/   (__)  (______)(____/    LICENSE.txt
 */
package com.razie.pub.base.data.test

import org.scalatest.junit._
import razie.base.data.RazElement
import razie.base.data.RazElement._
import razie.base.data._

/** testing the RazElement */
class TestRazElementJava extends JUnit3Suite {

   // using implicit conversion...
   val doc:RazElement= new XmlDoc().load("testrazelement", getClass().getResource("testing.xml"));
  
   def testA =
     expect ("roota") { doc a "name" }
   
   def testXpa =
     expect ("11") { doc xpa "/root/parent[@name='1']/child[@name='11']/@name" }
  
   def testXpe =
     expect ("11") { doc xpe "/root/parent[@name='1']/child[@name='11']" a "name" }
  
   def testXpl =
     expect (3) { doc xpl "/root/parent[@name='1']/child" size }
}

/** the scala version only supports one elemnet - to solve a path, use the XP.scala */
class TestRazElementScala extends JUnit3Suite {

   // using implicit conversion...
	val doc:RazElement = xml.XML.load (getClass().getResource("testing.xml").openStream());
  
   def testA =
     expect ("roota") { doc a "name" }
   
   def testXpa =
     expect ("roota") { doc xpa "@name" }
  
   def testXpe =
     expect ("roota") { doc xpe "/root" a "name" }
  
   def testXpl =
     expect (1) { doc xpl "/root" size }
}
