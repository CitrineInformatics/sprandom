import sbt._

object Dependencies {

  lazy val commonDependencies = Seq(
    "org.apache.commons" % "commons-rng-core"   % "1.4",
    "org.apache.commons" % "commons-rng-simple" % "1.4"
  )

  lazy val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % "3.2.12"
  )
}
