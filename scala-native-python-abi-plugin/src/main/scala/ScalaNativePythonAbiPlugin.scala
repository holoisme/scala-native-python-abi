import dotty.tools.dotc.CompilationUnit
import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Annotations.Annotation
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*
import dotty.tools.dotc.plugins.PluginPhase
import dotty.tools.dotc.plugins.StandardPlugin
import dotty.tools.dotc.report
import dotty.tools.dotc.typer.TyperPhase

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import scala.collection.mutable

class ScalaNativePythonAbiPlugin extends StandardPlugin:

  val name: String = "scalaNativePythonAbi"

  override val description: String =
    "Generates Python ABI metadata from Scala Native @exported methods"

  override def init(options: List[String]): List[PluginPhase] =
    val output =
      options
        .collectFirst {
          case option if option.startsWith("output:") =>
            option.stripPrefix("output:")
        }
        .getOrElse("target/python-abi")

    List(new ScalaNativePythonAbiPhase(output))


class ScalaNativePythonAbiPhase(output: String) extends PluginPhase:

  import tpd.*

  val phaseName: String = "scalaNativePythonAbi"

  override val runsAfter: Set[String] =
    Set(TyperPhase.name)

  private val exportedAnnotation =
    "scala.scalanative.unsafe.exported"

  private val functions =
    mutable.ListBuffer.empty[ExportedFunction]

  override def runOn(
      units: List[CompilationUnit]
  )(using ctx: Context): List[CompilationUnit] =

    functions.clear()

    for unit <- units do
      val traverser = new TreeTraverser:

        override def traverse(tree: Tree)(using Context): Unit =
          tree match
            case defDef: DefDef =>
              inspectDefDef(defDef)
              traverseChildren(tree)

            case _ =>
              traverseChildren(tree)

      traverser.traverse(unit.tpdTree)

    writeMetadata()

    units

  private def inspectDefDef(
      tree: DefDef
  )(using ctx: Context): Unit =

    val symbol = tree.symbol

    val annotation =
      symbol.annotations.find { annotation =>
        annotation.symbol.fullName.toString == exportedAnnotation
      }

    annotation.foreach { ann =>
      extractExportedFunction(tree, ann)
    }

  private def extractExportedFunction(
      tree: DefDef,
      annotation: Annotation
      // annotation: tpd.Annotation
      // annotation: tpd.Tree
  )(using ctx: Context): Unit =

    val exportName =
      extractExportName(tree, annotation).getOrElse {
        report.error(
          s"Unable to determine export name for ${tree.name}",
          tree.srcPos
        )
        return
      }

    tree.symbol.info match

      case methodType: MethodType =>
        val parameters =
          methodType.paramInfos.map(typeToAbi)

        val returnType =
          typeToAbi(methodType.resultType)

        functions += ExportedFunction(
          exportName,
          parameters,
          returnType
        )

      case polyType: PolyType =>
        report.error(
          s"Generic exported method is not supported: ${tree.name}",
          tree.srcPos
        )

      case other =>
        report.error(
          s"Unsupported exported method type for ${tree.name}: $other",
          tree.srcPos
        )

  private def extractExportName(
      // annotation: tpd.Tree
      tree: DefDef,
      annotation: Annotation
  )(using ctx: Context): Option[String] =
    annotation.tree match
      case Apply(_, Nil) => Some(tree.name.show)
      
      case Apply(_, List(Literal(constant))) =>
        constant.value match
          case value: String =>
            Some(value)
          case _ =>
            None
      
      case _ =>
        None

  private def typeToAbi(
      tpe: Type
  )(using ctx: Context): String =

    if tpe =:= defn.ByteType then
      "i8"

    else if tpe =:= defn.ShortType then
      "i16"

    else if tpe =:= defn.IntType then
      "i32"

    else if tpe =:= defn.LongType then
      "i64"

    else if tpe =:= defn.FloatType then
      "f32"

    else if tpe =:= defn.DoubleType then
      "f64"

    else if tpe =:= defn.BooleanType then
      "bool"

    else if tpe =:= defn.UnitType then
      "void"

    else
      // report.error(
      //   s"Unsupported Scala Native Python ABI type: $tpe"
      // )
      "object"

  private def writeMetadata()(using ctx: Context): Unit =

    val directory =
      Paths.get(output)

    Files.createDirectories(directory)

    val json =
      renderJson(functions.toList)

    val file =
      directory.resolve("exports.json")

    Files.writeString(
      file,
      json,
      StandardCharsets.UTF_8
    )

  private def renderJson(
      functions: List[ExportedFunction]
  ): String =

    val entries =
      functions.map { function =>

        val parameters =
          function.parameters
            .map(value => s""""$value"""")
            .mkString("[", ", ", "]")

        s"""    {
      "name": "${escape(function.name)}",
      "parameters": $parameters,
      "return": "${escape(function.returnType)}"
    }"""
      }

    s"""{
  "functions": [
${entries.mkString(",\n")}
  ]
}
"""

  private def escape(value: String): String =
    value
      .replace("\\", "\\\\")
      .replace("\"", "\\\"")


case class ExportedFunction(
    name: String,
    parameters: List[String],
    returnType: String
)

// import dotty.tools.dotc.ast.tpd
// import dotty.tools.dotc.ast.tpd.*
// import dotty.tools.dotc.core.*
// import dotty.tools.dotc.core.Contexts.Context
// import dotty.tools.dotc.core.Symbols.*
// import dotty.tools.dotc.core.Types.*
// import dotty.tools.dotc.plugins.{PluginPhase, StandardPlugin}
// import dotty.tools.dotc.typer.TyperPhase
// import dotty.tools.dotc.CompilationUnit
// import dotty.tools.dotc.report

// import java.nio.charset.StandardCharsets
// import java.nio.file.{Files, Path, Paths}

// class ScalaNativePythonAbiPlugin extends StandardPlugin:

//   val name: String = "scalaNativePythonAbi"

//   override val description: String =
//     "Generates ABI metadata for Scala Native @exported methods"

//   override def init(options: List[String]): List[PluginPhase] =
//     val output = parseOutput(options)

//     List(
//       new PythonAbiPhase(output)
//     )

//   private def parseOutput(options: List[String]): Path =
//     options.collectFirst {
//       case option if option.startsWith("output:") =>
//         Paths.get(option.stripPrefix("output:"))
//     }.getOrElse {
//       throw new IllegalArgumentException(
//         "scalaNativePythonAbi requires -P:scalaNativePythonAbi:output:<directory>"
//       )
//     }

// final class PythonAbiPhase(outputDirectory: Path) extends PluginPhase:

//   override val phaseName: String =
//     "scalaNativePythonAbi"

//   override val runsAfter: Set[String] =
//     Set(TyperPhase.name)

//   override def runOn(units: List[CompilationUnit])(using
//       ctx: Context
//   ): List[CompilationUnit] =

//     val exported =
//       units.flatMap { unit =>
//         collectFromTree(unit.tpdTree)
//       }

//     Files.createDirectories(outputDirectory)

//     val outputFile =
//       outputDirectory.resolve("exports.json")

//     Files.writeString(
//       outputFile,
//       Json.render(exported),
//       StandardCharsets.UTF_8
//     )

//     report.inform(
//       s"Scala Native Python ABI: generated $outputFile"
//     )

//     super.runOn(units)


//   private def collectFromTree(
//       tree: Tree
//   )(using ctx: Context): List[ExportedFunction] =
//     tree match

//       case definition: DefDef =>
//         exportedFunction(definition).toList

//       case _ =>
//         tree.children.flatMap(collectFromTree)


//   private def exportedFunction(
//       definition: DefDef
//   )(using ctx: Context): Option[ExportedFunction] =

//     val symbol = definition.symbol

//     val exportedAnnotation =
//       symbol.annotations.find { annotation =>
//         annotation.symbol.fullName.toString ==
//           "scala.scalanative.unsafe.exported"
//       }

//     exportedAnnotation.map { annotation =>

//       val exportName =
//         annotation.arguments.headOption match
//           case Some(Literal(Constant(value: String))) =>
//             value

//           case Some(_) =>
//             report.error(
//               s"@exported name for ${symbol.fullName} must be a string literal"
//             )

//           case None =>
//             symbol.name.show

//       val methodType =
//         symbol.info match
//           case method: MethodType =>
//             method

//           case poly: PolyType =>
//             poly.resultType match
//               case method: MethodType =>
//                 method

//               case _ =>
//                 report.error(
//                   s"@exported method ${symbol.fullName} does not have a method type"
//                 )

//           case other =>
//             report.error(
//               s"@exported method ${symbol.fullName} has unexpected type: $other"
//             )

//       ExportedFunction(
//         name = exportName,
//         parameters = methodType.paramTypes.map(typeToAbi),
//         returnType = typeToAbi(methodType.resultType)
//       )
//     }


//   private def typeToAbi(
//       tpe: Type
//   )(using ctx: Context): AbiType =

//     val tpe0 = tpe.dealias.simplified

//     if tpe0 =:= defn.ByteType then
//       AbiType("i8")
//     else if tpe0 =:= defn.ShortType then
//       AbiType("i16")
//     else if tpe0 =:= defn.IntType then
//       AbiType("i32")
//     else if tpe0 =:= defn.LongType then
//       AbiType("i64")
//     else if tpe0 =:= defn.FloatType then
//       AbiType("f32")
//     else if tpe0 =:= defn.DoubleType then
//       AbiType("f64")
//     else if tpe0 =:= defn.BooleanType then
//       AbiType("bool")
//     else if tpe0 =:= defn.UnitType then
//       AbiType("void")
//     else
//       report.errorAndAbort(
//         s"Unsupported @exported ABI type: ${tpe.show}"
//       )

// case class ExportedFunction(
//     name: String,
//     parameters: List[AbiType],
//     returnType: AbiType
// )

// case class AbiType(name: String)


// object Json:

//   def render(functions: List[ExportedFunction]): String =
//     val body =
//       functions
//         .map(renderFunction)
//         .mkString(",\n")

//     s"""{
//        |  "abiVersion": 1,
//        |  "functions": [
//        |$body
//        |  ]
//        |}
//        |""".stripMargin


//   private def renderFunction(
//       function: ExportedFunction
//   ): String =

//     val parameters =
//       function.parameters
//         .map(parameter => s""""${escape(parameter.name)}"""")
//         .mkString(", ")

//     s"""    {
//        |      "name": "${escape(function.name)}",
//        |      "parameters": [$parameters],
//        |      "return": "${escape(function.returnType.name)}"
//        |    }""".stripMargin


//   private def escape(value: String): String =
//     value
//       .replace("\\", "\\\\")
//       .replace("\"", "\\\"")
//       .replace("\n", "\\n")
//       .replace("\r", "\\r")
//       .replace("\t", "\\t")