import Dependencies._

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / crossScalaVersions := List("2.13.8")
ThisBuild / version := "0.0.1"
ThisBuild / organization := "io.citrine"
ThisBuild / organizationName := "Citrine Informatics"
ThisBuild / homepage := Some(url("https://github.com/CitrineInformatics/random"))
ThisBuild / description := "Reliable random number generation in Scala."
ThisBuild / licenses += "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")

// Publish settings
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
ThisBuild / scmInfo := Some(
  ScmInfo(url("https://github.com/CitrineInformatics/random"), "scm:git@github.com:CitrineInformatics/random.git")
)

lazy val commonSettings = Seq(
  javaOptions ++= sys.env.getOrElse("JAVA_OPTS", "").split(" ").toSeq,
  pomIncludeRepository := { _ => false }
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "random",
    libraryDependencies ++= commonDependencies,
    libraryDependencies ++= testDependencies.map(_ % Test)
  )
