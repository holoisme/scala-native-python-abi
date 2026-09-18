package python.cpython

import scala.scalanative.unsafe.*

import python.PyObject

@extern
object PyFloatApi:

  def PyFloat_AsDouble(obj: PyObject): CDouble = extern