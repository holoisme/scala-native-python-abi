package python.cpython

import python.PyObject

import scala.scalanative.unsafe.*

@extern
object PyObjectApi:

  def PyObject_GetAttrString(
      obj: PyObject,
      attrName: CString
  ): PyObject = extern

  def PyObject_CallNoArgs(
      callable: PyObject
  ): PyObject = extern

  def PyObject_CallOneArg(
      callable: PyObject,
      arg: PyObject
  ): PyObject = extern

  def PyObject_Call(
      callable: PyObject,
      args: PyObject,
      kwargs: PyObject
  ): PyObject = extern