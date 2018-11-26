ThisBuild / scalaVersion := "2.11.12"
ThisBuild / organization := "michalz.asynctest"
ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-Xfatal-warnings"
)

val `common-dependencies` = Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
  "org.apache.logging.log4j" % "log4j-api" % "2.11.1",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.11.1"
)

lazy val protocol = (project in file("protocol"))
  .settings(
    name := "Protocol classes",
    libraryDependencies ++= `common-dependencies`
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
    name := "Asynchronus Tests"
  )
  .aggregate(`example-api-server`, `api-client`)
