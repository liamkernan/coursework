/**
 * StackApplication.java
 *
 * This class contains static methods that USE your SinglyLinkedStack<T>
 * to solve two classic stack problems:
 *
 * 1) Checking whether an expression has correct parentheses/brackets/braces.
 * 2) Converting an infix arithmetic expression to postfix (RPN).
 *
 * STUDENT TASK:
 *  - Implement each method by using SinglyLinkedStack appropriately.
 *  - Follow the TODO instructions inside each method.
 */
public class StackApplication {

    /**
     * Checks whether the given expression contains correctly matched
     * parentheses/brackets/braces: (), [], {}.
     *
     * Non‑bracket characters should be ignored.
     *
     * ERROR POLICY:
     *   - Return true if balanced.
     *   - Return false if anything is mismatched or if a closer appears
     *     when no matching opener exists.
     *
     * @param expr a string containing any characters
     * @return true if parentheses are balanced, false otherwise
     */
    public static boolean checkParentheses(String expr) {
        SinglyLinkedStack<Character> stack = new SinglyLinkedStack<>();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (c == ')' || c == ']' || c == '}') {
                if (stack.isEmpty()) {
                    return false;
                }
                char open = stack.pop();
                if (!isMatchingPair(open, c)) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    private static boolean isMatchingPair(char open, char close) {
        return (open == '(' && close == ')')
            || (open == '[' && close == ']')
            || (open == '{' && close == '}');
    }

    /**
     * Converts a standard arithmetic INFIX expression into POSTFIX (RPN).
     *
     * RULES STUDENTS MUST IMPLEMENT:
     *  ---------------------------------------------------------------
     *  - Supported operators: +, -, *, /, ^
     *  - Precedence: ^ highest, then * and /, then + and -
     *  - Associativity: ^ is RIGHT associative, everything else LEFT associative
     *  - Parentheses () override normal precedence
     *  - Operands may be:
     *        * Identifiers: letters or '_', followed by letters/digits/'_'
     *        * Numbers: digits, optionally containing ONE '.'
     *  - Whitespace should be ignored.
     *  - Output tokens should be separated by a SINGLE SPACE.
     *  - On malformed expressions (e.g., mismatched parentheses or invalid
     *    characters), return NULL.
     *
     * @param expr the infix expression as a String
     * @return the postfix expression (space-separated), or null if malformed
     */
    public static String infixToPostfix(String expr) {
        SinglyLinkedStack<String> opStack = new SinglyLinkedStack<>();
        StringBuilder output = new StringBuilder();
        int i = 0;

        while (i < expr.length()) {
            char c = expr.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            if (Character.isDigit(c)) {
                StringBuilder token = new StringBuilder();
                boolean hasDot = false;
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    if (expr.charAt(i) == '.') {
                        if (hasDot) break;
                        hasDot = true;
                    }
                    token.append(expr.charAt(i));
                    i++;
                }
                output.append(token).append(' ');
                continue;
            }

            if (Character.isLetter(c) || c == '_') {
                StringBuilder token = new StringBuilder();
                while (i < expr.length() && (Character.isLetterOrDigit(expr.charAt(i)) || expr.charAt(i) == '_')) {
                    token.append(expr.charAt(i));
                    i++;
                }
                output.append(token).append(' ');
                continue;
            }

            if (c == '(') {
                opStack.push("(");
                i++;
                continue;
            }

            if (c == ')') {
                boolean foundOpen = false;
                while (!opStack.isEmpty()) {
                    String top = opStack.pop();
                    if (top.equals("(")) {
                        foundOpen = true;
                        break;
                    }
                    output.append(top).append(' ');
                }
                if (!foundOpen) {
                    return null;
                }
                i++;
                continue;
            }

            if (isOperator(c)) {
                String op = String.valueOf(c);
                while (!opStack.isEmpty() && !opStack.peek().equals("(") && shouldPopOperator(opStack.peek(), op)) {
                    output.append(opStack.pop()).append(' ');
                }
                opStack.push(op);
                i++;
                continue;
            }

            return null;
        }

        while (!opStack.isEmpty()) {
            String top = opStack.pop();
            if (top.equals("(")) {
                return null;
            }
            output.append(top).append(' ');
        }

        return output.toString().trim();
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private static int precedence(String op) {
        if (op.equals("+") || op.equals("-")) return 1;
        if (op.equals("*") || op.equals("/")) return 2;
        if (op.equals("^")) return 3;
        return 0;
    }

    private static boolean isLeftAssociative(String op) {
        return !op.equals("^");
    }

    private static boolean shouldPopOperator(String stackOp, String incomingOp) {
        int stackPrec = precedence(stackOp);
        int incomingPrec = precedence(incomingOp);
        if (stackPrec > incomingPrec) return true;
        if (stackPrec == incomingPrec && isLeftAssociative(incomingOp)) return true;
        return false;
    }
}