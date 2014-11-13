import AssemblyKeys._

assemblySettings

scalaVersion := "2.11.2"

lazy val core = project

lazy val indexer = project.dependsOn(core)

lazy val suggester = project.dependsOn(core)

lazy val profiler = project.dependsOn(core)

libraryDependencies ++= {
  Seq(
    "org.apache.commons" % "commons-io" % "1.3.2",
    "org.scala-lang" % "scala-pickling_2.11" % "0.9.1-SNAPSHOT",
    "com.github.wookietreiber" %% "scala-chart" % "latest.integration"
  )
}

resolvers += Resolver.sonatypeRepo("snapshots")
