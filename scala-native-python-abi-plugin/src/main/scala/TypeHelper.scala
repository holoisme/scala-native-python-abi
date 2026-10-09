
import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*

object TypeHelper:

  def typeToAbi(tpe: Type)(using ctx: Context): String =
    if tpe =:= defn.ByteType then "ctypes.c_int8"
    else if tpe =:= defn.ShortType then "ctypes.c_int16"
    else if tpe =:= defn.IntType then "ctypes.c_int32"
    else if tpe =:= defn.LongType then "ctypes.c_int64"
    else if tpe =:= defn.FloatType then "ctypes.c_float"
    else if tpe =:= defn.DoubleType then "ctypes.c_double"
    else if tpe =:= defn.BooleanType then "ctypes.c_bool"
    else if tpe =:= defn.UnitType then "None"
    else "ctypes.py_object"

  def typeToPythonIndication(tpe: Type)(using ctx: Context): String =
    if tpe =:= defn.ByteType then "int"
    else if tpe =:= defn.ShortType then "int"
    else if tpe =:= defn.IntType then "int"
    else if tpe =:= defn.LongType then "int"
    else if tpe =:= defn.FloatType then "float"
    else if tpe =:= defn.DoubleType then "float"
    else if tpe =:= defn.BooleanType then "bool"
    else if tpe =:= defn.StringType then "str"
    else if tpe =:= defn.UnitType then "None"
    else tpe.dealias match
      case AppliedType(tycon, List(a)) if tycon.typeSymbol == defn.OptionClass =>
        s"Option[${typeToPythonIndication(a)}]"
      case AppliedType(tycon, List(elem))
        if tycon.typeSymbol == defn.ArrayClass ||
           tycon.typeSymbol == defn.ListClass ||
           tycon.typeSymbol == defn.SeqClass =>
        s"Array[${typeToPythonIndication(elem)}]"
      case AppliedType(tycon, List(a)) if tycon.typeSymbol == defn.Tuple1 =>
        s"tuple[${typeToPythonIndication(a)}]"
      case AppliedType(tycon, List(a, b)) if tycon.typeSymbol == defn.Tuple2 =>
        s"tuple[${typeToPythonIndication(a)}, ${typeToPythonIndication(b)}]"
      case AppliedType(_, types) if defn.isDirectTupleNType(tpe) =>
        s"tuple[${types.map(typeToPythonIndication).mkString(", ")}]"
      case _ =>
        "Any"

  def exceptionDefaultReturnValue(tpe: Type)(using ctx: Context): Constant =
    if tpe =:= defn.ByteType then Constant(0)
    else if tpe =:= defn.ShortType then Constant(0)
    else if tpe =:= defn.IntType then Constant(0)
    else if tpe =:= defn.LongType then Constant(0L)
    else if tpe =:= defn.FloatType then Constant(0.0f)
    else if tpe =:= defn.DoubleType then Constant(0.0d)
    else if tpe =:= defn.BooleanType then Constant(false)
    else if tpe =:= defn.UnitType then Constant(())
    else Constant(null)

  def boundCheck(arg: String, typeName: String, min: String, max: String): String =
    s"if ${arg} < ${min} or ${arg} > ${max}:\n  raise ValueError(f'argument ${arg} of type ${typeName} (${min} -> ${max}) is out of bounds ({${arg}})')"

  def checkForType(tpe: Type, arg: String)(using ctx: Context): Option[String] =
    if tpe =:= defn.ByteType then Some(boundCheck(arg, "byte", s"${Byte.MinValue}", s"${Byte.MaxValue}"))
    else if tpe =:= defn.ShortType then Some(boundCheck(arg, "short", s"${Short.MinValue}", s"${Short.MaxValue}"))
    else if tpe =:= defn.IntType then Some(boundCheck(arg, "int", s"${Integer.MIN_VALUE}", s"${Integer.MAX_VALUE}"))
    else if tpe =:= defn.LongType then Some(boundCheck(arg, "long", "(-2**63)", "(2**63) - 1"))
    else if tpe =:= defn.FloatType then None // todo
    else if tpe =:= defn.DoubleType then None // todo
    else None

