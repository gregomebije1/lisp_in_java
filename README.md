# A Minimal Lisp In Java
## Features Supported

- **Atoms and Lists**: The interpreter can handle atoms (symbols or integers) and lists (S-expressions).
- **Primitive Operations**: The interpreter supports the following Lisp-like operations:
  - **`quote`**: Returns the expression as-is without evaluation.
  - **`atom`**: Returns `"t"` if the argument is an atom (not a list), otherwise returns `nil`.
  - **`eq`**: Compares two atoms for equality.
  - **`car`**: Returns the first element of a list.
  - **`cdr`**: Returns the tail of a list (everything except the first element).
  - **`cons`**: Combines two elements into a list.
  - Arithmetic operations (`+`, `-`, `*`, `/`).
  - **`cond`**: Basic conditionals, similar to an if-else structure.
- **REPL**: A simple Read-Eval-Print Loop where users can input Lisp expressions and get results printed.

## Example Usage

```shell
Lisp> (quote (a b c))
(a b c)
Lisp> (atom (quote a))
t
Lisp> (atom (quote (a b c)))
nil
Lisp> (car (quote (a b c)))
a
Lisp> (cdr (quote (a b c)))
(b c)
Lisp> (cons (quote a) (quote (b c)))
(a b c)
Lisp> (+ 3 4)
7
Lisp> (cond ((eq 1 2) (quote no)) ((eq 1 1) (quote yes)))
yes
```

## How it Works

- Tokenization and Parsing: The input Lisp expression is tokenized and parsed into Java's List<Object> structure (which represents a Lisp list). Atoms (symbols or numbers) are parsed separately.
- Evaluation: Each parsed expression is evaluated recursively based on its operator (e.g., +, atom, car). The eval function checks if the expression is a symbol, a number, or a list, and then processes it accordingly.
- Global Environment: A global environment (globalEnv) is used to store variable bindings, though in this simple interpreter it is left mostly empty. You can extend this for storing variable values and functions.

This is a basic Lisp interpreter, but it can be extended to support more advanced features such as user-defined functions, recursion, and better error handling.