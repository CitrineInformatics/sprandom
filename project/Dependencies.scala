import sbt._

object Dependencies {

  lazy val commonDependencies = Seq(
  )

  lazy val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % "3.2.12",
    "org.scalanlp" %% "breeze" % "2.0"
  )
}
