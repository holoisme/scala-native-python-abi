
import scala.scalanative.build.BuildTarget

enablePlugins(ScalaNativePlugin)

// lazy val runtime =
//   project
//     .enablePlugins(ScalaNativePlugin)
//     .settings(
//       scalaVersion := "3.3.7",
//       nativeConfig ~= {
//         _.withBuildTarget(BuildTarget.libraryDynamic)
//       }
//     )

ThisBuild / scalaVersion := "3.3.7"

organization := "com.holo"
name := "scala-native-python-runtime"
version := "0.1.0"

// crossVersion := CrossVersion.full
