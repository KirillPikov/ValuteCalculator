public class Calculator {

    private Calculator() {
    }

    public static String sum(String first, String second)
            throws WrongValueException, WrongExpressionFormatException {
        first = Converter.toRubles(first);
        second = Converter.toRubles(second);
        double firstValue;
        double secondValue;
        firstValue = Converter.getDoubleValue(first);
        secondValue = Converter.getDoubleValue(second);
        return String.valueOf(firstValue + secondValue)
                     .replace('.', Settings.split.charAt(0)) + "p";
    }

    public static String sub(String first, String second)
            throws WrongValueException, WrongExpressionFormatException {
        first = Converter.toRubles(first);
        second = Converter.toRubles(second);
        double firstValue;
        double secondValue;
        firstValue = Converter.getDoubleValue(first);
        secondValue = Converter.getDoubleValue(second);
        double result = firstValue - secondValue;
        if(result < 0) {
            throw new WrongValueException("Value must be > 0, value = " + result);
        }
        return String.valueOf(result)
                .replace('.', Settings.split.charAt(0)) + "p";
    }

    public static String calculateExpression(String expression)
            throws WrongValueException, WrongExpressionFormatException {
        String result = expression;
        if(expression.startsWith("toDollars(")) {
            int indexEndInnerExp = getIndexEndInnerExp(expression);
            result = calculateExpressionToDollars(expression, indexEndInnerExp);
            result = calcucalteOuterExpression(result, indexEndInnerExp, expression);
        } else
        if(expression.startsWith("toRubles(")) {
            int indexEndInnerExp = getIndexEndInnerExp(expression);
            result = calculateExpressionToRubles(expression, indexEndInnerExp);
            result = calcucalteOuterExpression(result, indexEndInnerExp, expression);
        } else {
            result = calculateExpressionWithBasicOperations(result, expression);
        }
        return result;
    }

    private static String calculateExpressionWithBasicOperations(String result,
                                                                 String expression)
            throws WrongValueException, WrongExpressionFormatException {
        int indexFirstSpace = expression.indexOf(' ');
        if(expression.charAt(indexFirstSpace + 1) == '+') {
            result = Calculator.sum(
                    expression.substring(0, indexFirstSpace),
                    calculateExpression(expression.substring(indexFirstSpace + 3))
            );
        } else
        if(expression.charAt(indexFirstSpace + 1) == '-') {
            result = calculateExpression(calculateExpressionWithSubstraction(expression));
        }
        return result;
    }

    private static String calcucalteOuterExpression(String result,
                                                    int indexEndInnerExp,
                                                    String expression)
            throws WrongValueException, WrongExpressionFormatException {
        if(indexEndInnerExp < expression.length() - 1) {
            if (expression.charAt(indexEndInnerExp + 2) == '+') {
                result = Calculator.sum(
                        result,
                        calculateExpression(expression.substring(indexEndInnerExp + 4))
                );
            }
            if (expression.charAt(indexEndInnerExp + 2) == '-') {
                result = calculateExpression(
                        calculateExpressionWithSubstraction(
                                result + expression.substring(indexEndInnerExp + 1)
                        )
                );
            }
        }
        return result;
    }

    private static String calculateExpressionToDollars(String expression,
                                                      int indexEndInnerExp)
            throws WrongValueException, WrongExpressionFormatException {
        return Converter.toDollars(
                calculateExpression(expression.substring(10, indexEndInnerExp))
        );
    }

    private static String calculateExpressionToRubles(String expression,
                                                     int indexEndInnerExp)
            throws WrongValueException, WrongExpressionFormatException {
        return Converter.toRubles(
                calculateExpression(expression.substring(9, indexEndInnerExp))
        );
    }

    private static String calculateExpressionWithSubstraction(String expression)
            throws WrongValueException, WrongExpressionFormatException {
        String result;
        String subExpression = expression.substring(expression.indexOf(' ') + 3);
        String secondValue;
        int secondValueLength = 0;
        int indexEndInnerExpression = -1;
        if(subExpression.startsWith("toDollars")) {
            indexEndInnerExpression = getIndexEndInnerExp(subExpression);
            secondValueLength = indexEndInnerExpression + 1;
            secondValue = calculateExpressionToDollars(subExpression, indexEndInnerExpression);
        } else
        if(subExpression.startsWith("toRubles")) {
            indexEndInnerExpression = getIndexEndInnerExp(subExpression);
            secondValueLength = indexEndInnerExpression + 1;
            secondValue = calculateExpressionToRubles(subExpression, indexEndInnerExpression);
        } else
        {
            char[] subExpressionChars = subExpression.toCharArray();
            for(int i = 1; i < subExpressionChars.length; i++) {
                if(subExpressionChars[i] == ' ' || subExpressionChars[i] == ')') {
                    indexEndInnerExpression = i;
                    break;
                }
            }
            if(indexEndInnerExpression == -1) {
                indexEndInnerExpression = subExpression.length();
            }
            secondValue = subExpression.substring(0, indexEndInnerExpression);
            secondValueLength = secondValue.length();
        }
        String substraction = Calculator.sub(
                expression.substring(0, expression.indexOf(' ')),
                secondValue
        );
        if(subExpression.length() > secondValueLength) {
            String restExpression;
            if(subExpression.charAt(indexEndInnerExpression) == ')') {
                restExpression = subExpression.substring(indexEndInnerExpression + 1);
            } else {
                restExpression = subExpression.substring(indexEndInnerExpression);
            }
            result = substraction + restExpression;
        } else {
            result = substraction;
        }
        return result;
    }

    public static int getIndexEndInnerExp(String expression) {
        char[] charArray = expression.substring(10).toCharArray();
        int brakesCount = 1;
        int indexEndInnerExp = 10;
        for (char ch: charArray) {
            if(ch == '(') brakesCount++;
            if(ch == ')') brakesCount--;
            if(brakesCount == 0) break;
            indexEndInnerExp++;
        }
        return indexEndInnerExp;
    }
}
