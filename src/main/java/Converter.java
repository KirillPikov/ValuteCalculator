import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс, предназначенный для конвертирования валют и получения
 * их численного значения по строковому.
 */
public class Converter {
    /**
     * Пустой приватный конструктор.
     */
    private Converter() {
    }

    /**
     * Конвертирует доллары в рубли.
     * Если было передано значение в рублях - оно и вернётся.
     * @param dollars строковое значение в долларах.
     * @return строковое значение в рублях.
     * @throws WrongValueException возникает, если получилось отрицательное значение.
     * @throws WrongExpressionFormatException возникает, если значение не является валютой.
     */
    public static String toRubles(String dollars) throws WrongValueException, WrongExpressionFormatException {
        String result = new String();
        if(dollars.startsWith("$")) {
            /* Получаем числовое значение в долларах */
            double dollarsValue = getDoubleValue(dollars);
            /* Переводим в рубли в соответствии с курсом */
            double rublesValue = dollarsValue * Settings.COURS;
            result = rublesValue + "p";
        } else
        if(dollars.endsWith("p")) {
            /* В случае, если передали значение в рублях, возвращаем его же */
            result = dollars;
        } else {
            throw new WrongExpressionFormatException("Wrong format! Your expression: " + dollars);
        }
        return result.replace('.', Settings.SPLIT.charAt(0) );
    }

    /**
     * Конвертирует рубли в доллары.
     * Если было передано значение в долларах - оно и вернётся.
     * @param rubles строковое значение в рублях.
     * @return строковое значение в долларах.
     * @throws WrongValueException возникает, если получилось отрицательное значение.
     * @throws WrongExpressionFormatException возникает, если значение не является валютой.
     */
    public static String toDollars(String rubles)
            throws WrongValueException, WrongExpressionFormatException {
        String result = new String();
        if(rubles.endsWith("p")) {
            /* Получаем числовое значение в рублях */
            double rublesValue = getDoubleValue(rubles);
            /* Переводим в доллары в соответствии с курсом */
            double dollarsValue = rublesValue / Settings.COURS;
            result = "$" + dollarsValue;
        } else
        if(rubles.startsWith("$")) {
            /* В случае, если передали значение в долларах, возвращаем его же */
            result = rubles;
        } else {
            throw new WrongExpressionFormatException("Wrong format! Your expression: " + rubles);
        }
        return result.replace('.', Settings.SPLIT.charAt(0) );
    }

    /**
     * Высчитывает числовое значение переданного строкового. (результат не зависит от валюты)
     * @param stringValue строковое значение.
     * @return числовое значение.
     * @throws WrongValueException возникает, если получилось отрицательное значение.
     * @throws WrongExpressionFormatException возникает, если значение не является валютой.
     */
    public static double getDoubleValue(String stringValue)
            throws WrongValueException, WrongExpressionFormatException {
        /* Прежде чем получать числовое значение, необходимо убрать лишние сиволы валют */
        stringValue = getStringValueWithoutValute(stringValue);
        /* Числовое значение */
        double doubleValue;
        /* Если это не целое число, то надо отделить целую часть от дробной */
        if(stringValue.contains(Settings.SPLIT)) {
            String[] dollarsParts = stringValue.split(Settings.SPLIT);
            int wholePart;
            int intRestPart;
            try {
                wholePart = Integer.valueOf(dollarsParts[0]);
                /* Отсекаем ненужные разряды после запятой */
                dollarsParts[1] = dollarsParts[1].length() > Settings.DIGITS_COUNT_AFTER_COMMA ?
                        dollarsParts[1].substring(0, Settings.DIGITS_COUNT_AFTER_COMMA)
                        : dollarsParts[1];
                intRestPart = Integer.valueOf(dollarsParts[1]);
            } catch (Exception e) {
                throw new WrongExpressionFormatException("Wrong format of expression!");
            }
            /* Переносим дробную часть в свои разряды, т.е 23 -> 0,23 */
            double doubleRestPart = intRestPart / ( Math.pow( 10, dollarsParts[1].length() ) );
            /* Складываем целую часть и дробную */
            doubleValue = wholePart + doubleRestPart;
        } else {
            /* Иначе, переводим строковое значение в численное */
            doubleValue = Integer.valueOf(stringValue);
        }
        if(doubleValue < 0) {
            throw new WrongValueException("Value must be > 0, value = " + stringValue);
        }
        return doubleValue;
    }

    /**
     * Метод, предназначенный для отсечения знака валюты.
     * @param stringValue значение, в котором необходимо отсечь знак валюты.
     * @return строковое значение без знака валюты.
     * @throws WrongExpressionFormatException возникает в случае, если переданное значение не содержит знак валюты.
     */
    private static String getStringValueWithoutValute(String stringValue) throws WrongExpressionFormatException {
        String regex = "\\d*" + Settings.SPLIT + "{0,1}\\d{0," + Settings.DIGITS_COUNT_AFTER_COMMA + "}";
        String string = stringValue;
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            String group = matcher.group();
            if(group.length() != 0) {
                stringValue = matcher.group();
                break;
            }
        }
        return stringValue;
    }
}
