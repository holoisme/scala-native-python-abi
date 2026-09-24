import example

print(example.add(100, 2))

example.hello()

print(example.first([111, 222, 333]))

name = example.getName()

print(name)


# Each GC does its own thing, and calls INCREF DECREF functions when the wrapper object is freed

class Person:
    def __init__(self, first_name, last_name, age):
        self.first_name = first_name
        self.last_name = last_name
        self.age = age

    def full_name(self):
        return f"{self.first_name} {self.last_name}"
    
    def set_age(self, age):
        self.age = age

    def say(self, sentence):
        print(f"{self.first_name}: {sentence}")

p = Person("Youssef", "Laraki", 21)

example.present(p)

# example.my_print("Hello, world! Yes")
# example.print_first(["hello", "world"])



# print(len(example.reflect([1, 2, 3])))

# example.print_all([1, 2, 3])








# from example import hello

# hello()


# import ctypes
# from pathlib import Path

# lib_path = Path("target/scala-3.3.7/libexample.so")

# lib = ctypes.CDLL(str(lib_path))

# # def add(a: int, b: int) -> int:
# # 	return lib.add(a, b)

# # def multiply(a: float, b: float) -> float:
# # 	return lib.multiply(a, b)

# # print(add(10, 2))
# # print(multiply(3.5, 2))

# # int add(int, int)
# lib.add.argtypes = [ctypes.c_int32, ctypes.c_int32]
# lib.add.restype = ctypes.c_int32

# # double multiply(double, double)
# lib.multiply.argtypes = [ctypes.c_double, ctypes.c_double]
# lib.multiply.restype = ctypes.c_double

# # void hello(void)
# lib.hello.argtypes = []
# lib.hello.restype = None

# print(lib.add(20, 22))
# print(lib.multiply(3.5, 2.0))

# lib.hello()
