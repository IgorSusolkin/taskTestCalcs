package by.intervale.test.task.beans;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;

import by.intervale.test.task.exeptions.TokenExeption;
/**
 * калькулятор с операциями "+", "-", "*", "/" , "^", "%" и скобками.
 * вычисление производится путем преобразования стоки в массив в обр польскую нотацию
 * методом сорт станции
 * @author Igor Susolkin
 *
 */
public class AdvCalc extends SimplCalc {
    private static final String OPENED_BRACKET = "(";
    private static final String CLOSED_BRACKET = ")";

    public AdvCalc() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String exec(String line) {
        // TODO Auto-generated method stub
        try {
            String[] rpn = getReversePolish(line);
            return reversePolishToResult(rpn);
        } catch (NumberFormatException e) {
            return "Wrong number format ";
        } catch (ArithmeticException e) {
            return "Division by zero";
        } catch (TokenExeption e) {
            return e.getMessage();
        }
    }

    protected String[] getReversePolish(String line) throws TokenExeption {
        final Stack<String> operatorStack = new Stack<>();
        final Queue<String> outputQueue = new LinkedList<>();
        String expression = line.replace(" ", "");
        final String delimiters = getDelimiters();
        final boolean returnDelims = true;
        StringTokenizer stringTokenizer = new StringTokenizer(expression, delimiters, returnDelims);
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            if (isNumeric(token)) {
                outputQueue.add(token);
            } else if (isConditionAddStack(token)) {
                operatorStack.push(token);
            } else if (isClosedBracket(token)) {
                while (!operatorStack.isEmpty()
                        && !isOpenedBracket(operatorStack.peek())) {
                    outputQueue.add(operatorStack.pop());
                }
                if (!operatorStack.isEmpty() && isOpenedBracket(operatorStack.peek())) {
                    operatorStack.pop(); // pop and discard closed bracket.
                } else {
                    throw new TokenExeption("Opened bracket is not found");
                }
            } else if (isOperator(token)) {
                final boolean tokenLeftAssociative = Operatop.isLeftAssociative(token);
                boolean condition = true;
                while (!operatorStack.isEmpty() && condition) {
                    final String token2 = operatorStack.peek();
                    final int tokenPrecedence = Operatop.getPrecedence(token);
                    final int token2Precedence = Operatop.getPrecedence(token2);
                    condition = (tokenLeftAssociative
                            && tokenPrecedence <= token2Precedence)
                            || (!tokenLeftAssociative
                            && tokenPrecedence < token2Precedence);
                    if (condition) {
                        outputQueue.add(operatorStack.pop());
                    }
                }
                operatorStack.push(token);
            } else {
                throw new TokenExeption("Unknown token : " + token);
            }
        }

        while (!operatorStack.isEmpty()) { // remainder
            final String token = operatorStack.pop();
            if (isOpenedBracket(token)) {
                throw new TokenExeption("Opened bracket is very much");
            } else if (isClosedBracket(token)) {
                throw new TokenExeption("Closed bracket is very much");
            } else {
                outputQueue.add(token);
            }
        }
        return outputQueue.toArray(new String[] {});
    }

    protected String reversePolishToResult(final String[] args) throws TokenExeption {
        final Stack<String> expression = new Stack<String>();

        for (final String token : args) {
            if (isNumeric(token)) {
                expression.push(token);
            } else {
                handlerOperatorAndFunction(token, expression);
            }
        }
        if (expression.size() == 1) {
            BigDecimal bd = new BigDecimal(expression.pop());
            return getSetFormat(bd).toString();
        } else {
            throw new TokenExeption("Expression is not correctly");
        }
    }

    protected void handlerOperatorAndFunction(final String token,
            final Stack<String> expression) throws TokenExeption {
        if (isOperator(token)) {
            final BigDecimal op2 = getNumberFromStack(token, expression);
            final BigDecimal op1;
            if (!expression.isEmpty() && isNumeric(expression.peek())) {
                op1 =  new BigDecimal(expression.pop());
            } else {
                op1 = BigDecimal.ZERO;
            }
            final BigDecimal result = getResultByTokens(token, op1, op2);
            expression.push(result.toString());
        }
    }

    protected BigDecimal getNumberFromStack(final String token,
            final Stack<String> expression) throws TokenExeption {
        if (!expression.isEmpty() && isNumeric(expression.peek())) {
            return new BigDecimal(expression.pop());
        } else {
            throw new TokenExeption("Operation is not correct" + token);
        }
    }

    protected boolean isConditionAddStack(final String token) {
        return isOpenedBracket(token);
    }


    @Override
    protected String getDelimiters() {
        // TODO Auto-generated method stub
        return super.getDelimiters() + OPENED_BRACKET + CLOSED_BRACKET;
    }

    protected boolean isOpenedBracket(final String token) {
        return token.equals(OPENED_BRACKET);
    }

    protected boolean isClosedBracket(final String token) {
        return token.equals(CLOSED_BRACKET);
    }
    // класс содержит приоритеты и ассоциативность для операторов
    protected static class Operatop {
        public static int getPrecedence(final String token) throws TokenExeption {
            switch (token) {
            case "+" : return 1;
            case "-" : return 1;
            case "*" : return 2;
            case "/" : return 2;
            case "%" : return 2;
            case "^" : return 3;
            case "√" : return 3;
            case "(" : return 0;
            case ")" : return 0;
            default: throw new TokenExeption("Unknown priority for operator " + token);
            }
        }

        public static boolean isLeftAssociative(final String token) throws TokenExeption {
            switch (token) {
            case "+" : return true;
            case "-" : return true;
            case "*" : return true;
            case "/" : return true;
            case "%" : return true;
            case "^" : return false;
            case "√" : return false;
            default: throw new TokenExeption("Unknown associative for operator" + token);
            }
        }
    }
}
