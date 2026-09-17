package python.cpython

import python.PyObject

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

@extern
object PyUnicodeApi:

  def PyUnicode_FromStringAndSize(
      str: CString,
      size: CSSize
  ): PyObject = extern

  def PyUnicode_Check(obj: PyObject): CInt = extern

  def PyUnicode_GetLength(obj: PyObject): CSSize = extern

  def PyUnicode_AsUTF8AndSize(
      obj: PyObject,
      size: Ptr[CSSize]
  ): CString = extern
