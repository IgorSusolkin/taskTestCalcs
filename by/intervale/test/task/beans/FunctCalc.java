package by.intervale.test.task.beans;

import java.math.BigDecimal;
import java.util.Stack;

import by.intervale.test.task.exeptions.TokenExeption;
/**
 * калькулятор со скобками с функциями "sin", "cos", "tg" и корень квадратный
 * @author Igor Susolkin
 *
 */
public class FunctCalc extends AdvCalc {
    private static final String ADD_OPERATOR = "√";
    private static final String[] FUNCTIONS = {"sin", "cos", "tg"};

    public FunctCalc() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String getDelimiters() {
        // TODO Auto-generated method stub
        return super.getDelimiters() + ADD_OPERATOR;
    }

    @Override
    protected boolean isConditionAddStack(final String token) {
        // TODO Auto-generated method stub
        return isFunction(token) || isOpenedBracket(token);
    }

    @Override
    protected void handlerOperatorAndFunction(final String token,
            final Stack<String> expression) throws TokenExeption {
        if (isFunction(token) || token.equals(ADD_OPERATOR)) {
            final BigDecimal op1 = getNumberFromStack(token, expression);
            final BigDecimal result = getResultByTokens(token, op1);
            result.setScale(5, BigDecimal.ROUND_HALF_UP);
            expression.push(result.toString());
        } else if (isOperator(token)) {
            super.handlerOperatorAndFunction(token, expression);
        }
    }

    protected boolean isFunction(final String token) {
        for (String item : FUNCTIONS) {
            if (item.equals(token)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean isOperator(final String token) {
        // TODO Auto-generated method stub
        return super.isOperator(token) || token.equals(ADD_OPERATOR);
    }

    @Override
    protected BigDecimal getResultByTokens(final String operator,
            final BigDecimal ... args) throws TokenExeption {
        // TODO Auto-generated method stub
            switch (operator) {
            case "sin" : return sin(args[0]);
            case "cos" : return cos(args[0]);
            case "tg" : return tan(args[0]);
            case "√" : return sqrt(args[0]);
            default: return super.getResultByTokens(operator, args);
            }
    }

    protected BigDecimal sin(final BigDecimal a) {
        final String value = Double.toString(Math.sin(a.doubleValue()));
        return new BigDecimal(value);
    }

    protected BigDecimal cos(final BigDecimal a) {
        final String value = Double.toString(Math.cos(a.doubleValue()));
        return new BigDecimal(value);
    }

    protected BigDecimal tan(final BigDecimal a) {
        final String value = Double.toString(Math.tan(a.doubleValue()));
        return new BigDecimal(value);
    }

    protected BigDecimal sqrt(final BigDecimal a) {
        final BigDecimal exponent = new BigDecimal("0.5");
        return pow(a, exponent);
    }
}
