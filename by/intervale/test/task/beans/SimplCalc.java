package by.intervale.test.task.beans;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.StringTokenizer;

import by.intervale.test.task.exeptions.TokenExeption;
/**
 * ������� ����������� � ���������� "+", "-", "*", "/" , "^", "%"
 * @author Igor Susolkin
 *
 */
public class SimplCalc extends AbstractCalc {
    private static final String[] OPERATORS = {"+", "-", "*", "/" , "^", "%"};
    private BigDecimal memory;
    private String operator;

    public SimplCalc() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String exec(final String line) {
        memory = BigDecimal.ZERO;
        operator = null;
        String expression = line.replace(" ", "");
        final String delimiters = getDelimiters();
        final boolean returnDelims = true;
        StringTokenizer stringTokenizer =
                new StringTokenizer(expression, delimiters, returnDelims);
        try {
            while (stringTokenizer.hasMoreTokens()) {
                String token = stringTokenizer.nextToken();
                parserCycle(token);
            }
        } catch (ArithmeticException e) {
            return "Division by zero";
        } catch (TokenExeption e) {
            return e.getMessage();
        }
        return getSetFormat(memory).toString();
    }

    protected void parserCycle(final String token) throws TokenExeption {
         if (isOperator(token)) {
            operator = token;
        } else if (isNumeric(token)) {
            final BigDecimal value = new BigDecimal(token);
            if (operator == null) {
                memory = value;
            } else {
                memory = getResultByTokens(operator, memory, value);
            }
        } else {
            throw new TokenExeption("Unknown token : " + token);
        }
    }

    protected BigDecimal getSetFormat(final BigDecimal bd) {
        BigDecimal value = bd.setScale(5, RoundingMode.HALF_UP);
        value = value.stripTrailingZeros();
        return value;
    }

    protected boolean isNumeric(final String token) {
        final String regexNumeric = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
        return token.matches(regexNumeric);
    }

    protected boolean isOperator(final String token) {
        for (String item : OPERATORS) {
            if (item.equals(token)) {
                return true;
            }
        }
        return false;
    }

    protected String getDelimiters() {
        return getStringsToString(OPERATORS);
    }

    protected String getStringsToString(final String[] args) {
        StringBuilder strBuilder = new StringBuilder();
        for (int number = 0; number < args.length; number++) {
           strBuilder.append(args[number]);
        }
        return strBuilder.toString();
    }

    protected BigDecimal getResultByTokens(final String operator,
            final BigDecimal ... args) throws TokenExeption {
        switch (operator) {
        case "+" : return add(args[0], args[1]);
        case "-" : return sub(args[0], args[1]);
        case "*" : return mul(args[0], args[1]);
        case "/" : return div(args[0], args[1]);
        case "%" : return percentOfNumber(args[0], args[1]);
        case "^" : return pow(args[0], args[1]);
        default: throw new TokenExeption("Unknown operator :" + operator);
        }
    }

    protected BigDecimal add(final BigDecimal a, final BigDecimal b) {
        return a.add(b);
    }

    protected BigDecimal sub(final BigDecimal a, final BigDecimal b) {
        return a.subtract(b);
    }

    protected BigDecimal mul(final BigDecimal a, final BigDecimal b) {
        return a.multiply(b);
    }

    protected BigDecimal div(final BigDecimal a, final BigDecimal b) {
        return a.divide(b, 5, BigDecimal.ROUND_HALF_UP);
    }

    protected BigDecimal percentOfNumber(final BigDecimal a, final BigDecimal b) {
        final BigDecimal percent = new BigDecimal("0.01");
        return mul(mul(b, a), percent); //b * a / 100
    }

    protected BigDecimal pow(final BigDecimal a, final BigDecimal b) {
        final double base = a.doubleValue();
        final double exponent = b.doubleValue();
        final String result = Double.toString(Math.pow(base, exponent));
        return new BigDecimal(result); //Math.pow(a, b)
    }
}
