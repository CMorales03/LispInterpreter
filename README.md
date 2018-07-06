# Lisp Interpreter

A simple Lisp interpreter written in Java.

## Install

```
$ git clone https://github.com/pacocoursey/lispinterpreter.git
$ cd LispInterpreter
```

## Usage

Compile the code using the makefile:
```
$ make
```

Run the Lisp interpreter using:
```
$ java LispInterpreter
```

This will enter into an interactive Lisp prompt.

After typing `(quit)` the intermediate results will be outputted to a file "results.txt".

If you get errors, ensure you have correctly formatted your Lisp input.
i.e: `(+ (+ 2 3) 5)` will work correctly, `(+ (+2 3) 5)` will not.

#

<p align="center">
  <a href="http://paco.sh"><img src="https://raw.githubusercontent.com/pacocoursey/pacocoursey.github.io/master/footer.png" height="300"></a>
</p>
