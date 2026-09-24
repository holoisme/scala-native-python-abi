package python.cpython

import python.PyObject

import scala.scalanative.unsafe.*

@extern
object PyObjectApi:

  def PyObject_GetAttrString(
      obj: PyObject,
      attrName: CString
  ): PyObject = extern

  def PyObject_SetAttrString(
      obj: PyObject,
      attrName: CString,
      value: PyObject
  ): CInt = extern

  def PyObject_CallNoArgs(
      callable: PyObject
  ): PyObject = extern

  def PyObject_CallOneArg(
      callable: PyObject,
      arg: PyObject
  ): PyObject = extern

  def PyObject_CallMethod(
      obj: PyObject,
      name: CString,
      format: CString
  ): PyObject = extern

  def PyObject_CallMethodNoArgs(
      obj: PyObject,
      name: PyObject
  ): PyObject = extern

  def Py_DecRef(
      obj: PyObject
  ): Unit = extern

  def PyObject_VectorcallMethod(
      name: PyObject,
      args: Ptr[CVoidPtr],
      nargsf: CSize,
      kwnames: PyObject
  ): PyObject = extern

//   def PyObject_CallMethodNoArgs(
//       obj: PyObject,
//       name: PyObject
//   ): PyObject = extern

//   def PyObject_CallMethodOneArg(
//       obj: PyObject,
//       name: PyObject,
//       arg: PyObject
//   ): PyObject = extern

  def PyObject_Call(
      callable: PyObject,
      args: PyObject,
      kwargs: PyObject
  ): PyObject = extern
  