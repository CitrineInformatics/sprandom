import sbt._

object Dependencies {

  lazy val commonDependencies = Seq(
  )

  lazy val testDependencies = Seq(
    "junit"          % "junit"           % "4.13.2",
    "com.github.sbt" % "junit-interface" % "0.13.3",
    "org.scalatest" %% "scalatest"       % "3.2.12"
  )
}
