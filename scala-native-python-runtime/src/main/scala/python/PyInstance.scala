package python

import python.cpython.PyObjectApi
import python.cpython.PyUnicodeApi

import scala.language.dynamics
import scala.scalanative.unsafe.*
// import scala.scalanative.unsafe.Size.intToSize
import scala.scalanative.unsigned.UnsignedRichInt

@feature("PyInstance")
opaque type PyInstance = PyObject

object PyInstance:

  def fromObject(obj: PyObject): PyInstance =
    obj

  extension (obj: PyInstance)

    def asObject: PyObject =
      obj
    
    def call[A](methodName: String): PyObject =
      callArbitrary(methodName)

    def call[A](methodName: String, a: A)(using elemA: PyElement[A]): PyObject =
      val va = elemA.toPyObject(a)
      val result = callArbitrary(methodName, va)
      if elemA.needsImmediateCleanUp() then va.decref()
      result

    def call[A, B](methodName: String, a: A, b: B)(using elemA: PyElement[A])(using elemB: PyElement[B]): PyObject =
      val va = elemA.toPyObject(a)
      val vb = elemB.toPyObject(b)
      val result = callArbitrary(methodName, va, vb)
      if elemA.needsImmediateCleanUp() then va.decref()
      if elemB.needsImmediateCleanUp() then vb.decref()
      result

    def callArbitrary(methodName: String, args: PyObject*): PyObject =
      Zone:
        val name = PyUnicodeApi.PyUnicode_FromString(toCString(methodName))

        if name.ptr == null then
          PyObject.Null
        else
          val argv = stackalloc[CVoidPtr](args.length + 1)

          argv(0) = obj.ptr

          var i = 0
          while i < args.length do
            argv(i + 1) = args(i).ptr
            i += 1

          val result = PyObjectApi.PyObject_VectorcallMethod(
              name,
              argv,
              (args.length + 1).toCSize,
              PyObject.Null
            )

          PyObjectApi.Py_DecRef(name)

          result
      
    def field(fieldName: String): PyObject =
      Zone:
        PyObjectApi.PyObject_GetAttrString(
          obj,
          toCString(fieldName)
        )

    def setField(fieldName: String, value: PyObject): Boolean =
        Zone:
          val result =
            PyObjectApi.PyObject_SetAttrString(
              obj,
              toCString(fieldName),
              value
            )

          result == 0

    def setFieldNew[T](fieldName: String, value: T)(using element: PyElement[T]): Boolean =
        Zone:
          val obj = element.toPyObject(value)
          val result =
            PyObjectApi.PyObject_SetAttrString(
              obj,
              toCString(fieldName),
              obj
            )

          if element.needsImmediateCleanUp() then
            obj.decref()

          result == 0
    
    // def applyDynamic(name: String)(args: Any*): PyObject =
    //   // convert args -> PyObject
    //   // call Python method
    //   ???

    // def selectDynamic(name: String): PyObject = field(name)
    
    // def call(method: String): PyObject =
    //   Zone:
    //     println(s"Calling \"${method}\"...")
    //     val name = methodName(method)// PyUnicodeApi.PyUnicode_FromString(toCString(method))

    //     println("Created name")

    //     val args = stackalloc[CVoidPtr](1)
    //     args(0) = obj.ptr

    //     println("Created stackalloc, calling vector call...")

    //     val result =
    //       PyObjectApi.PyObject_VectorcallMethod(
    //         name,
    //         args,
    //         1.toUSize | PyVectorcallArgumentsOffset/*PY_VECTORCALL_ARGUMENTS_OFFSET*/,
    //         PyObject.Null
    //       )

    //     println("Return from PyObject_VectorcallMethod")

    //     PyObjectApi.Py_DecRef(name)

    //     result
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