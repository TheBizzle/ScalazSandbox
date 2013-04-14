name := "Scalaz Sandbox"

scalaVersion := "2.10.1"

resolvers ++= Seq("Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/")

libraryDependencies  ++= Seq("org.scalaz" %% "scalaz-core" % "7.0-SNAPSHOT")
