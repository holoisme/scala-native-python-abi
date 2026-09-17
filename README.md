# High Performance Python Backend for Scala 3


Having a Python backend for Scala 3 makes accessible the rich Python ecosystem of model APIs, scientific computing, and ML tooling, which would position Scala 3 as a strong language for building agents and AI workflows. This project aims to develop such a backend with high performance and seamless interoperability. Existing attempts compile Scala 3 to Python source and interpret it: correct, but severely slow. This project takes a different route: compile Scala 3 through Scala Native to native code that will be linked directly with CPython's garbage collector and object heap. Sharing a single GC and heap lets Scala and Python objects reference each other directly, enabling efficient interop in both directions.


### Exploration

 - We want to explore linking LLVM-compiled binaries so that they are
accessible from Python. So maybe you can go ahead and try this
already? Write a little file in C/C++/Rust or Scala Native and link it
into CPython so that its methods can be called.

 - Study the nanonbind library (try what you can find out about it),
which provides some useful services for task 1.
 - Study the recently open sourced Mojo compiler, which uses a similar
strategy. It's big, so you'll have to find where
   the relevant parts are.
