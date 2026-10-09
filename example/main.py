import example
import numpy as np

n = example.getNamePlease()

print(n)


# import pandas as pd
# import matplotlib.pyplot as plt

# x = example.safeDivide(1000, 0)

# y = x
# print(x.is_none())
# print("PLease?")
# print(y)

# name = example.getNamePlease()
# print(f"{name}v")

# example.__lib.java_lang_Object_toString.argtypes = []
# example.__lib.java_lang_Object_toString.restype = None
# v = example.__lib.java_lang_Object_toString
# print(dir(example.__lib))

# p = example.Option(120)
# print(p)

# y = x + 1

# print(x)

# y = x.get()
#
# print(y)

# arr = example.instanciate()
#
# print(len(arr)) # length query
#
# print(arr[0]) # indexing
#
# for i in arr: # iteration
# 	print(i)
#
# av = np.average(arr)
# print(av)
#
# narray = np.array([42, 1997, 2004])
# example.print_all(narray)

# data = {'Year': [2000, 2001, 2002, 2003],'Unemployment Rate': arr}
# df = pd.DataFrame(data)

# df = pd.DataFrame(arr)

# df.plot(x='Year', y='Unemployment Rate', kind='line')
# plt.show()

# import psutil
# import resource

# process = psutil.Process()


# for i in range(100):
# 	x = example.instanciate()
# 	m = resource.getrusage(resource.RUSAGE_SELF).ru_maxrss
# 	# print(f"{process.memory_info().rss} bytes")
# 	print(f"{m} bytes")
# 	print(x)

# [a, b] = example.ex1()

# x = example.instanciate2()
# print(x)

# import numpy as np
# import gc

# gc.disable()

# example.workPlease(1)

# example.hello()

# example.doCrash()


# for i in x:
# 	print(i)
# print(x)

# print(len(x))
# av = np.average(x)
# print(x[0])
# print(x[1])
# print(x[2])
# print(av)

# a = [1, 2, 3]
# b = a / 1;

# for i in x:
#     print(i)
# print(len(x))
# print(example.add(100, 2))


# print(example.first([111, 222, 333]))

# name = example.getName(12)
# print(name)

# class Person:
#     def __init__(self, first_name, last_name, age):
#         self.first_name = first_name
#         self.last_name = last_name
#         self.age = age

#     def full_name(self):
#         return f"{self.first_name} {self.last_name}"

#     def say(self, sentence):
#         print(f"{self.first_name}: {sentence}")

# p = Person("Youssef", "Laraki", 21)

# example.present(p)


# class Animal:
#     name: str
#     species: str

#     def __init__(self, name, species):
#         self.__inner = 0 # Instanciate Scala class.

#     # Other this() constructors:
#     # def new(self, name):
#     #     self.__inner = 0 # Instanciate Scala class.

#     def __call__(self, args, kwds):
#         # apply()
#         pass

#     def __getattribute__(self, name):
#         # Read field from __inner
#         print(f"Accessing {name}")
#         pass

#     def __setattr__(self, name, value) -> None:
#         # Set field in __inner
#         pass

#     def __del__(self):
#         # Decrease reference count of __inner in Scala Native Object space

#         return

#     def present(self):
#         # Call __inner.present
#         pass

# animal = Animal("felix", "cat")
# print(animal.name)

# animal.present()




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
