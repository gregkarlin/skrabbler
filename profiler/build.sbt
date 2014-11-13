import java.io.File

import AssemblyKeys._

assemblySettings

scalaVersion := "2.11.2"

jarName in assembly := { s"${name.value}.jar" }

libraryDependencies ++= {
  Seq(
    "com.github.wookietreiber" %% "scala-chart" % "latest.integration"
  )
}

resolvers += Resolver.sonatypeRepo("snapshots")