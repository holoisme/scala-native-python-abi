#
# AUTO-GENERATED FILE
# Please do not modify this file.
#

import ctypes
import sys
from pathlib import Path
from typing import Any

def __library_name():
  if sys.platform == "linux":
    return "libexample.so"

  if sys.platform == "darwin":
    return "libexample.dylib"

  if sys.platform == "win32":
    return "example.dll"

  raise RuntimeError(f"Unsupported platform: {sys.platform}")

__library_path = Path(__file__).parent / __library_name()
__lib = ctypes.PyDLL(str(__library_path))

__lib.add__abi.argtypes = [ctypes.c_int32, ctypes.c_int32]
__lib.add__abi.restype = ctypes.c_int32
def add(a: int, b: int) -> int:
  """
  Adds two numbers.
  
  @param a first number
  @param b second number
  @return the sum
  """
  return __lib.add__abi(a, b)


__lib.multiply__abi.argtypes = [ctypes.c_double, ctypes.c_double]
__lib.multiply__abi.restype = ctypes.c_double
def multiply(a: float, b: float) -> float:
  return __lib.multiply__abi(a, b)


__lib.print_all__abi.argtypes = [ctypes.py_object]
__lib.print_all__abi.restype = None
def print_all(list: Any) -> None:
  return __lib.print_all__abi(list)


__lib.sum__abi.argtypes = [ctypes.py_object]
__lib.sum__abi.restype = ctypes.c_int32
def sum(xs: Any) -> int:
  return __lib.sum__abi(xs)


__lib.reflect__abi.argtypes = [ctypes.py_object]
__lib.reflect__abi.restype = ctypes.py_object
def reflect(xs: Any) -> Any:
  return __lib.reflect__abi(xs)


__lib.first__abi.argtypes = [ctypes.py_object]
__lib.first__abi.restype = ctypes.c_int32
def first(xs: Any) -> int:
  """
  Returns the first element of a list
  
  @param xs
  @return xs[0]
  """
  return __lib.first__abi(xs)


__lib.print_first__abi.argtypes = [ctypes.py_object]
__lib.print_first__abi.restype = None
def print_first(xs: Any) -> None:
  return __lib.print_first__abi(xs)


__lib.hello__abi.argtypes = []
__lib.hello__abi.restype = None
def hello() -> None:
  return __lib.hello__abi()


__lib.present__abi.argtypes = [ctypes.py_object]
__lib.present__abi.restype = None
def present(p: Any) -> None:
  return __lib.present__abi(p)


__lib.getName__abi.argtypes = []
__lib.getName__abi.restype = ctypes.py_object
def getName() -> Any:
  return __lib.getName__abi()
