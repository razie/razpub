import sbt._
import Keys._

object V {
  val version      = "0.3-SNAPSHOT"
  val scalaVersion = "2.9.1"
  val organization = "com.razie"

  def snap = (if (V.version endsWith "-SNAPSHOT") "-SNAPSHOT" else "")

  def SCALAVER   = scalaVersion

  def RAZBASEVER  = "0.6" + snap
  def SNAKKVER    = "0.4" + snap
  def LIGHTSOAVER = "0.6" + snap
  def SCALAFSVER  = "0.3" + snap
  def GREMLINSVER = "0.6" + snap
}

object MyBuild extends Build {

  def scalatest = "org.scalatest"  % "scalatest_2.9.1" % "1.6.1"
  def junit     = "junit"          % "junit"           % "4.5"      % "test->default"
  def json      = "org.json"       % "json"            % "20090211"
  
  val snakk    = "com.razie" %% "snakk-core"      % V.SNAKKVER
  def razBase  = "com.razie" %% "razbase"         % V.RAZBASEVER
  def swing20  = "com.razie" %% "20swing"         % V.RAZBASEVER
  def lightsoa = "com.razie" %% "lightsoa-core"   % V.LIGHTSOAVER
  def scalafs  = "com.razie" %% "scalafs"         % V.SCALAFSVER
  def gremlins = "com.razie" %% "gremlins"        % V.GREMLINSVER

  lazy val root = Project(id="razpub",    base=file("."),
                          settings = defaultSettings ++ 
                            Seq(libraryDependencies ++= Seq(
                              scalatest, junit, json, razBase, snakk, lightsoa, scalafs, gremlins))
                  ) 

  def defaultSettings = Defaults.defaultSettings ++ Seq (
    scalaVersion         := V.scalaVersion,
    version              := V.version,

    organization         := V.organization,
    organizationName     := "Razie's Pub",
    organizationHomepage := Some(url("http://www.razie.com")),

    publishTo <<= version { (v: String) =>
      if(v endsWith "-SNAPSHOT")
        Some ("Sonatype" at "https://oss.sonatype.org/content/repositories/snapshots/")
      else
        Some ("Sonatype" at "https://oss.sonatype.org/content/repositories/releases/")
    }  )
}
