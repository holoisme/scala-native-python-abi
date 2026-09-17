package python.cpython

import scala.scalanative.unsafe.*

import python.PyObject
import python.PyBorrowedObject

@extern
object PyListApi:

  def PyList_Check(list: PyObject): CInt = extern

  def PyList_Size(list: PyObject): CSSize = extern

  def PyList_GetItem(list: PyObject, index: CSSize): PyBorrowedObject = extern