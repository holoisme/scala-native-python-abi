package python

import python.cpython.PyFloatApi
import python.cpython.PyLongApi
import python.cpython.PyObjectApi

import scala.scalanative.unsafe.CVoidPtr
import scala.scalanative.unsafe.UnsafeRichInt

@feature("PyObject")
opaque type PyObject = CVoidPtr

object PyObject:

  def Null: PyObject = null

  def fromPtr(ptr: CVoidPtr): PyObject =
    ptr

  def fromInt(value: Int): PyObject =
    PyLongApi.PyLong_FromLong(value.toSize)

  extension (obj: PyObject)

    def ptr: CVoidPtr =
      obj

    def asPyString: PyString =
      PyString.fromObject(obj)

    def asString: String =
      PyString.fromObject(obj).asString

    def asInt: Int =
      PyLongApi.PyLong_AsInt(obj).toInt

    def asLong: Long =
      PyLongApi.PyLong_AsLongLong(obj).toLong

    def asFloat: Float =
      PyFloatApi.PyFloat_AsDouble(obj).toFloat
    
    def asDouble: Double =
      PyFloatApi.PyFloat_AsDouble(obj).toDouble

    def decref(): Unit =
      PyObjectApi.Py_DecRef(obj)

extension (value: String)
  def toPyString: PyString =
    PyString(value)

  def toPyObject: PyObject =
    PyString(value).asObject

extension (value: Int)
  def toPyObject: PyObject =
    PyLongApi.PyLong_FromLong(value.toSize)

