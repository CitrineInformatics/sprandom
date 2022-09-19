import Dependencies._

ThisBuild / scalaVersion := "2.13.4"
ThisBuild / organization := "io.citrine"
ThisBuild / organizationName := "Citrine Informatics"
ThisBuild / homepage := Some(url("https://github.com/CitrineInformatics/sprandom"))
ThisBuild / developers := List(Developer(
  id="Citrine",
  name="Citrine Informatics",
  email="public-repository@citrine.io",
  url=url("https://github.com/CitrineInformatics")
))
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
