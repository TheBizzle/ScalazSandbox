name := "Scalaz Sandbox"

scalaVersion := "2.10.1"

scalaSource in Compile <<= baseDirectory(_ / "src" / "test")

scalaSource in Test <<= baseDirectory(_ / "src" / "test")

resolvers ++= Seq(
  "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.0-SNAPSHOT",
  "org.scalatest" % "scalatest_2.10" % "1.9.1",
  "com.chuusai" % "shapeless_2.10" % "1.2.3"
)
