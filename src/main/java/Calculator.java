public class Calculator {
    public static String sum(String first, String second) throws WrongValueException {
        first = Converter.toRubles(first);
        second = Converter.toRubles(second);
        double firstValue;
        double secondValue;
        firstValue = Converter.getDoubleValue(first);
        secondValue = Converter.getDoubleValue(second);
        return String.valueOf(firstValue + secondValue)
                     .replace('.', Settings.split.charAt(0)) + "p";
    }

    public static String sub(String first, String second) throws WrongValueException {
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
}
