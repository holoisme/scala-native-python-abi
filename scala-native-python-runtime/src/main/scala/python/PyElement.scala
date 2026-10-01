package python

import python.cpython.PyFloatApi
import python.cpython.PyLongApi
import python.cpython.PyUnicodeApi

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

trait PyElement[T]:
  def fromPyObject(obj: PyObject): T
  def toPyObject(obj: T): PyObject
  def needsImmediateCleanUp(): Boolean = true

object PyElement:

  given intElement: PyElement[Int] with
    def fromPyObject(obj: PyObject): Int =
      python.cpython.PyLongApi.PyLong_AsInt(obj)
    def toPyObject(obj: Int): PyObject =
      PyLongApi.PyLong_FromLong(obj.toSize)
  
  given longElement: PyElement[Long] with
    def fromPyObject(obj: PyObject): Long =
      PyLongApi.PyLong_AsLongLong(obj).toLong
    def toPyObject(obj: Long): PyObject =
      PyLongApi.PyLong_FromLong(obj.toSize)

  given doubleElement: PyElement[Double] with
    def fromPyObject(obj: PyObject): Double =
      PyFloatApi.PyFloat_AsDouble(obj)
    def toPyObject(obj: Double): PyObject = ???

  given floatElement: PyElement[Float] with
    def fromPyObject(obj: PyObject): Float =
      PyFloatApi.PyFloat_AsDouble(obj).toFloat
    def toPyObject(obj: Float): PyObject = ???
  
  given stringElement: PyElement[String] with
    def fromPyObject(obj: PyObject): String =
      PyString.fromObject(obj).asString
    def toPyObject(obj: String): PyObject =
      PyString(obj).asObject