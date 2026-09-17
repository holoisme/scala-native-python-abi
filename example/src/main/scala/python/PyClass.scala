package python

import python.cpython.PyObjectApi

opaque type PyClass = PyObject

object PyClass:

  def fromObject(obj: PyObject): PyClass =
    obj

  extension (cls: PyClass)

    def asObject: PyObject =
      cls

    def apply(): PyObject =
      PyObjectApi.PyObject_CallNoArgs(cls)

    def apply(arg: PyObject): PyObject =
      PyObjectApi.PyObject_CallOneArg(cls, arg)