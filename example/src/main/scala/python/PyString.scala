package python

import python.cpython.PyUnicodeApi

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

opaque type PyString = PyObject

// final class PyString private[python] (
//     private[python] val obj: PyObject
// ):
//   override def toString: String =
//     PyString.fromObject(obj)

object PyString:

  def toScalaString(obj: PyString): String =
    Zone:
      val size = alloc[CSSize]()

      val utf8 =
        PyUnicodeApi.PyUnicode_AsUTF8AndSize(
          obj,
          size
        )

      println(s"[Internal] string size is ${size(0).toCSize}")

      fromCStringSlice(
        utf8,
        size(0).toCSize
      )

  def apply(value: String): PyString =
    Zone:
      val utf8 = toCString(value)

      PyUnicodeApi.PyUnicode_FromStringAndSize(
        utf8,
        value.length.toCSSize
      )
    
  extension (str: PyString)

    def asObject: PyObject = str

    def asString: String = toScalaString(str)
	
    // def toString: String = PyString.fromObject(str)
