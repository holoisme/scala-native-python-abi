package python

import python.cpython.PyObjectApi

import scala.scalanative.unsafe.*

opaque type PyInstance = PyObject

object PyInstance:

  def fromObject(obj: PyObject): PyInstance =
    obj

  extension (obj: PyInstance)

    def asObject: PyObject =
      obj

    def call(method: String): PyObject =
      Zone:
        val methodName = toCString(method)
        println("Here 1")

        val callable =
          PyObjectApi.PyObject_GetAttrString(
            obj,
            methodName
          )
        
        println(s"Here 2 ${callable}")

        PyObjectApi.PyObject_CallNoArgs(callable)

    def call(
        method: String,
        arg: PyObject
    ): PyObject =
      Zone:
        val methodName = toCString(method)

        val callable =
          PyObjectApi.PyObject_GetAttrString(
            obj,
            methodName
          )

        PyObjectApi.PyObject_CallOneArg(
          callable,
          arg
        )