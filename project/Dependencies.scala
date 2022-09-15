import sbt._

object Dependencies {

  lazy val commonDependencies = Seq(
    "org.scalanlp" %% "breeze" % "2.0"
  )

  lazy val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % "3.2.12"
  )
}
