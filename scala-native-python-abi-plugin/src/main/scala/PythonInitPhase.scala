import dotty.tools.dotc.CompilationUnit
import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Annotations.Annotation
import dotty.tools.dotc.core.Comments.Comment
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
import scala.collection.mutable.ListBuffer
import scala.compiletime.ops.boolean

class PythonInitPhase(outputDirectory: String, functionsWithSiblings: ListBuffer[Symbol]) extends PluginPhase:

  import tpd.*

  val phaseName: String = "python-init-phase"

  override val runsAfter: Set[String] = Set(TyperPhase.name)

  private val exportedAnnotation = "scala.scalanative.unsafe.exported"

  private val functions = mutable.ListBuffer.empty[ExportedFunction]

  override def runOn(units: List[CompilationUnit])(using ctx: Context): List[CompilationUnit] =
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

  private def inspectDefDef(tree: DefDef)(using ctx: Context): Unit =
    val symbol = tree.symbol

    println("")
    println(tree.symbol.show)
    println(tree.symbol.annotations.map(_.symbol.fullName))

    val annotation =
      symbol.annotations.find { annotation =>
        annotation.symbol.fullName.toString == exportedAnnotation
      }

    annotation.foreach { ann =>
      extractExportedFunction(tree, ann)
    }

  private def extractExportedFunction(tree: DefDef, annotation: Annotation)(using ctx: Context): Unit =
    val exportName =
      extractExportName(tree, annotation).getOrElse {
        report.error(
          s"Unable to determine export name for ${tree.name}",
          tree.srcPos
        )
        return
      }

    val hasSibling = functionsWithSiblings.contains(tree.symbol)

    tree.symbol.info match

      case methodType: MethodType =>
        val patametersName = tree.paramss.flatten.map(_.name.show)
        val params = patametersName.zip(methodType.paramInfos).map((name, ty) => FunctionParameter(name, ty))

        functions += ExportedFunction(exportName, params, methodType.resultType, tree.rawComment, hasSibling)

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

  private def extractExportName(tree: DefDef, annotation: Annotation)(using ctx: Context): Option[String] =
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

  private def writeMetadata()(using ctx: Context): Unit =
    val directory = Paths.get(outputDirectory)
    Files.createDirectories(directory)

    val loader = renderPythonLoader(functions.toList)
    val file = directory.resolve("__init__.py")

    Files.writeString(file, loader, StandardCharsets.UTF_8)

  private def renderPythonLoader(functions: List[ExportedFunction])(using ctx: Context): String =

    println("\nFunctions with siblings:")
    println(functionsWithSiblings)
    println("\n")

    val libName = "_library"

    val functionEntries =
      functions.map { function =>
        val hasSibling = function.hasSibling// functionsWithSiblings.contains(function.name)
        val functionName = escape(function.name)
        val calledFunction = if(hasSibling) s"""${functionName}_abi""" else functionName
        val parameters = function.parameters.map(p => s"""${{TypeHelper.typeToAbi(p.tpe)}}""").mkString("[", ", ", "]")

        val comment = function.comment match
          case None => ""
          case Some(s) => "\"\"\"\n  " + s.raw.linesIterator.map(s => " " + s.trim().stripPrefix("/**").stripPrefix("*/").stripPrefix("*")).mkString("\n").trim() + "\n  \"\"\"\n  " //.mkString("\"\"\"\n", "\n", "\n\"\"\"\n  ")
        
          s"""${libName}.${functionName}.argtypes = ${parameters}
${libName}.${functionName}.restype = ${escape(TypeHelper.typeToAbi(function.returnType))}
def ${functionName}(${function.parameters.map(p => s"""${p.name}: ${{TypeHelper.typeToPythonIndication(p.tpe)}}""").mkString(", ")}) -> ${{TypeHelper.typeToPythonIndication(function.returnType)}}:
  ${comment}return ${libName}.${calledFunction}(${function.parameters.map(_.name).mkString(", ")})
"""
      }

    val globalUpdate = s"""globals().update({
  ${functions.map(f => 
      s""""${f.name}": ${f.name},"""
    ).mkString("\n  ")}
})"""

    s"""
#
# AUTO-GENERATED FILE
# Please do not modify this file.
#

import ctypes
import sys
from pathlib import Path
from typing import Any

def _library_name():
    if sys.platform == "linux":
        return "libexample.so"

    if sys.platform == "darwin":
        return "libexample.dylib"

    if sys.platform == "win32":
        return "example.dll"

    raise RuntimeError(f"Unsupported platform: {sys.platform}")

_library_path = Path(__file__).parent / _library_name()
${libName} = ctypes.CDLL(str(_library_path))

${functionEntries.mkString("\n\n")}"""

  private def escape(value: String): String =
    value
      .replace("\\", "\\\\")
      .replace("\"", "\\\"")


case class FunctionParameter(
    name: String,
    tpe: Type
)

case class ExportedFunction(
    name: String,
    // name: Symbol,
    parameters: List[FunctionParameter],
    returnType: Type,
    comment: Option[Comment],
    hasSibling: Boolean
    // symbol: Symbol
)
