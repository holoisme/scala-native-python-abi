import python.*

/**
 * Adds two numbers.
 *
 * @param a first number
 * @param b second number
 * @return the sum
 */
def add(a: Int, b: Int): Int =
  a + b

def multiply(a: Double, b: Double): Double =
  a * b

def print_all(list: Seq[Int]): Unit =
  for (i <- list) {
    println(i)
  }

def sum(xs: PyList[Int]): Int =
  println(s"Given length is ${xs.length}")
  println(s"First element is ${xs(0)}")
  420

def reflect(xs: PyList[Int]): PyList[Int] =
  xs

/**
  * Returns the first element of a list
  *
  * @param xs
  * @return xs[0]
  */
def first(xs: PyList[Int]): Int =
  println(xs.toSeq)
  xs(0)

def print_first(xs: PyList[String]): Unit =
  println(s"${xs(0)}")

def hello(): Unit =
  println("Hello everyone!")

def present(p: PyInstance): Unit =
  val fullName = p.call("full_name").asString
  val age = p.field("age").asInt
  
  p.call("say", Seq(PyString(s"Hello! My name is ${fullName} and I'm ${age}yo").asObject))

  // println(s"Hello! My name is ${fullName} and I'm ${age}yo")

  p.setField("age", PyObject.fromInt(age + 1))
  println(s"Happy birthday! ${fullName} is ${p.field("age").asInt} now.")

  // val newAge = PyObject.fromInt(age + 1)
  // p.call("set_age", Seq(newAge))
  // val age2 = p.field("age").asInt
  // println(s"Actually I'm ${age2}yo")
  // PyObjectApi.Py_DecRef(newAge)
  

def getName(): PyString =
  PyString("Bob")
