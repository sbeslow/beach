name := """beach"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.jsoup" % "jsoup" % "1.8.1",
  "mysql" % "mysql-connector-java" % "5.1.35"
)