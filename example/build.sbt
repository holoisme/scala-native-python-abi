import scala.scalanative.build.BuildTarget

enablePlugins(ScalaNativePlugin)

scalaVersion := "3.3.7"

nativeConfig ~= { config =>
  config
    .withBuildTarget(BuildTarget.libraryDynamic)
    .withBaseName("example")
}

libraryDependencies +=
  "com.holo" %%% "scala-native-python-runtime" % "0.1.0"

addCompilerPlugin(
  "com.holo" % "scala-native-python-abi-plugin" % "0.1.0" cross CrossVersion.full
)

Compile / scalacOptions ++= {
  val output =
    (Compile / target).value / "python-abi"

  Seq(
    s"-P:scala-native-python-abi:output:${output.getAbsolutePath}"
  )
}

import scala.sys.process.*

lazy val packagePython = taskKey[Unit](
  "Build the Scala Native library and package it with the Python interface"
)

packagePython := {
  val log = streams.value.log

  val packageDir =
    baseDirectory.value / "python" / "example"

  val nativeTarget =
    (Compile / nativeLink).value

  val exports =
    (Compile / target).value / "python-abi" / "__init__.py"

  IO.createDirectory(packageDir)

  val libraryDestination =
    packageDir / nativeTarget.getName

  IO.copyFile(
    nativeTarget,
    libraryDestination
  )

  val exportsDestination =
    packageDir / "__init__.py"

  IO.copyFile(
    exports,
    exportsDestination
  )

  log.info(
    s"Python package assembled at: $packageDir"
  )
}

// import scala.scalanative.build._

// scalaVersion := "3.3.7" // A Long Term Support version.

// enablePlugins(ScalaNativePlugin)

// // set to Debug for compilation details (Info is default)
// // logLevel := Level.Info

// // import to add Scala Native options

// // defaults set with common options shown
// nativeConfig ~= { config =>
//   config.withBuildTarget(BuildTarget.libraryDynamic)
// }
// // nativeConfig ~= { c =>
// //   c.withLTO(LTO.none) // thin
// //     .withMode(Mode.debug) // releaseFast
// //     // .withGC(GC.immix) // commix
// //     .withBuildTarget(BuildTarget.libraryDynamic)
// // }
