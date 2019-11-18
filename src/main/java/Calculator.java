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
     * @throws WrongValueException смотри {@link Converter#toRubles(String)},
     * {@link Converter#toDollars(String)}, {@link Converter#getDoubleValue(String)}
     * @throws WrongExpressionFormatException смотри {@link Converter#getDoubleValue(String)}
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
                     .replace('.', Settings.SPLIT.charAt(0)) + "p";
    }

    /**
     * Метод, выполняющий операцию сложения двух валют (не обязательно одинаковых)
     * @param first уменьшаемое.
     * @param second вычитаемое.
     * @return разность двух значений в рублях.
     * @throws WrongValueException смотри {@link Converter#toRubles(String)},
     * {@link Converter#toDollars(String)}, {@link Converter#getDoubleValue(String)}
     * @throws WrongExpressionFormatException смотри {@link Converter#getDoubleValue(String)}
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
                .replace('.', Settings.SPLIT.charAt(0)) + "p";
    }

    /**
     * Подсчитывает значение выражения.
     * @param expression выражение, которое необходимо подсчитать.
     * @return значение выражения.
     * @throws WrongValueException смотри {@link Converter#toRubles(String)},
     * {@link Converter#toDollars(String)}, {@link Converter#getDoubleValue(String)}
     * @throws WrongExpressionFormatException смотри {@link Converter#getDoubleValue(String)}
     */
    public static String calculateExpression(String expression)
            throws WrongValueException, WrongExpressionFormatException {
        /* Получаем первый и второй термы */
        String firstTerm = getTerm(expression);
        /* Строка начала второга терма */
        String beginSecondTerm;
        String secondTerm;
        int endSecondTerm;
        /* Проверка на наличие второго терма */
        if(!(firstTerm.length() + 3 < expression.length())) {
            secondTerm = "";
        } else {
            beginSecondTerm = expression.substring(firstTerm.length() + 3);
            secondTerm = getTerm(beginSecondTerm);
        }
        endSecondTerm  = secondTerm.length();

        /* Выражение, находящееся за этими двумя термами */
        String outerExpression = "";
        /* Если оно существует, получаем его */
        if(firstTerm.length() + secondTerm.length() + 3 < expression.length()) {
            outerExpression = expression.substring(endSecondTerm + firstTerm.length() + 3);
        }

        /* Высчитываем значения термов, если они являются "функциями",
            в противном случае они представляют собой значения валют */
        firstTerm  = firstTerm.startsWith("toDollars")   ? calculateExpressionToDollars(firstTerm)   : firstTerm;
        firstTerm  = firstTerm.startsWith("toRubles")    ? calculateExpressionToRubles(firstTerm)    : firstTerm;
        secondTerm = secondTerm.startsWith("toDollars")  ? calculateExpressionToDollars(secondTerm)  : secondTerm;
        secondTerm = secondTerm.startsWith("toRubles")   ? calculateExpressionToRubles(secondTerm)   : secondTerm;

        /* На случай, если второй терм пустой, то результат всего выражения есть значение первого терма */
        String resultOperationWithTerms = firstTerm;
        /* Если второй терм есть */
        if(secondTerm.length() > 0) {
            /* Опрделяем знак операции между этими термами */
            if (expression.charAt(firstTerm.length() + 1) == '+') {
                resultOperationWithTerms = sum(firstTerm, secondTerm);
            }
            if (expression.charAt(firstTerm.length() + 1) == '-') {
                resultOperationWithTerms = sub(firstTerm, secondTerm);
            }
        }
        /* Промежуточным результатом этого метода является результат операции между двумя термами */
        String result = resultOperationWithTerms + outerExpression;
        /* Но если после них есть выражение, его тоже необходимо посчитать, т.е выполняем операции слева направо */
        if(result.contains(" ")) {
            result = calculateExpression(result);
        }
        /* Приводим его в нормальную форму */
        return termToNormalForm(result);
    }

    /**
     * Приводит терм в нормальную форму, т.е если это целое число - вернётся оно же, но если
     * это дробное, то вернётся это же число, но с {@link Settings#DIGITS_COUNT_AFTER_COMMA} знаком(ами)
     * после запятой.
     * @param term исходный терм.
     * @return нормализованый терм.
     */
    private static String termToNormalForm(String term) {
        /* В случае целого числа */
        String normalTerm = term;
        /* Если оно дробное */
        if(normalTerm.contains(Settings.SPLIT)) {
            boolean isRubles = false;
            /* Определяем валюту, т.к. если это рубли, то мы можем затереть символ "p" */
            if(normalTerm.endsWith("p")) {
                isRubles = true;
            }
            /* В случае, если знаков в терме после запятой больше, чем задано в Settings.DIGITS_COUNT_AFTER_COMMA */
            if(normalTerm.length() - normalTerm.indexOf(Settings.SPLIT) > Settings.DIGITS_COUNT_AFTER_COMMA) {
                /* Получаем подстроку с количеством символов после запятой Settings.DIGITS_COUNT_AFTER_COMMA */
                normalTerm = normalTerm.substring(
                        0,
                        normalTerm.indexOf(Settings.SPLIT) + Settings.DIGITS_COUNT_AFTER_COMMA
                );
                /* Если это рубли, то необходимо добавить знак "p" */
                normalTerm = (isRubles) ? normalTerm + "p" : normalTerm;
            }
        }
        return normalTerm;
    }

    /**
     * Метод для получения первого терма из выражения.
     * @param expression выражение, из которого извлекаем терм.
     * @return первый терм выражения.
     */
    private static String getTerm(String expression) {
        int indexEndTerm = getIndexEndTerm(expression);
        return expression.substring(0, indexEndTerm);
    }

    /**
     * Метод, вычисляющий значение операции toDollars {@link Converter#toDollars(String)}
     * и выражения лежащего внутри него.
     * @param expression вычисляемое выражение.
     * @return значение операции toDollars {@link Converter#toDollars(String)} применённой к
     * лежащему внутри выражению.
     * @throws WrongValueException смотри {@link Converter#toDollars(String)}
     * @throws WrongExpressionFormatException смотри {@link Converter#getDoubleValue(String)}
     */
    private static String calculateExpressionToDollars(String expression)
            throws WrongValueException, WrongExpressionFormatException {
        return Converter.toDollars(
                calculateExpression(expression.substring(10, getIndexEndTerm(expression) - 1 ))
        );
    }

    /**
     * Метод, вычисляющий значение операции toRubles {@link Converter#toRubles(String)}
     * и выражения лежащего внутри него.
     * @param expression вычисляемое выражение.
     * @return значение операции toRubles {@link Converter#toRubles(String)} применённой к
     * лежащему внутри выражению.
     * @throws WrongValueException смотри {@link Converter#toRubles(String)}
     * @throws WrongExpressionFormatException смотри {@link Converter#getDoubleValue(String)}
     */
    private static String calculateExpressionToRubles(String expression)
            throws WrongValueException, WrongExpressionFormatException {
        return Converter.toRubles(
                calculateExpression(expression.substring(9, getIndexEndTerm(expression) - 1 ))
        );
    }

    /**
     * Метод для получения индекса символа, завершающего выражение.
     * @param expression выражение из которого необходимо вычленить первое подвыражение
     * и найти индекс завершающий его.
     * @return индекс символа завершающего выражение.
     */
    private static int getIndexEndTerm(String expression) {
        int indexEndTerm = 0;
        /* В случае, если необходимо найти индекс символа завершающего функцию */
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
                if (ch == ')') /* Если встретили закрытую скобку - уменьшаем счётчик открытых скобок */ {
                    brakesCount--;
                }
                indexEndTerm++;
                if (brakesCount == 0) {
                    break;
                }
            }
        } else /* Иначе, это может быть как сумма или разность термов, тогда индекс - первый пробел,
         в противном случае это просто один простой терм (пример "569,364р")*/ {
            indexEndTerm = (expression.contains(" ")) ? expression.indexOf(" ") : expression.length();
        }
        return indexEndTerm;
    }
}
