package python

import python.cpython.PyUnicodeApi

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

opaque type PyString = PyObject

object PyString:

  def fromObject(obj: PyObject): PyString =
    obj

  def toScalaString(obj: PyString): String =
    Zone:
      val size = alloc[CSSize]()

      val utf8 = PyUnicodeApi.PyUnicode_AsUTF8AndSize(obj, size)

      fromCStringSlice(utf8, size(0).toCSize)

  def apply(value: String): PyString =
    Zone:
      val utf8 = toCString(value)

      PyUnicodeApi.PyUnicode_FromString(utf8)
    
  extension (str: PyString)

    def asObject: PyObject = str

    def asString: String = toScalaString(str)
	
    // def toString: String = PyString.fromObject(str)


// final class PyStringWrapper private[python] (
//     private[python] val obj: PyObject
// ):
//   override def toString: String =
//     PyString.fromObject(obj)
