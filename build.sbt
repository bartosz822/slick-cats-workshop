name := "slick-cats-workshop"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies += "org.typelevel" %% "cats-core" % "1.0.1"
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.2.2"
libraryDependencies += "com.h2database" % "h2" % "1.4.196" % Test % "runtime"
libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.6.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test
