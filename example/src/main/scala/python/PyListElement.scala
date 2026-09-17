package python

import python.cpython.PyLongApi
import python.cpython.PyFloatApi
import python.cpython.PyUnicodeApi
import scala.scalanative.unsigned.*

import scala.scalanative.unsafe.*

trait PyListElement[T]:
  def fromPyObject(obj: PyBorrowedObject): T

object PyListElement:

  given intElement: PyListElement[Int] with
    def fromPyObject(obj: PyBorrowedObject): Int =
      python.cpython.PyLongApi.PyLong_AsInt(obj.asObject)
  
  given longElement: PyListElement[Long] with
    def fromPyObject(obj: PyBorrowedObject): Long =
      PyLongApi.PyLong_AsLongLong(obj.asObject).toLong

  given doubleElement: PyListElement[Double] with
    def fromPyObject(obj: PyBorrowedObject): Double =
      PyFloatApi.PyFloat_AsDouble(obj.asObject)

  given floatElement: PyListElement[Float] with
    def fromPyObject(obj: PyBorrowedObject): Float =
      PyFloatApi.PyFloat_AsDouble(obj.asObject).toFloat
  
  given stringElement: PyListElement[String] with
    def fromPyObject(obj: PyBorrowedObject): String =
      Zone:
        "yayyay"
        // val size = alloc[CSSize]()

        // val utf8 =
        //   PyUnicodeApi.PyUnicode_AsUTF8AndSize(
        //     obj.asObject,
        //     size
        //   )

        // fromCStringSlice(utf8, size(0).toCSize)