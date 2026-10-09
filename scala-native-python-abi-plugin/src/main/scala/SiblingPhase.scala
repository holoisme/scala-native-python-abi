import TypeHelper.exceptionDefaultReturnValue
import dotty.tools.dotc.CompilationUnit
import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Annotations.Annotation
import dotty.tools.dotc.core.Comments.Comment
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Decorators.toTermName
import dotty.tools.dotc.core.Flags
import dotty.tools.dotc.core.NameKinds.TempResultName
import dotty.tools.dotc.core.StdNames
import dotty.tools.dotc.core.StdNames.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*
import dotty.tools.dotc.plugins.PluginPhase
import dotty.tools.dotc.plugins.StandardPlugin
import dotty.tools.dotc.report
import dotty.tools.dotc.typer.TyperPhase
import dotty.tools.dotc.util.EnumFlags.FlagSet
import dotty.tools.dotc.util.Spans

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class SiblingPhase(exportedFunctions: ListBuffer[ExportedSiblingFunction]) extends PluginPhase:

  override val phaseName: String = "sibling-phase"

  override val runsAfter: Set[String] = Set(TyperPhase.name)

  private val exportedAnnotation = "scala.scalanative.unsafe.exported"

  override def transformTypeDef(tree: tpd.TypeDef)(using ctx: Context): tpd.Tree =
    exportedFunctions.clear()

    tree.rhs match
      case template: tpd.Template =>
        val newBody =
          template.body.flatMap {
            case defDef: tpd.DefDef =>
              val isExported =
                defDef.symbol.annotations.find { annotation =>
                    annotation.symbol.fullName.toString == exportedAnnotation
                }.isDefined

              val isPrivate = defDef.symbol.isPrivate || defDef.symbol.privateWithin.exists

              if(isExported || isPrivate || defDef.name.show.startsWith("_"))
                List(defDef)
              else
                makeSibling(defDef) match
                  case Some(sibling) => {
                    println(s"${defDef.name.show} has a sibling")
                    exportedFunctions += ExportedSiblingFunction(defDef.name.show, defDef.symbol, sibling.name.show, sibling.symbol)
                    List(defDef, sibling)
                  }
                  case None => List(defDef)

            case other => List(other)
          }

        val newTemplate = cpy.Template(template)(body = newBody)

        cpy.TypeDef(tree)(rhs = newTemplate)

      case _ =>
        tree

  private def makeSibling(original: tpd.DefDef)(using ctx: Context): Option[tpd.DefDef] =
    val originalSym = original.symbol

    val exportedClass = requiredClass(exportedAnnotation)

    val methodType = originalSym.info match
      case tpe: MethodType => tpe
      case _ => return None

    val siblingName = originalSym.name ++ "__abi"

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

    siblingSym.addAnnotation(
      Annotation(exportedClass, tpd.New(
        exportedClass.typeRef,
        tpd.Literal(Constant(null)) :: Nil
      ), Spans.NoSpan)
    )

    val siblingDef = tpd.DefDef(siblingSym.asInstanceOf[dotty.tools.dotc.core.Symbols.TermSymbol])

    siblingDef.setComment(original.rawComment)

    val siblingParams = siblingDef.termParamss.flatten.map(_.symbol)

    val call = ref(originalSym).appliedToArgs(siblingParams.map(ref))

    // val exceptionHandled = wrapInTryCatch(call)

    val result = cpy.DefDef(siblingDef)(rhs = call)

    Some(result)

  private def wrapPythonException(call: tpd.GenericApply)(using Context): tpd.Tree = {
    val pyErrFromScalaSym: TermSymbol = requiredMethod("scala.scalanative.runtime.BubbleThrowable.bubbleExceptionToPython")

    val throwableTpe = defn.ThrowableType

    // The owner must be the generated sibling method here.
    val exceptionSym = newPatternBoundSymbol(TempResultName.fresh(), throwableTpe, call.span)

    val e = NamedType(throwableTpe, StdNames.nme.WILDCARD)

    val exceptionPat =
      Bind(
        exceptionSym,
        Typed(
          Ident(e).withType(throwableTpe),
          TypeTree(throwableTpe)
        )
      ).withType(exceptionSym.termRef)

    /*
    * scalanative_PyErr_FromScalaException(e)
    */
    val reportException = ref(pyErrFromScalaSym).appliedTo(ref(exceptionSym))

    /*
    * {
    *   scalanative_PyErr_FromScalaException(e)
    *   null
    * }
    */
    val handlerBody = Block(reportException :: Nil, Literal(TypeHelper.exceptionDefaultReturnValue(call.tpe)))

    val catchCase = CaseDef(exceptionPat, EmptyTree, handlerBody).withType(call.tpe)

    Try(call, catchCase :: Nil, EmptyTree).withType(call.tpe)
  }

  def wrapInTryCatch_leg(call: tpd.GenericApply)(using ctx: Context): tpd.Tree = {
    // 1. Resolve necessary references from the global definitions context
    val throwableType = defn.ThrowableType

    // 2. Create the symbol for the pattern variable 'e'
    // It requires Case and CaseAccessor flags to act correctly as a pattern variable
    val eSymbol = newSymbol(
      owner = ctx.owner,
      name = nme.x_0, // or nme.x_0 / any fresh name like "e".toTermName
      flags = Flags.Case | Flags.CaseAccessor,
      info = throwableType,
      coord = call.span
    )

    val e = NamedType(throwableType, StdNames.nme.WILDCARD)

    // 3. Construct the 'case e: Throwable => ...' pattern matching trees
    // Bind maps the incoming exception value to the eSymbol
    val bindPattern = tpd.Bind(eSymbol, tpd.Typed(Ident(e), tpd.TypeTree(throwableType)))

    // 4. Resolve the handleException helper method
    // Replace this lookup depending on where your handleException method lives
    val handleExceptionSymbol = requiredMethod("scala.scalanative.runtime.BubbleThrowable.bubbleExceptionToPython")
// currentOwner.topLevelClass.info.member(nme.handleException).symbol
    
    // Construct the statement: handleException(e)
    val handleExceptionCall = tpd.ref(handleExceptionSymbol).appliedTo(tpd.ref(eSymbol))

    // Construct the sequential block body: { handleException(e); null }
    val catchBody = tpd.Block(
      stats = List(handleExceptionCall),
      expr = tpd.Literal(Constant(null)).withType(defn.NullType)
    )

    val caseDef = tpd.CaseDef(bindPattern, tpd.EmptyTree, catchBody)

    tpd.Try(call, List(caseDef), tpd.EmptyTree)
  }


  // def wrapInTryCatch(call: tpd.GenericApply)(using ctx: Context): tpd.Tree = {
  //   // import dotty.tools.dotc.ast.tpd
  //   // import dotty.tools.dotc.core.Contexts.Context
  //   // import dotty.tools.dotc.core.Flags
  //   // import dotty.tools.dotc.core.Names.nme
  //   // import dotty.tools.dotc.core.Symbols.*
  //   // import dotty.tools.dotc.core.Constants.Constant
  //   // import dotty.tools.dotc.core.StdNames
    
  //   // 1. Resolve references
  //   val throwableType = defn.ThrowableType

  //   val uniqueName = freshTermName("e")

  //   "e".toTermName

  //   // 2. Create the symbol for variable 'e'
  //   val eSymbol = newSymbol(
  //     owner = ctx.owner,
  //     name = uniqueName,
  //     flags = Flags.Case | Flags.CaseAccessor,
  //     info = throwableType,
  //     coord = call.span
  //   )

  //   // 3. Construct the 'case e: Throwable => ...' pattern trees
  //   // Fix: Replace tpd.EmptyTree with a proper wildcard identity identifier. 
  //   // Under the hood, this compiles down to a clean typechecked pattern binding 
  //   // that Pickler can successfully serialize into TASTy.
  //   val wildcardIdent = tpd.Ident(throwableType)//.withType(throwableType)
  //   val bindPattern = tpd.Bind(eSymbol, tpd.Typed(wildcardIdent, tpd.TypeTree(throwableType)))

  //   // 4. Resolve the handleException helper method
  //   val handleExceptionSymbol = requiredMethod("scala.scalanative.runtime.BubbleThrowable.bubbleExceptionToPython")

  //   // Construct the statement: handleException(e)
  //   val handleExceptionCall = tpd.ref(handleExceptionSymbol).appliedTo(tpd.ref(eSymbol))

  //   // Construct the sequential block body: { handleException(e); null }
  //   val catchBody = tpd.Block(
  //     stats = List(handleExceptionCall),
  //     expr = tpd.Literal(Constant(null)).withType(defn.NullType)
  //   )

  //   // 5. Build the final CaseDef and Try blocks
  //   val caseDef = tpd.CaseDef(bindPattern, tpd.EmptyTree, catchBody)

  //   tpd.Try(call, List(caseDef), tpd.EmptyTree)
  // }


  //   import dotty.tools.dotc.ast.tpd
  // import dotty.tools.dotc.core.Contexts.Context
  // import dotty.tools.dotc.core.Flags
  // import dotty.tools.dotc.core.Names.nme
  // import dotty.tools.dotc.core.Symbols.*
  // import dotty.tools.dotc.core.Constants.Constant
  // import dotty.tools.dotc.core.StdNames

  // def wrapInTryCatch(call: tpd.GenericApply)(using ctx: Context): tpd.Tree = {
  //   // 1. Resolve references
  //   val throwableType = defn.ThrowableType

  //   // 2. Safely create the symbol for variable 'e'
  //   // Use symbols.newSymbol explicitly and ensure it tracks standard local variable boundaries
  //   val eSymbol = newSymbol(
  //     owner = ctx.owner,
  //     name = "e".toTermName, // Use a readable name or nme.x_0
  //     flags = Flags.Case | Flags.CaseAccessor,
  //     info = throwableType,
  //     coord = call.span
  //   )

  //   // 3. Construct the 'case e: Throwable => ...' pattern trees
  //   // Fix: In tpd, the typed pattern target uses an EmptyTree typecheck anchor 
  //   // rather than a synthesized NamedType(WILDCARD) identity.
  //   val bindPattern = tpd.Bind(eSymbol, tpd.Typed(tpd.EmptyTree, tpd.TypeTree(throwableType)))

  //   // 4. Resolve the handleException helper method
  //   val handleExceptionSymbol = requiredMethod("scala.scalanative.runtime.BubbleThrowable.bubbleExceptionToPython")

  //   // Construct the statement: handleException(e)
  //   // Ensure eSymbol is wrapped cleanly as a reference term
  //   val handleExceptionCall = tpd.ref(handleExceptionSymbol).appliedTo(tpd.ref(eSymbol))

  //   // Construct the sequential block body: { handleException(e); null }
  //   val catchBody = tpd.Block(
  //     stats = List(handleExceptionCall),
  //     expr = tpd.Literal(Constant(null)).withType(defn.NullType)
  //   )

  //   // 5. Build the final CaseDef and Try blocks
  //   val caseDef = tpd.CaseDef(bindPattern, tpd.EmptyTree, catchBody)

  //   tpd.Try(call, List(caseDef), tpd.EmptyTree)
  // }



  // private def appropriateDefaultReturnValue(tpe: Type): Constant = {
  //   if tpe.
  //   Constant(null)
  // }