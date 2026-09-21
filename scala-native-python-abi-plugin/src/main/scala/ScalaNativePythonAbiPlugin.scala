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
import scala.util.boundary

class ScalaNativePythonAbiPlugin extends StandardPlugin:

  val name: String = "scala-native-python-abi"

  override val description: String = "Generates Python ABI from Scala Native @exported items"

  val exportedFunctions = mutable.ListBuffer.empty[ExportedSiblingFunction]

  override def init(options: List[String]): List[PluginPhase] =
    val outputDirectory =
      options
        .collectFirst {
          case option if option.startsWith("output:") =>
            option.stripPrefix("output:")
        }
        .getOrElse("target/python-abi")

    // val siblingPhaseOutput = 
    List(
      new SiblingPhase(exportedFunctions),
      new PythonInitPhase(outputDirectory, exportedFunctions),
    )
