import ctypes
import json
import sys
from pathlib import Path


_ABI_TYPES = {
    "i8": ctypes.c_int8,
    "i16": ctypes.c_int16,
    "i32": ctypes.c_int32,
    "i64": ctypes.c_int64,
    "f32": ctypes.c_float,
    "f64": ctypes.c_double,
    "bool": ctypes.c_bool,
    "void": None,

    "object": ctypes.py_object,
    "pyobject": ctypes.py_object
}


def _library_name():
    if sys.platform == "linux":
        return "libexample.so"

    if sys.platform == "darwin":
        return "libexample.dylib"

    if sys.platform == "win32":
        return "example.dll"

    raise RuntimeError(
        f"Unsupported platform: {sys.platform}"
    )


def _load_library():
    package_dir = Path(__file__).parent
    library = package_dir / _library_name()

    return ctypes.CDLL(str(library))


def _load_exports():
    package_dir = Path(__file__).parent
    metadata = package_dir / "exports.json"

    with metadata.open(encoding="utf-8") as f:
        return json.load(f)


def _configure_function(lib, spec):
    name = spec["name"]

    function = getattr(lib, name)

    function.argtypes = [
        _ABI_TYPES[type_name]
        for type_name in spec["parameters"]
    ]

    function.restype = _ABI_TYPES[spec["return"]]

    return function


def load_module():
    lib = _load_library()
    metadata = _load_exports()

    # print(lib.add2)

    functions = {}

    for spec in metadata["functions"]:
        functions[spec["name"]] = _configure_function(
            lib,
            spec,
        )

    return functions