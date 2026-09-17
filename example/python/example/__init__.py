from ._loader import load_module

_functions = load_module()

globals().update(_functions)