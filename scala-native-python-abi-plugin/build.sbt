ThisBuild / scalaVersion := "3.8.3"

organization := "com.holo"
name := "scala-native-python-abi-plugin"
version := "0.1.0"

libraryDependencies +=
  "org.scala-lang" %% "scala3-compiler" % scalaVersion.value % Provided

crossVersion := CrossVersion.full
