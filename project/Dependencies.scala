import sbt._

object Dependencies {

  lazy val commonDependencies = Seq(
  )

  lazy val testDependencies = Seq(
    "org.scalatest"           %% "scalatest"                  % "3.2.12",
    "org.scalanlp"            %% "breeze"                     % "2.0",
    "org.scala-lang.modules"  %% "scala-parallel-collections" % "1.0.0"
  )
}
