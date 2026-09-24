package python.cpython

import python.PyObject

import scala.scalanative.unsafe.*

@extern
object PyLongApi:

  def PyLong_AsInt(obj: PyObject): CInt = extern
  
  def PyLong_AsLongLong(obj: PyObject): CLongLong = extern

  def PyLong_FromLong(value: CLong): PyObject = extern

extension (value: Int)
  def toPyObject: PyObject =
    PyLongApi.PyLong_FromLong(value.toSize)
