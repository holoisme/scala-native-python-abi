package python

import python.cpython.PyListApi

opaque type PyList[T] = PyObject

object PyList:

  def fromObject[T](obj: PyObject): PyList[T] =
    obj

  extension [T](list: PyList[T])

    def asObject: PyObject =
      list
    
    def length: Int =
      PyListApi.PyList_Size(list).toInt

    inline def apply(index: Int)(using element: PyListElement[T]): T =
      element.fromPyObject(
        PyListApi.PyList_GetItem(list, index)
      )

    // def apply(index: Int): PyBorrowedObject =
    //   PyListApi.PyList_GetItem(list, index)

    def isValid: Boolean =
      PyListApi.PyList_Check(list) != 0


