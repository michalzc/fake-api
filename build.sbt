ThisBuild / scalaVersion := "2.11.12"
ThisBuild / organization := "michalz"
ThisBuild / scalacOptions ++= Seq(
    "-deprecation",
    "-Xfatal-warnings",
    "-Ywarn-unused:implicits",
    "-Ywarn-unused:imports"
)

lazy val protocol = (project in file("protocol"))
    .settings(
        name := "Protocol classes"
    )

lazy val `example-api-server` = (project in file("api"))
    .settings(
        name := "Example API Server"
    )
    .dependsOn(protocol)

lazy val `api-client` = (project in file("client"))
    .settings(
        name := "API Consumer"
    )
    .dependsOn(protocol)


lazy val root = (project in file("."))
    .settings(
    )
    .aggregate(`example-api-server`, `api-client`)
