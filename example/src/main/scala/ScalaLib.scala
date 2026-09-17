import python.*

import scala.scalanative.unsafe.*

object ScalaLib:
  
  @exported
  def add(a: Int, b: Int): Int =
    a + b

  @exported
  def multiply(a: Double, b: Double): Double =
    a * b

  @exported
  def print_all(list: Seq[Int]): Unit =
    for (i <- list) {
      println(i);
    }

  @exported
  def sum(xs: PyList[Int]): Int =
    println(s"Given length is ${xs.length}")
    println(s"First element is ${xs(0)}")
    420

  @exported
  def reflect(xs: PyList[Int]): PyList[Int] =
    xs

  @exported
  def first(xs: PyList[Int]): Int =
    // println(s"Returning ${xs(0)}")
    xs(0)

  
  @exported
  def my_print(x: PyString): Unit =
    println(s"${x.asString}")

  @exported
  def print_first(xs: PyList[String]): Unit =
    println(s"${xs(0)}")

  @exported
  def hello(): Unit =
    println("Hello everyone!")

  @exported
  def greet_person(p: PyInstance): Unit =
    // val 
    p.call("greet")
