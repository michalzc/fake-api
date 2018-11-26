import sbt._

object Dependencies {

  val log4jVersion = "2.11.1"
  val catsVersion = "1.0.1"
  val circeVersion = "0.9.3"

  val `common-dependencies` = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
    "org.apache.logging.log4j" % "log4j-api" % log4jVersion,
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion,
    "org.typelevel" %% "cats-core" % catsVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "org.scalaj" %% "scalaj-http" % "2.4.1"
  )


}
