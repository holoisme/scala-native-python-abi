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

def print_all(list: List[Int]): Unit =
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


def my_print(x: PyString): Unit =
  println(s"${x.asString}")

def print_first(xs: PyList[String]): Unit =
  // xs.toSeq.toList.map(_.length)
  println(s"${xs(0)}")

def hello(): Unit =
  println("Hello everyone!")

def greet_person(p: PyInstance): Unit =
  // val 
  p.call("greet")
