import Dependencies._

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / crossScalaVersions := List("2.13.8")
ThisBuild / organization := "io.citrine"
ThisBuild / organizationName := "Citrine Informatics"
ThisBuild / homepage := Some(url("https://github.com/CitrineInformatics/sprandom"))
ThisBuild / description := "Splittable, serializable pseudorandom number generation in Scala."
ThisBuild / licenses += "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")

// Publish settings
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
ThisBuild / scmInfo := Some(
  ScmInfo(url("https://github.com/CitrineInformatics/sprandom"), "scm:git@github.com:CitrineInformatics/sprandom.git")
)
ThisBuild / pomIncludeRepository := { _ => false }

lazy val commonSettings = Seq(
  javaOptions ++= sys.env.getOrElse("JAVA_OPTS", "").split(" ").toSeq
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "sprandom",
    libraryDependencies ++= commonDependencies,
    libraryDependencies ++= testDependencies.map(_ % Test)
  )
