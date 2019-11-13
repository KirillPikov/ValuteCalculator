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
     * @throws WrongExpressionFormatException возникает, если //TODO дописать!
     */
    public static String toRubles(String dollars) throws WrongValueException, WrongExpressionFormatException {
        String result = new String();
        if(dollars.startsWith("$")) {
            /* Получаем числовое значение в долларах */
            double dollarsValue = getDoubleValue(dollars);
            /* Переводим в рубли в соответствии с курсом */
            double rublesValue = dollarsValue * Settings.cours;
            result = rublesValue + "p";
        } else
        if(dollars.endsWith("p")) {
            /* В случае, если передали значение в рублях, возвращаем его же */
            result = dollars;
        } else {
            throw new WrongExpressionFormatException("Wrong format! Your expression: " + dollars);
        }
        return result.replace('.', Settings.split.charAt(0) );
    }

    /**
     * Конвертирует рубли в доллары.
     * @param rubles строковое значение в рублях.
     * @return строковое значение в долларах.
     * @throws WrongValueException возникает, если получилось отрицательное значение.
     * @throws WrongExpressionFormatException возникает, если //TODO дописать!
     */
    public static String toDollars(String rubles) throws WrongValueException, WrongExpressionFormatException {
        String result = new String();
        if(rubles.endsWith("p")) {
            /* Получаем числовое значение в рублях */
            double rublesValue = getDoubleValue(rubles);
            /* Переводим в доллары в соответствии с курсом */
            double dollarsValue = rublesValue / Settings.cours;
            result = "$" + dollarsValue;
        } else
        if(rubles.startsWith("$")) {
            /* В случае, если передали значение в долларах, возвращаем его же */
            result = rubles;
        } else {
            throw new WrongExpressionFormatException("Wrong format! Your expression: " + rubles);
        }
        return result.replace('.', Settings.split.charAt(0) );
    }

    /**
     * Высчитывает числовое значение переданного строкового. (результат не зависит от валюты)
     * @param stringValue строковое значение.
     * @return числовое значение.
     * @throws WrongValueException возникает, если получилось отрицательное значение.
     * @throws WrongExpressionFormatException возникает, если //TODO дописать!
     */
    public static double getDoubleValue(String stringValue) throws WrongValueException, WrongExpressionFormatException {
        /*  */
        if(stringValue.startsWith("$")) {
            stringValue = stringValue.substring(1);
        } else
        if(stringValue.endsWith("p")) {
            stringValue = stringValue.substring(0, stringValue.length() - 1);
        } else {
            throw new WrongExpressionFormatException("Wrong format! Your expression: " + stringValue);
        }
        /* Числовое значение */
        double doubleValue;
        /* Если это не целое число, надо отделить целую часть от дробной */
        if(stringValue.contains(Settings.split)) {
            String[] dollarsParts = stringValue.split(Settings.split);
            int wholePart = Integer.valueOf(dollarsParts[0]);
            int intRestPart  = Integer.valueOf(dollarsParts[1]);
            /* Переносим дробную часть в свои разряды, т.е 23 -> 0,23 */
            double doubleRestPart = intRestPart / ( Math.pow( 10, dollarsParts[1].length() ) );
            doubleValue = wholePart + doubleRestPart;
        } else {
            doubleValue = Integer.valueOf(stringValue);
        }
        if(doubleValue < 0) {
            throw new WrongValueException("Value must be > 0, value = " + stringValue);
        }
        return doubleValue;
    }
}
