name := "slick-cats-workshop"

version := "0.1"

scalaVersion := "2.12.4"


//basics
libraryDependencies += "org.typelevel" %% "cats-core" % "1.1.0"
libraryDependencies += "org.typelevel" %% "cats-mtl-core" % "0.2.1"
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.2.2"
libraryDependencies += "com.h2database" % "h2" % "1.4.196"
libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.6.4"

//basic tests
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.5" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.3" % Test

//cats instances tests
libraryDependencies += "org.typelevel" %% "cats-laws" % "1.0.1" % Test
libraryDependencies += "org.typelevel" %% "cats-testkit" % "1.0.1" % Test
libraryDependencies += "com.github.alexarchambault" %% "scalacheck-shapeless_1.13" % "1.1.6" % Test

scalacOptions += "-Ypartial-unification"
parallelExecution in Test := false
