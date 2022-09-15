import sbt._

object Dependencies {

  lazy val commonDependencies = Seq(
    "org.apache.commons" % "commons-math3" % "3.6.1",
    "org.scalanlp" %% "breeze" % "2.0"
  )

  lazy val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % "3.2.12"
  )
}
