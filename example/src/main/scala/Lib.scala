import python.*


def hello() =
  println("Hello, world!")


def instanciate(): Array[Int] =
  val arr = new Array[Int](4)
  arr(0) = 0
  arr(1) = 111
  arr(2) = 222
  arr(3) = 333
  arr

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

def print_all(list: PyList[Int]): Unit =
  var index = 0
  while(index < list.length) {
    println(list(index))
    index += 1
  }

def sum(xs: PyList[Int]): Int =
  xs.toSeq.sum

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

def present(p: PyInstance): Unit =
  val fullName = p.call("full_name").asString
  val age = p.field("age").asInt

  p.call("say", s"Hello! My name is ${fullName} and I'm ${age}yo")

  p.setField("age", (age + 1).toPyObject)
  println(s"Happy birthday! ${fullName} is ${p.field("age").asInt} now.")


def getNamePlease(): String =
  "Bob"

def instanciate2(): Object =
  Seq(1, 2, 3)

def ex1(): (Int, String) =
  (12, "hello")

def ex2(): (Int, String, MyAnimal) =
  (12, "hello", MyAnimal(317))

class MyAnimal(val x: Int) {
  def _present(): Unit = println(x)
}

def safeDivide(a: Int, b: Int): Option[Int] =
  if b != 0 then Some(a / b)
  else None

// def doCrash() =
//   throw IllegalArgumentException("Hello this is an error")
  // try
  // catch {
  //   case _: Throwable => println("en fait ça va")
  // }


// class Animal(name: String, species: String):
//   def present() =
//     println(s"${name} is a ${species}")

// class Animal__abi(__refcount: Long, name: String, species: String):
//   def present() =
//     println(s"${name} is a ${species}")



// class Foo(val x: Int, val y: Int, val z: Int, val w: Int) {
//   @noinline def _sum(): Int =
//     this.x + this.y + this.z + this.w
// }

// @noinline private def newArray(): Object = new Array[Int](1225)
// // @noinline private def newString(i: Int): Object = s"Hello, world! ${i}"
// @noinline private def newString(i: Int): String = s"Hello, world! ${i}"

// @noinline private def newAnimal(): AnyRef = Cat("felix")
// @noinline private def newAnimal2(): AnyRef = Dog("max")

// def instanciate(): Int =
//   // val arr = new Array[Int](3170)
//   // generic_wrap(arr, 0, 3170, 0, 3170, true)
//   // System.out.println("Hello, world!")
//   println("Hello, world!")
//   3170
//   // val s = newString(100)
//   // s.length()
//   // 3170
//   // val animal1 = newAnimal()
//   // val animal2 = newAnimal2()
//   // callMyTrait(animal1) + callMyTrait(animal2)
//   // val s = newString(100)
//   // doGetLength(s)
//   // s.asInstanceOf[String].length()
//   // val c = Cat("felix")
//   // val d = Dog("max")

//   // callPresent(c) + callPresent(d)
//   // val s = newString()
//   // s.asInstanceOf[String].length()
//   // val l = newArray()
//   // l.asInstanceOf[Array[?]]
//   // l.length

//   // System.out.write('h')
//   // System.out.print("Hello")
//   // val a = new Array[Char](3170)
//   // checkByAny(a)
//   // val len = 3170
//   // val a = new Array[Char](len)
//   // val b = new Array[Char](len)
//   // val res = doCast(a, 0, b, 0, len)

//   // 0
//   // Array.copy(a, 0, b, 0, len)
//   // a.asInstanceOf[CharArray]
//   // // copy2(a, 0, b, 0, 8)
//   // System.out.println("Hello, world!")
//   // true
//   // val x = "Hello, world!"
//   // x.length
//   // buffer.isInstanceOf[Array[_]]
//   // val x = "Hello, world!"
//   // val charArray = x.toCharArray()
//   // charArray.isInstanceOf[Array[Char]]
//   // x.isInstanceOf[String]
//   // val foo = new Foo(3170, 1000, 1000, 1000)
//   // foo._sum().getClass()
//   // thingThing(foo)
//   // foo.isInstanceOf[Foo]
//   // // System.out.println(100)
//   // s"${foo.x}"

// def doPresent(_from: AnyRef): Int = {
//   _from.asInstanceOf[Animal]._present()
// }

// def doGetLength(_from: AnyRef): Int = {
//   _from.asInstanceOf[String].length()
// }

// def thingThing(_from: Object): Int = {
//   _from.asInstanceOf[Foo]._sum()
// }

// def checkByAny(_from: AnyRef): Boolean = {
//   _from.isInstanceOf[NativeArray[?]]
// }

// def checkByAny2(_from: Array[Char]): Object = {
//   _from.asInstanceOf[NativeArray[?]]
// }

// def copy2(_from: AnyRef, _fromPos: Int, _to: AnyRef, _toPos: Int, _len: Int): Unit = {
//     if (_from == null || _to == null) {
//       throw new NullPointerException()
//     } else if (!_from.isInstanceOf[Array[?]]) {
//       throw new IllegalArgumentException("from argument must be an array")
//     } else if (!_to.isInstanceOf[Array[?]]) {
//       throw new IllegalArgumentException("to argument must be an array")
//     } else {

//     }
// }

// // def doCast(_from: AnyRef, _fromPos: Int, _to: AnyRef, _toPos: Int, _len: Int): Int = {
// def doCast(_from: AnyRef, _fromPos: Int, _to: AnyRef, _toPos: Int, _len: Int): Char = {
//   val f = _from.asInstanceOf[NativeArray[Char]]
//   f(0)
//   // val x = f.length
//   // x + 317000
// }


// abstract class Animal(val name: String) {
//   def _present(): Int
// }

// class Cat(name: String) extends Animal(name) with MyTrait {

//   override def _my_trait_thing(): Int = name.length()

//   def _present(): Int = {
//     this.name.length() + 1
//   }
// }

// class Dog(name: String) extends Animal(name) with MyTrait {

//   override def _my_trait_thing(): Int = name.length()

//   def _present(): Int = {
//     this.name.length() + 2
//   }
// }

// trait MyTrait {
//   def _my_trait_thing(): Int
// }


// @noinline private def callPresent(a: Animal): Int = {
//   a._present()
// }

// @noinline private def callMyTrait(a: Object): Int = {
//   a.asInstanceOf[MyTrait]._my_trait_thing()
// }

// @noinline private def generic_wrap(
//     array: Array[Int],
//     arrayOffset: Int,
//     capacity: Int,
//     initialPosition: Int,
//     initialLength: Int,
//     isReadOnly: Boolean
// ): Int = {
//   if (capacity < 0) {
//     throw new IllegalArgumentException()
//   }
//   if (arrayOffset < 0 ||
//       arrayOffset + capacity > array.length)
//     throw new IndexOutOfBoundsException
//   val initialLimit = initialPosition + initialLength
//   if (initialPosition < 0 || initialLength < 0 || initialLimit > capacity)
//     throw new IndexOutOfBoundsException
//   initialLimit
// }


// class Foo(val x: Int, val y: Int, val z: Int, val w: Int) {

// }

// def instanciate() =
//   System.out.println(310)
  // val foo = new Foo(3170, 1000, 1000, 1000)
  // foo.getClass()

// var globalThing: Any

// def greet(n: Int): String =
//   s"Hello, ${n} !"


// def workPlease(i: Int) =
//   System.out.println(s"Work please ! ${i}")

// @noinline private def doCrash[T](implicit tag: Tag[T]): Int =
//   tag.size

// @noinline private def doCrash2[T](tag: Tag[T]): Int =
//   tag.size

// def hello(i: Int): Int =
//   val x = doCrash2(Tag.Int)
//   x
//   // val x = doCrash2(Tag.Int)
//   // x
//   // val x = doCrash[Int]
//   // // x
//   // x
//   // val p = stackalloc[Int](4)

//   // p.update(0, 10)
//   // p.update(1, 20)
//   // p.update(2, 30)
//   // p.update(3, 40)

//   // println(p(2))

//   // p
//   // System.out.println("Hello, world!")
//   // val r = (1209).toCSize
//   // Zone:
//   //   val r = toCString("hello")
//   //   r
//   // println(r.toString())
