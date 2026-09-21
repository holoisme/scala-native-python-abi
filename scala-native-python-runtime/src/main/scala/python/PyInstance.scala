package python

import python.cpython.PyObjectApi
import python.cpython.PyUnicodeApi

import scala.scalanative.unsafe.*
// import scala.scalanative.unsafe.Size.intToSize
import scala.scalanative.unsigned.UnsignedRichInt

opaque type PyInstance = PyObject

object PyInstance:

  // private val PyVectorcallArgumentsOffset: CSize =
  // (1.toULong << (8 * sizeof[CSize] - 1)).toCSize

  private val PyVectorcallArgumentsOffset: CSize = (1.toULong << 63).toUSize

  def fromObject(obj: PyObject): PyInstance =
    obj

  def methodName(name: String): PyObject =
    Zone:
      val cstr = toCString(name)
      PyUnicodeApi.PyUnicode_FromStringAndSize(
        cstr,
        name.getBytes("UTF-8").length.toCSSize
      )

  extension (obj: PyInstance)

    def asObject: PyObject =
      obj

    def call(method: String): PyObject =
      Zone:
        println(s"Calling \"${method}\"...")
        val name = methodName(method)// PyUnicodeApi.PyUnicode_FromString(toCString(method))

        println("Created name")

        val args = stackalloc[CVoidPtr](1)
        args(0) = obj.ptr

        println("Created stackalloc, calling vector call...")

        val result =
          PyObjectApi.PyObject_VectorcallMethod(
            name,
            args,
            1.toUSize | PyVectorcallArgumentsOffset/*PY_VECTORCALL_ARGUMENTS_OFFSET*/,
            PyObject.Null
          )

        println("Return from PyObject_VectorcallMethod")

        PyObjectApi.Py_DecRef(name)

        result
        // val c = toCString(method)
        // val name = PyUnicodeApi.PyUnicode_FromString(c)

        // val result =
        //   PyObjectApi.PyObject_CallMethodNoArgs(
        //     obj,
        //     name
        //   )

        // PyObjectApi.Py_DecRef(name)

        // result

        // if name == null then
        //   null
        // else
        //   val result =
        //     PyObjectApi.PyObject_CallMethodNoArgs(
        //       obj,
        //       name
        //     )

        //   PyObjectApi.Py_DecRef(name)

        //   result
        
        // println(s"-- HERE 1 (${obj}, ${method})")
        // val methodName = toCString(method)
        // println("-- HERE 2")
        // val res = PyObjectApi.PyObject_CallMethodNoArgs(obj, methodName)
        // // val res = PyObjectApi.PyObject_CallMethod(obj, methodName, null)
        // println("-- HERE 3")
        // res
        // val methodName = toCString(method);

        // val callable = PyObjectApi.PyObject_GetAttrString(obj, methodName)

        // PyObjectApi.PyObject_CallNoArgs(callable)

        // PyObjectApi.PyObject_CallMethodNoArgs(obj, )

        // val name = methodName(method)
        // val methodName = // toCString(method)
        // println("Here 1")

        // val callable =
        //   PyObjectApi.PyObject_GetAttrString(
        //     obj,
        //     methodName
        //   )
        
        // println(s"Here 2 ${callable}")

        // PyObjectApi.PyObject_CallNoArgs(callable)

    // def call(
    //     method: String,
    //     arg: PyObject
    // ): PyObject =
    //   Zone:
    //     val methodName = toCString(method)

    //     val callable =
    //       PyObjectApi.PyObject_GetAttrString(
    //         obj,
    //         methodName
    //       )

    //     PyObjectApi.PyObject_CallOneArg(
    //       callable,
    //       arg
    //     )