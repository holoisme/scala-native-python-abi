package python

import python.cpython.PyListApi

import scala.collection.AbstractSeq

@feature("PyList")
opaque type PyList[T] = PyObject

object PyList:

  def fromObject[T](obj: PyObject): PyList[T] =
    obj

  extension [T](list: PyList[T])

    def asObject: PyObject =
      list
    
    def length: Int =
      PyListApi.PyList_Size(list).toInt

    inline def apply(index: Int)(using element: PyElement[T]): T =
      element.fromPyObject(
        PyListApi.PyList_GetItem(list, index)
      )

    def isValid: Boolean =
      PyListApi.PyList_Check(list) != 0
    
    def map[U](f: T => U)(using element: PyElement[T]): Seq[U] =
      val n = list.length
      val builder = Seq.newBuilder[U]

      var i = 0
      while i < n do
        builder += f(list(i))
        i += 1

      builder.result()

    def toSeq(using PyElement[T]): Seq[T] = PyListWrapper(list)

final class PyListWrapper[T](
    private val inner: PyList[T]
)(using PyElement[T])
    extends scala.collection.immutable.AbstractSeq[T]:

  override def length: Int =
    inner.length

  override def apply(index: Int): T =
    inner(index)

  override def iterator: Iterator[T] =
    new Iterator[T]:
      private var index = 0

      override def hasNext: Boolean =
        index < PyListWrapper.this.length

      override def next(): T =
        if !hasNext then
          throw new NoSuchElementException("next on empty iterator")

        val value = PyListWrapper.this(index)
        index += 1
        value


    // def toSeq()(using PyListElement[T]): Seq[T] = PyListWrapper(list)
    // def toSeq()(using element: PyListElement[T]): Seq[T] =
    //   val n = list.length
    //   val builder = Seq.newBuilder[T]

    //   var i = 0
    //   while i < n do
    //     builder += list(i)
    //     i += 1

    //   builder.result()
