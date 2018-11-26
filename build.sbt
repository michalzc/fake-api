ThisBuild / scalaVersion := "2.11.12"
ThisBuild / organization := "michalz.asynctest"
ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-Xfatal-warnings"
)


lazy val `api-client` = (project in file("api-client"))
  .settings(
    libraryDependencies ++= Dependencies.`common-dependencies`
  )