import java.util.*;

public class Lisp {
    // Global environment (like Alist in Lisp)
    private static Map<String, Object> globalEnv = new HashMap<>();

    public static void main(String[] args) {
        // Repl - Read, Eval, Print Loop
        repl();
    }

    // Start the Read-Eval-Print Loop (REPL)
    public static void repl() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Simple Lisp Interpreter");
        while (true) {
            System.out.print("Lisp> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            Object expression = parse(input);
            Object result = eval(expression, globalEnv);
            System.out.println(printSExp(result));
        }
    }

    // Parsing S-expressions
    public static Object parse(String input) {
        List<String> tokens = tokenize(input);
        return readFromTokens(new LinkedList<>(tokens));
    }

    public static List<String> tokenize(String input) {
        input = input.replace("(", " ( ").replace(")", " ) ");
        return Arrays.asList(input.trim().split("\\s+"));
    }

    public static Object readFromTokens(Queue<String> tokens) {
        if (tokens.isEmpty()) {
            throw new RuntimeException("Unexpected EOF");
        }
        String token = tokens.poll();
        if ("(".equals(token)) {
            List<Object> list = new ArrayList<>();
            while (!tokens.peek().equals(")")) {
                list.add(readFromTokens(tokens));
            }
            tokens.poll();  // Remove closing ')'
            return list;
        } else if (")".equals(token)) {
            throw new RuntimeException("Unexpected ')'");
        } else {
            return atom(token);
        }
    }

    // Handle atoms (numbers, symbols)
    public static Object atom(String token) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            return token;  // Return as symbol
        }
    }

    // Evaluation of expressions
    public static Object eval(Object exp, Map<String, Object> env) {
        if (exp instanceof String) { // Variable
            String symbol = (String) exp;
            if (env.containsKey(symbol)) {
                return env.get(symbol);
            } else {
                throw new RuntimeException("Unbound symbol: " + symbol);
            }
        } else if (!(exp instanceof List)) {
            return exp;  // Return the value if it's a number
        }

        List<?> list = (List<?>) exp;
        String operator = (String) list.get(0);

        switch (operator) {
            case "quote":  // '(exp)
                return list.get(1);

            case "atom":   // (atom exp)
                return (eval(list.get(1), env) instanceof List) ? null : "t";

            case "eq":     // (eq exp1 exp2)
                Object arg1 = eval(list.get(1), env);
                Object arg2 = eval(list.get(2), env);
                return arg1.equals(arg2) ? "t" : null;

            case "car":    // (car exp)
                return ((List<?>) eval(list.get(1), env)).get(0);

            case "cdr":    // (cdr exp)
                return ((List<?>) eval(list.get(1), env)).subList(1, ((List<?>) eval(list.get(1), env)).size());

            case "cons":   // (cons exp1 exp2)
                Object first = eval(list.get(1), env);
                Object second = eval(list.get(2), env);
                List<Object> resultList = new ArrayList<>();
                resultList.add(first);
                if (second instanceof List) {
                    resultList.addAll((List<?>) second);
                } else {
                    resultList.add(second);
                }
                return resultList;

            case "+":
                return (int) eval(list.get(1), env) + (int) eval(list.get(2), env);

            case "-":
                return (int) eval(list.get(1), env) - (int) eval(list.get(2), env);

            case "*":
                return (int) eval(list.get(1), env) * (int) eval(list.get(2), env);

            case "/":
                return (int) eval(list.get(1), env) / (int) eval(list.get(2), env);

            case "cond":   // (cond (test exp) ...)
                for (int i = 1; i < list.size(); i++) {
                    List<?> clause = (List<?>) list.get(i);
                    if (eval(clause.get(0), env) != null) {
                        return eval(clause.get(1), env);
                    }
                }
                return null;

            default:
                throw new RuntimeException("Unknown operator: " + operator);
        }
    }

    // Function to convert the result back to a string (for printing)
    public static String printSExp(Object exp) {
        if (exp instanceof List) {
            List<?> list = (List<?>) exp;
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < list.size(); i++) {
                sb.append(printSExp(list.get(i)));
                if (i != list.size() - 1) sb.append(" ");
            }
            sb.append(")");
            return sb.toString();
        } else if (exp == null) {
            return "nil";
        } else {
            return exp.toString();
        }
    }
}
