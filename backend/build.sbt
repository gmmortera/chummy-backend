name := """backend"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.14"
val PlaySlickVersion = "5.3.0"
val PekkoVersion = "1.0.2"

libraryDependencies += guice
libraryDependencies ++= Seq(
	"org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test,
	"com.typesafe.play" %% "play-slick" % PlaySlickVersion,
	"com.typesafe.play" %% "play-slick-evolutions" % PlaySlickVersion,
	"org.postgresql" % "postgresql" % "42.7.2",
	"org.typelevel" %% "cats-core" % "2.12.0",
	"org.apache.pekko" %% "pekko-actor" % PekkoVersion
)

PlayKeys.devSettings ++= Seq("play.server.websocket.periodic-keep-alive-max-idle" -> "10 seconds",
							 "play.server.websocket.periodic-keep-alive-mode" -> "pong")
