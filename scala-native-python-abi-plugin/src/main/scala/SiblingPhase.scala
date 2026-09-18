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

class SiblingPhase(functionsWithSiblings: ListBuffer[Symbol]) extends PluginPhase:

  override val phaseName: String = "sibling-phase"

  override val runsAfter: Set[String] = Set(TyperPhase.name)

  private val exportedAnnotation = "scala.scalanative.unsafe.exported"

  override def transformTypeDef(tree: tpd.TypeDef)(using ctx: Context): tpd.Tree =
    tree.rhs match
      case template: tpd.Template =>
        val newBody =
          template.body.flatMap {
            case defDef: tpd.DefDef =>
              val isExported =
                defDef.symbol.annotations.find { annotation =>
                    annotation.symbol.fullName.toString == exportedAnnotation
                }.isDefined
              
              if (isExported)
                makeSibling(defDef) match
                  case Some(sibling) => {
                    functionsWithSiblings += defDef.symbol
                    List(defDef, sibling)
                  }
                  case None => List(defDef)
              else
                List(defDef)

            case other => List(other)
          }

        val newTemplate = cpy.Template(template)(body = newBody)

        cpy.TypeDef(tree)(rhs = newTemplate)

      case _ =>
        tree

  private def makeSibling(original: tpd.DefDef)(using ctx: Context): Option[tpd.DefDef] =
    val originalSym = original.symbol

    val methodType = originalSym.info.asInstanceOf[MethodType]

    val siblingName = originalSym.name ++ "_abi"

    val siblingSym =
        newSymbol(
          owner = originalSym.owner,
          name = siblingName,
          flags = originalSym.flags,
          info = MethodType.fromSymbols(
              originalSym.paramSymss.flatten,
              methodType.resultType
          )
        )

    // siblingSym.copySymDenotation(
    //   annotations = originalSym.annotations
    // )

    siblingSym.annotations = originalSym.annotations

    val siblingDef = tpd.DefDef(siblingSym.asInstanceOf[dotty.tools.dotc.core.Symbols.TermSymbol])

    val siblingParams = siblingDef.termParamss.flatten.map(_.symbol)

    val call = ref(originalSym).appliedToArgs(siblingParams.map(ref))

    val result = cpy.DefDef(siblingDef)(
        rhs = call
    )

    println(s"RESULT OF SIBLING PHASE ${siblingName.show}")
    println(result)

    Some(result)

// class SiblingPhaseOutput:
//   val functionsWithSiblings = mutable.ListBuffer.empty[Symbol]
