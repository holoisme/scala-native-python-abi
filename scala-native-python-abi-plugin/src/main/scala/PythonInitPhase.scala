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

import tpd.*

class PythonInitPhase(outputDirectory: String, exportedFunctions: ListBuffer[ExportedSiblingFunction]) extends PluginPhase:

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

    writeInit()

    units

  private def inspectDefDef(tree: DefDef)(using ctx: Context): Unit =
    tree.symbol.annotations.find { _.symbol.fullName.toString == exportedAnnotation } match
      case Some(ann) => extractExportedFunction(tree, ann)
      case None => ()
  
  private def extractExportedFunction(tree: DefDef, annotation: Annotation)(using ctx: Context): Unit =
    println(s"${tree.name.show} is exported")

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
        val patametersName = tree.paramss.flatten.map(_.name.show)
        val params = patametersName.zip(methodType.paramInfos).map((name, ty) => FunctionParameter(name, ty))

        functions += ExportedFunction(exportName, params, methodType.resultType, tree.rawComment, exportedFunctions.find(_.siblingSymbol eq tree.symbol))

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
      case Apply(_, List(Literal(constant))) =>
        constant.value match
          case value: String => Some(value)
          case _ => Some(tree.name.show)
      case _ => Some(tree.name.show)

  private def writeInit()(using ctx: Context): Unit =
    val directory = Paths.get(outputDirectory)
    Files.createDirectories(directory)

    val loader = renderPythonLoader(functions.toList)
    val file = directory.resolve("__init__.py")

    Files.writeString(file, loader, StandardCharsets.UTF_8)

  private def renderPythonLoader(functions: List[ExportedFunction])(using ctx: Context): String =
    val lib = "__lib"

    val functionEntries =
      functions.map { function =>
        val (functionName, calledFunction) = function.siblingModel match
          case Some(value) => (escape(value.originalName), escape(value.siblingName))
          case None => (escape(function.name), escape(function.name))

        val parameters = function.parameters.map(p => s"""${{TypeHelper.typeToAbi(p.tpe)}}""").mkString("[", ", ", "]")

        val comment = function.comment match
          case None => ""
          case Some(s) => "  \"\"\"\n  " + s.raw.linesIterator.map(s => "  " + s.trim().stripPrefix("/**").stripPrefix("*/").stripPrefix("*").trim()).mkString("\n").trim() + "\n  \"\"\"\n"
        
        val allChecks = function.parameters.map(p => TypeHelper.checkForType(p.tpe, p.name)).flatten.mkString("\n").indent(2)

          s"""${lib}.${calledFunction}.argtypes = ${parameters}
${lib}.${calledFunction}.restype = ${TypeHelper.typeToAbi(function.returnType)}
def ${functionName}(${function.parameters.map(p => s"""${p.name}: ${{TypeHelper.typeToPythonIndication(p.tpe)}}""").mkString(", ")}) -> ${{TypeHelper.typeToPythonIndication(function.returnType)}}:
${comment}${allChecks}  return ${lib}.${calledFunction}(${function.parameters.map(_.name).mkString(", ")})
"""
      }

    s"""#
# AUTO-GENERATED FILE
# Please do not modify this file.
#

import ctypes
import sys
from pathlib import Path
from typing import Any

def __library_name():
  if sys.platform == "linux":
    return "libexample.so"

  if sys.platform == "darwin":
    return "libexample.dylib"

  if sys.platform == "win32":
    return "example.dll"

  raise RuntimeError(f"Unsupported platform: {sys.platform}")

__library_path = Path(__file__).parent / __library_name()
${lib} = ctypes.PyDLL(str(__library_path))

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
    parameters: List[FunctionParameter],
    returnType: Type,
    comment: Option[Comment],

    siblingModel: Option[ExportedSiblingFunction]

    // name: Symbol,
    // hasSibling: Boolean
    // symbol: Symbol
)
