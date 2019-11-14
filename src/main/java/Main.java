public class Main {

    public static void main(String[] args) throws WrongValueException, WrongExpressionFormatException {
        String testExpression = "$8 - toDollars(350,366p - toRubles(toDollars(120,66p - $1,63)) + 120p - 5p - 1p - toRubles(15p - toRubles(3p + toRubles(2p))) - 1p)";
        System.out.println(Calculator.calculateExpression(testExpression));
    }
}
