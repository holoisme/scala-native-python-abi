import python.*

import scala.scalanative.unsafe.*

/**
 * Adds two numbers.
 *
 * @param a first number
 * @param b second number
 * @return the sum
 */
@exported
def add(a: Int, b: Int): Int =
  a + b

@exported
def multiply(a: Double, b: Double): Double =
  a * b

@exported
def print_all(list: Seq[Int]): Unit =
  for (i <- list) {
    println(i)
  }

@exported
def sum(xs: PyList[Int]): Int =
  println(s"Given length is ${xs.length}")
  println(s"First element is ${xs(0)}")
  420

@exported
def reflect(xs: PyList[Int]): PyList[Int] =
  xs

/**
  * Returns the first element of a list
  *
  * @param xs
  * @return xs[0]
  */
@exported
def first(xs: PyList[Int]): Int =
  xs(0)


@exported
def my_print(x: PyString): Unit =
  println(s"${x.asString}")

@exported
def print_first(xs: PyList[String]): Unit =
  // xs.toSeq.toList.map(_.length)
  println(s"${xs(0)}")

@exported
def hello(): Unit =
  println("Hello everyone!")

@exported
def greet_person(p: PyInstance): Unit =
  // val 
  p.call("greet")
