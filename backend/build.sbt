name := """backend"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.14"

libraryDependencies += guice
libraryDependencies ++= Seq(
	"org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test,
	"com.typesafe.play" %% "play-slick" % "5.3.0",
	"com.typesafe.play" %% "play-slick-evolutions" % "5.3.0",
	"org.postgresql" % "postgresql" % "42.7.2",
	"org.typelevel" %% "cats-core" % "2.12.0"
)

PlayKeys.devSettings ++= Seq("play.server.websocket.periodic-keep-alive-max-idle" -> "10 seconds",
							 "play.server.websocket.periodic-keep-alive-mode" -> "pong")
