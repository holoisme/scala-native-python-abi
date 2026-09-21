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

case class ExportedSiblingFunction(
    originalName: String,
    originalSymbol: Symbol,

    siblingName: String,
    siblingSymbol: Symbol,
    // name: Symbol,
    // parameters: List[FunctionParameter],
    // returnType: Type,
    // comment: Option[Comment],
    // hasSibling: Boolean
    // symbol: Symbol
)
