#
# AUTO-GENERATED FILE
# Please do not modify this file.
#

import ctypes
import sys
from pathlib import Path
from typing import Any, Protocol, TypeVar

T = TypeVar('T')
C = TypeVar('C', covariant=True)

class Array(list[T]):
  pass

class Option(Protocol[C]):
  def get(self) -> C: ...
  def is_some(self) -> bool: ...
  def is_none(self) -> bool: ...

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

__lib.hello__abi.argtypes = []
__lib.hello__abi.restype = None
def hello() -> None:
  return __lib.hello__abi()


__lib.instanciate__abi.argtypes = []
__lib.instanciate__abi.restype = ctypes.py_object
def instanciate() -> Array[int]:
  return __lib.instanciate__abi()


__lib.add__abi.argtypes = [ctypes.c_int32, ctypes.c_int32]
__lib.add__abi.restype = ctypes.c_int32
def add(a: int, b: int) -> int:
  """
  Adds two numbers.
  
  @param a first number
  @param b second number
  @return the sum
  """
  if a < -2147483648 or a > 2147483647:
    raise ValueError(f'argument a of type int (-2147483648 -> 2147483647) is out of bounds ({a})')
  if b < -2147483648 or b > 2147483647:
    raise ValueError(f'argument b of type int (-2147483648 -> 2147483647) is out of bounds ({b})')
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


__lib.present__abi.argtypes = [ctypes.py_object]
__lib.present__abi.restype = None
def present(p: Any) -> None:
  return __lib.present__abi(p)


__lib.getNamePlease__abi.argtypes = []
__lib.getNamePlease__abi.restype = ctypes.py_object
def getNamePlease() -> str:
  return __lib.getNamePlease__abi()


__lib.instanciate2__abi.argtypes = []
__lib.instanciate2__abi.restype = ctypes.py_object
def instanciate2() -> Any:
  return __lib.instanciate2__abi()


__lib.ex1__abi.argtypes = []
__lib.ex1__abi.restype = ctypes.py_object
def ex1() -> tuple[int, str]:
  return __lib.ex1__abi()


__lib.ex2__abi.argtypes = []
__lib.ex2__abi.restype = ctypes.py_object
def ex2() -> tuple[int, str, Any]:
  return __lib.ex2__abi()


__lib.safeDivide__abi.argtypes = [ctypes.c_int32, ctypes.c_int32]
__lib.safeDivide__abi.restype = ctypes.py_object
def safeDivide(a: int, b: int) -> Option[int]:
  if a < -2147483648 or a > 2147483647:
    raise ValueError(f'argument a of type int (-2147483648 -> 2147483647) is out of bounds ({a})')
  if b < -2147483648 or b > 2147483647:
    raise ValueError(f'argument b of type int (-2147483648 -> 2147483647) is out of bounds ({b})')
  return __lib.safeDivide__abi(a, b)

