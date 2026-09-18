package python

opaque type PyBorrowedObject = PyObject

object PyBorrowedObject:

  def fromObject(obj: PyObject): PyBorrowedObject =
    obj

  extension (obj: PyBorrowedObject)

    def asObject: PyObject =
      obj