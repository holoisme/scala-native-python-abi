
import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.ast.tpd.*
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
    else "Any"

