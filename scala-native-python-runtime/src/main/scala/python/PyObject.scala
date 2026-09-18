package python

import scala.scalanative.unsafe.CVoidPtr

opaque type PyObject = CVoidPtr

object PyObject:

  def fromPtr(ptr: CVoidPtr): PyObject =
    ptr

  extension (obj: PyObject)

    def ptr: CVoidPtr =
      obj