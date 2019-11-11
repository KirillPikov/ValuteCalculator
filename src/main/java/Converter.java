public class Converter {
    public static String toRubles(String dollars) throws WrongValueException {
        if(dollars.endsWith("p")) return dollars;
        double dollarsValue = getDoubleValue(dollars);
        double rublesValue = dollarsValue * Settings.cours;
        return String.valueOf(rublesValue)
                .replace('.', Settings.split.charAt(0) ) + "p";
    }

    public static String toDollars(String rubles) throws WrongValueException {
        if(rubles.startsWith("$")) return rubles;
        double rublesValue = getDoubleValue(rubles);
        double dollarsValue = rublesValue / Settings.cours;
        return "$" + String.valueOf(dollarsValue)
                .replace('.', Settings.split.charAt(0) );
    }

    public static double getDoubleValue(String stringValue) throws WrongValueException {
        if(stringValue.startsWith("$")) stringValue = stringValue.substring(1);
        if(stringValue.endsWith("p")) stringValue = stringValue.substring(0, stringValue.length() - 1);
        double doubleValue;
        if(stringValue.contains(Settings.split)) {
            String[] dollarsParts = stringValue.split(Settings.split);
            int wholePart = Integer.valueOf(dollarsParts[0]);
            int intRestPart  = Integer.valueOf(dollarsParts[1].substring(0, dollarsParts[1].length()));
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
