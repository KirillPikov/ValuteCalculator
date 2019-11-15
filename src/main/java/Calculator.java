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

    public static String calculateExpression(String expression)
            throws WrongValueException, WrongExpressionFormatException {
        if((expression.startsWith("toDollars") || expression.startsWith("toRubles")) && !expression.endsWith(")")) {
            expression += ")";
        }
        /* Получаем первый и второй термы */
        String firstTerm = getTerm(expression);
        String beginSecondTerm = expression.substring(firstTerm.length() + 3);
        int endSecondTerm = getIndexEndTerm(beginSecondTerm);
        String secondTerm = getTerm(beginSecondTerm);

        /* Получаем выражение, находящееся за этими двумя термами */
        String outerExpression = expression.substring(endSecondTerm + firstTerm.length() + 3);
        /* Высчитываем значения термов, если они являются "функциями",
            в противном случае они представляют собой значения валют */
        firstTerm  = firstTerm.startsWith("toDollars")   ? calculateExpressionToDollars(firstTerm)   : firstTerm;
        firstTerm  = firstTerm.startsWith("toRubles")    ? calculateExpressionToRubles(firstTerm)    : firstTerm;
        secondTerm = secondTerm.startsWith("toDollars")  ? calculateExpressionToDollars(secondTerm)  : secondTerm;
        secondTerm = secondTerm.startsWith("toRubles")   ? calculateExpressionToRubles(secondTerm)   : secondTerm;

        String resultOperationWithTerms = firstTerm;
        if(secondTerm.length() > 0) {
            if (expression.charAt(firstTerm.length() + 1) == '+') {
                resultOperationWithTerms = sum(firstTerm, secondTerm);
            }
            if (expression.charAt(firstTerm.length() + 1) == '-') {
                resultOperationWithTerms = sub(firstTerm, secondTerm);
            }
        }
        return resultOperationWithTerms + outerExpression;
    }

    public static String getTerm(String expression) {
        int indexEndTerm = getIndexEndTerm(expression);
        return expression.substring(0, indexEndTerm);
    }

    /**
     * Метод, вычисляющий значение операции toDollars {@link Converter#toDollars(String)}
     * и выражения лежащего внутри него.
     * @param expression вычисляемое выражение.
     * @return значение операции toDollars {@link Converter#toDollars(String)} применённой к
     * лежащему внутри выражению.
     * @throws WrongValueException //TODO
     * @throws WrongExpressionFormatException //TODO
     */
    private static String calculateExpressionToDollars(String expression)
            throws WrongValueException, WrongExpressionFormatException {
        return Converter.toDollars(
                calculateExpression(expression.substring(10))
        );
    }

    /**
     * Метод, вычисляющий значение операции toRubles {@link Converter#toRubles(String)}
     * и выражения лежащего внутри него.
     * @param expression вычисляемое выражение.
     * @return значение операции toRubles {@link Converter#toRubles(String)} применённой к
     * лежащему внутри выражению.
     * @throws WrongValueException //TODO
     * @throws WrongExpressionFormatException //TODO
     */
    private static String calculateExpressionToRubles(String expression)
            throws WrongValueException, WrongExpressionFormatException {
        return Converter.toRubles(
                calculateExpression(expression.substring(9))
        );
    }

    public static int getIndexEndInnerExpression(String expression) {
        return getIndexEndTerm(expression) - 1;
    }

    /**
     * Метод для получения индекса символа, завершающего выражение.
     * @param expression выражение из которого необходимо вычленить первое подвыражение
     * и найти индекс завершающий его.
     * @return индекс символа завершающего выражение.
     */
    public static int getIndexEndTerm(String expression) {
        int indexEndTerm = 0;
        if(expression.startsWith("toDollars") ||
           expression.startsWith("toRubles")) {
            char[] charArray = expression.substring(10).toCharArray();
            /* Число открытых скобок */
            int brakesCount = 1;
            /* Индекс для отсчёта */
            indexEndTerm = 10;
            for (char ch : charArray) {
                if (ch == '(') /* Если встретили открытую скобку - увеличиваем счётчик открытых скобок */ {
                    brakesCount++;
                }
                if (ch == ')') {
                    brakesCount--;
                }
                indexEndTerm++;
                if (brakesCount == 0) {
                    break;
                }
            }
        } else {
            indexEndTerm = expression.indexOf(" ");
        }
        /* Цикл выполняется до тех пор, пока кол-во открытых скобок не будет равно 0
         или пока не дойдём до конца выражения */
        return indexEndTerm;
    }
}
