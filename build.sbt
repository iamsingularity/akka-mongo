lazy val akkaHttpVersion = "10.1.1"
lazy val akkaVersion = "2.5.11"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.john-cd",
      scalaVersion := "2.12.5"
    )),
    name := "akka-rest-mongo",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "org.mongodb" %% "casbah" % "3.1.1",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.1",
      "joda-time" % "joda-time" % "2.9.9",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "org.scalatest" %% "scalatest" % "3.0.1" % Test
    )
  )


/* DEPLOYMENT
https://steveking.site/blog/2017/deploying-scala-sbt-microservices-to-kubernetes/
https://sbt-native-packager.readthedocs.io/en/latest/formats/docker.html

The NativePackagerHelper package contains some sbt tasks and settings that we will make use of to bundle our Docker image. We also enable two plugins. The first plugin, JavaAppPackaging, is what compiles the libraries and source code we're using in our application to jars, and generates an executable script to run the application inside of the Docker image. The DockerPlugin is a required setting to use the sbt Docker plugin.
*/

import NativePackagerHelper._

enablePlugins(JavaAppPackaging, DockerPlugin)

packageName in Docker := packageName.value
version in Docker := version.value
dockerExposedPorts := List(8080)
dockerLabels := Map("maintainer" -> "noreply@john-cd.com")
dockerBaseImage := "openjdk"
dockerRepository := Some("johncd")

defaultLinuxInstallLocation in Docker := "/usr/local"
daemonUser in Docker := "daemon"

