lazy val V = _root_.scalafix.sbt.BuildInfo

updateOptions := updateOptions.value.withGigahorse(false)

inThisBuild(
  List(
    organization := "io.github.ttj4",
    homepage := Some(url("https://github.com/ttj4/scalafix-community-rules")),
    licenses := List(
      "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        "ttj4",
        "Jinto Thomas",
        "jinto.thomas@live.com",
        url("https://ttj4.github.io")
      )
    ),
    scalaVersion := V.scala213,
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalacOptions ++= List(
      "-Yrangepos",
      "-P:semanticdb:synthetics:on"
    ),
    publishMavenStyle := true,
    makePom / publishArtifact := true,
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/ttj4/scalafix-community-rules"),
        "scm:git@github.com:ttj4/scalafix-community-rules.git"
      )
    )
  )
)

ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publish / skip := true

lazy val rules = project.settings(
  libraryDependencies ++= Seq(
    "ch.epfl.scala" %% "scalafix-cli" % V.scalafixVersion cross CrossVersion.full
  ),
  moduleName := "scalafix-community-rules"
)

lazy val input = project
  .settings(
    publish / skip := true
  )

lazy val output = project
  .settings(
    publish / skip := true
  )

lazy val tests = project
  .settings(
    publish / skip := true,
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full,
    scalafixTestkitOutputSourceDirectories :=
      (output / Compile / sourceDirectories).value,
    scalafixTestkitInputSourceDirectories :=
      (input / Compile / sourceDirectories).value,
    scalafixTestkitInputClasspath :=
      (input / Compile / fullClasspath).value
  )
  .dependsOn(input, rules)
  .enablePlugins(ScalafixTestkitPlugin)