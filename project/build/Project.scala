import sbt._

class PRazpub(info: ProjectInfo) extends DefaultProject(info) {

  override def managedStyle = ManagedStyle.Maven
  val publishTo =
    if (version.toString endsWith "-SNAPSHOT")
      "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/snapshots/"
    else
      "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/releases/"
  Credentials(Path.userHome / ".ivy2.credentials", log)

  val snap = (if (version.toString endsWith "-SNAPSHOT") "-SNAPSHOT" else "")

  val scalatest = "org.scalatest" % "scalatest_2.9.1" % "1.6.1"
  val junit     = "junit"         % "junit"           % "4.5" % "test->default"
  val json      = "org.json"      % "json"            % "20090211"

  val slf4jApi  = "org.slf4j"     % "slf4j-api"       % "1.6.1"

  val gremlins  = "com.razie"    %% "gremlins-core"   % ("0.5" + snap)
  val scalafs   = "com.razie"    %% "scalafs"         % ("0.2" + snap)
}

