
#
# AUTO-GENERATED FILE
# Please do not modify this file.
#

import ctypes
import sys
from pathlib import Path
from typing import Any

def _library_name():
    if sys.platform == "linux":
        return "libexample.so"

    if sys.platform == "darwin":
        return "libexample.dylib"

    if sys.platform == "win32":
        return "example.dll"

    raise RuntimeError(f"Unsupported platform: {sys.platform}")

_library_path = Path(__file__).parent / _library_name()
_library = ctypes.CDLL(str(_library_path))

_library.add.argtypes = [ctypes.c_int32, ctypes.c_int32]
_library.add.restype = ctypes.c_int32
def add(a: int, b: int) -> int:
  """
  Adds two numbers.
 
  @param a first number
  @param b second number
  @return the sum
  """
  return _library.add_abi(a, b)

_library.add_abi.argtypes = [ctypes.c_int32, ctypes.c_int32]
_library.add_abi.restype = ctypes.c_int32
def add_abi(a: int, b: int) -> int:
  return _library.add_abi(a, b)


_library.multiply.argtypes = [ctypes.c_double, ctypes.c_double]
_library.multiply.restype = ctypes.c_double
def multiply(a: float, b: float) -> float:
  return _library.multiply_abi(a, b)


_library.multiply_abi.argtypes = [ctypes.c_double, ctypes.c_double]
_library.multiply_abi.restype = ctypes.c_double
def multiply_abi(a: float, b: float) -> float:
  return _library.multiply_abi(a, b)


_library.print_all.argtypes = [ctypes.py_object]
_library.print_all.restype = None
def print_all(list: Any) -> None:
  return _library.print_all_abi(list)


_library.print_all_abi.argtypes = [ctypes.py_object]
_library.print_all_abi.restype = None
def print_all_abi(list: Any) -> None:
  return _library.print_all_abi(list)


_library.sum.argtypes = [ctypes.py_object]
_library.sum.restype = ctypes.c_int32
def sum(xs: Any) -> int:
  return _library.sum_abi(xs)


_library.sum_abi.argtypes = [ctypes.py_object]
_library.sum_abi.restype = ctypes.c_int32
def sum_abi(xs: Any) -> int:
  return _library.sum_abi(xs)


_library.reflect.argtypes = [ctypes.py_object]
_library.reflect.restype = ctypes.py_object
def reflect(xs: Any) -> Any:
  return _library.reflect_abi(xs)


_library.reflect_abi.argtypes = [ctypes.py_object]
_library.reflect_abi.restype = ctypes.py_object
def reflect_abi(xs: Any) -> Any:
  return _library.reflect_abi(xs)


_library.first.argtypes = [ctypes.py_object]
_library.first.restype = ctypes.c_int32
def first(xs: Any) -> int:
  """
  Returns the first element of a list
 
  @param xs
  @return xs[0]
  """
  return _library.first_abi(xs)


_library.first_abi.argtypes = [ctypes.py_object]
_library.first_abi.restype = ctypes.c_int32
def first_abi(xs: Any) -> int:
  return _library.first_abi(xs)


_library.my_print.argtypes = [ctypes.py_object]
_library.my_print.restype = None
def my_print(x: Any) -> None:
  return _library.my_print_abi(x)


_library.my_print_abi.argtypes = [ctypes.py_object]
_library.my_print_abi.restype = None
def my_print_abi(x: Any) -> None:
  return _library.my_print_abi(x)


_library.print_first.argtypes = [ctypes.py_object]
_library.print_first.restype = None
def print_first(xs: Any) -> None:
  return _library.print_first_abi(xs)


_library.print_first_abi.argtypes = [ctypes.py_object]
_library.print_first_abi.restype = None
def print_first_abi(xs: Any) -> None:
  return _library.print_first_abi(xs)


_library.hello.argtypes = []
_library.hello.restype = None
def hello() -> None:
  return _library.hello_abi()


_library.hello_abi.argtypes = []
_library.hello_abi.restype = None
def hello_abi() -> None:
  return _library.hello_abi()


_library.greet_person.argtypes = [ctypes.py_object]
_library.greet_person.restype = None
def greet_person(p: Any) -> None:
  return _library.greet_person_abi(p)


_library.greet_person_abi.argtypes = [ctypes.py_object]
_library.greet_person_abi.restype = None
def greet_person_abi(p: Any) -> None:
  return _library.greet_person_abi(p)

