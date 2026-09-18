package python.cpython

import scala.scalanative.unsafe.*

import python.PyObject

@extern
object PyLongApi:

  def PyLong_AsInt(obj: PyObject): CInt = extern
  
  def PyLong_AsLongLong(obj: PyObject): CLongLong = extern