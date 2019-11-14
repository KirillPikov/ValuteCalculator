/**
 * Калькулятор валют, позволяющий проводить разные операции с выражениями.
 */
public class Calculator {
    /**
     * Пустой приватный конструктор.
     */
    private Calculator() {
    }

    /**
     * Метод, выполняющий операцию сложения двух валют (не обязательно одинаковых)
     * в строковом представлении. Проводит вычисления в рублях.
     * @param first первое слагаемое.
     * @param second второе слагаемое.
     * @return сумму двух значений в рублях.
     * @throws WrongValueException //TODO
     * @throws WrongExpressionFormatException //TODO
     */
    public static String sum(String first, String second)
            throws WrongValueException, WrongExpressionFormatException {
        /* Неважно какой валюты были значения, приводим их к рублю */
        first = Converter.toRubles(first);
        second = Converter.toRubles(second);
        double firstValue;
        double secondValue;
        /* Получаем численное значение каждого слагаемого */
        firstValue = Converter.getDoubleValue(first);
        secondValue = Converter.getDoubleValue(second);
        /* Возвращаем строковое представление суммы слагаемых, добавляя в конец выражение знак рубля */
        return String.valueOf(firstValue + secondValue)
                     .replace('.', Settings.split.charAt(0)) + "p";
    }

    /**
     * Метод, выполняющий операцию сложения двух валют (не обязательно одинаковых)
     * @param first уменьшаемое.
     * @param second вычитаемое.
     * @return разность двух значений в рублях.
     * @throws WrongValueException  //TODO
     * @throws WrongExpressionFormatException //TODO
     */
    public static String sub(String first, String second)
            throws WrongValueException, WrongExpressionFormatException {
        /* Неважно какой валюты были значения, приводим их к рублю */
        first = Converter.toRubles(first);
        second = Converter.toRubles(second);
        double firstValue;
        double secondValue;
        /* Получаем численное значение уменьшаемого и вычитаемого */
        firstValue = Converter.getDoubleValue(first);
        secondValue = Converter.getDoubleValue(second);
        /* Находим разость */
        double result = firstValue - secondValue;
        if(result < 0) {
            throw new WrongValueException("Value must be > 0, value = " + result);
        }
        /* Возвращаем строковое представление разности значений, добавляя в конец выражение знак рубля */
        return String.valueOf(result)
                .replace('.', Settings.split.charAt(0)) + "p";
    }

    /**
     * <p>
     * Метод для вычисления значения выражения содержащего следующие операции:
     * <li>toDollars - {@link Converter#toDollars(String)}</li>
     * <li>toRubles - {@link Converter#toRubles(String)}</li>
     * <li>"<b> + </b>" - {@link Calculator#sum(String, String)}</li>
     * <li>"<b> - </b>" - {@link Calculator#sub(String, String)}</li>
     * </p>
     * @param expression подсчитываемое выражение.
     * @return значение переданного выражения.
     * @throws WrongValueException //TODO
     * @throws WrongExpressionFormatException //TODO
     */
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
            result = calculateExpressionWithBasicOperations(expression);
        }
        return result;
    }

    /**
     *  <p>
     *  Метод для вычисления значения выражения содержащего следующие операции:
     *  <li>"<b> + </b>" - {@link Calculator#sum(String, String)}</li>
     *  <li>"<b> - </b>" - {@link Calculator#sub(String, String)}</li>
     *  </p>
     * @param expression подсчитываемое выражение.
     * @return значение переданного выражения.
     * @throws WrongValueException
     * @throws WrongExpressionFormatException
     */
    private static String calculateExpressionWithBasicOperations(String expression)
            throws WrongValueException, WrongExpressionFormatException {
        String result = expression;
        int indexFirstSpace = expression.indexOf(' ');
        if(expression.charAt(indexFirstSpace + 1) == '+') {
            result = Calculator.sum(
                    expression.substring(0, indexFirstSpace),
                    calculateExpression(expression.substring(indexFirstSpace + 3))
            );
        }
        else if(expression.charAt(indexFirstSpace + 1) == '-') {
            result = calculateExpression(
                    calculateExpressionWithSubstraction(expression)
            );
        }
        return result;
    }

    /**
     * <p>
     * Метод для определения и вычисления выражений, находящихся после операций:
     * <li>toDollars - {@link Converter#toDollars(String)}</li>
     * <li>toRubles - {@link Converter#toRubles(String)}</li>
     * </p>
     * Пример такого выражения выделен жирным: <code>toDollars(35p - 0,1$)<b> + $25 - 300p</b></code>
     * @param result результат выражения, благодаря которуму вызвался этот метод
     * ( в примере это <code>toDollars(35p - 0,1$)</code> ).
     * @param indexEndInnerExp индекс последней закрывающей скобки " ) " параметра result.
     * @param expression само внешнее выражение, которо необходимо посчитать и прибавить к параметру result.
     * @return result как сумму или разность выражений result и expression, в случае если после result
     * есть внешнее выражение. Иначе возвращает result без изменений.
     * @throws WrongValueException //TODO
     * @throws WrongExpressionFormatException //TODO
     */
    private static String calcucalteOuterExpression(String result,
                                                    int indexEndInnerExp,
                                                    String expression)
            throws WrongValueException, WrongExpressionFormatException {
        /* Если за символом " ) " есть ещё выражение */
        if(indexEndInnerExp < expression.length() - 1) {
            /* И если это выражение имеет первой операцией сложение */
            if (expression.charAt(indexEndInnerExp + 2) == '+') {
                result = Calculator.sum(
                        result,
                        calculateExpression(expression.substring(indexEndInnerExp + 4))
                );
            } /* Или если это выражение имеет первой операцией сложение */
            else if (expression.charAt(indexEndInnerExp + 2) == '-') {
                result = calculateExpression(
                        calculateExpressionWithSubstraction(
                                result + expression.substring(indexEndInnerExp + 1)
                        )
                );
            }
        }
        return result;
    }

    /**
     * Метод, вычисляющий значение операции toDollars {@link Converter#toDollars(String)}
     * и выражения лежащего внутри него.
     * @param expression вычисляемое выражение.
     * @param indexEndInnerExp индекс последней закрывающей скобки " ) " параметра expression.
     * @return значение операции toDollars {@link Converter#toDollars(String)} применённой к
     * лежащему внутри выражению.
     * @throws WrongValueException //TODO
     * @throws WrongExpressionFormatException //TODO
     */
    private static String calculateExpressionToDollars(String expression,
                                                      int indexEndInnerExp)
            throws WrongValueException, WrongExpressionFormatException {
        return Converter.toDollars(
                calculateExpression(expression.substring(10, indexEndInnerExp))
        );
    }

    /**
     * Метод, вычисляющий значение операции toRubles {@link Converter#toRubles(String)}
     * и выражения лежащего внутри него.
     * @param expression вычисляемое выражение.
     * @param indexEndInnerExp индекс последней закрывающей скобки " ) " параметра expression.
     * @return значение операции toRubles {@link Converter#toRubles(String)} применённой к
     * лежащему внутри выражению.
     * @throws WrongValueException //TODO
     * @throws WrongExpressionFormatException //TODO
     */
    private static String calculateExpressionToRubles(String expression,
                                                     int indexEndInnerExp)
            throws WrongValueException, WrongExpressionFormatException {
        return Converter.toRubles(
                calculateExpression(expression.substring(9, indexEndInnerExp))
        );
    }
    //TODO упростить!
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

    /**
     * Метод для получения индекса символа, завершающего выражение.
     * @param expression выражение из которого необходимо вычленить первое подвыражение
     * и найти индекс завершающий его.
     * @return индекс символа завершающего выражение.
     */
    public static int getIndexEndInnerExp(String expression) {
        char[] charArray = expression.substring(10).toCharArray();
        /* Число открытых скобок */
        int brakesCount = 1;
        /* Индекс для отсчёта */
        int indexEndInnerExp = 10;
        for (char ch: charArray) {
            if(ch == '(') /* Если встретили открытую скобку - увеличиваем счётчик открытых скобок */ {
                brakesCount++;
            }
            if(ch == ')') {
                brakesCount--;
            }
            if(brakesCount == 0) {
                break;
            }
            indexEndInnerExp++;
        }
        /* Цикл выполняется до тех пор, пока кол-во открытых скобок не будет равно 0
         или пока не дойдём до конца выражения */
        return indexEndInnerExp;
    }
}
