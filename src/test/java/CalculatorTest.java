import org.junit.Assert;
import org.junit.Test;

public class CalculatorTest {
    @Test
    public void sum() throws WrongValueException, WrongExpressionFormatException {
        Assert.assertEquals(
                String.valueOf((542.0 + 458.0) * Settings.COURS).replace(".", Settings.SPLIT) + "p",
                Calculator.sum("$542", "$458")
        );

        Assert.assertEquals(
                String.valueOf((100.0  * Settings.COURS + 355.0)).replace(".", Settings.SPLIT) + "p",
                Calculator.sum("$100", "355p")
        );

        Assert.assertEquals(
                String.valueOf((1045.0 + 560.0)).replace(".", Settings.SPLIT) + "p",
                Calculator.sum("1045p", "560p")
        );

        Assert.assertEquals(
                String.valueOf((1045.50 + 560.60)).replace(".", Settings.SPLIT) + "p",
                Calculator.sum("1045,50p", "560,60p")
        );
    }

    @Test(expected = WrongValueException.class)
    public void sub() throws WrongValueException, WrongExpressionFormatException {
        Assert.assertEquals(
                String.valueOf((542.0 - 458.0) * Settings.COURS).replace(".", Settings.SPLIT) + "p",
                Calculator.sub("$542", "$458")
        );

        Assert.assertEquals(
                String.valueOf((100.0  * Settings.COURS - 355.0)).replace(".", Settings.SPLIT) + "p",
                Calculator.sub("$100", "355p")
        );

        Assert.assertEquals(
                String.valueOf((1045.0 - 560.0)).replace(".", Settings.SPLIT) + "p",
                Calculator.sub("1045p", "560p")
        );

        Assert.assertEquals("ex", Calculator.sub("1045p", "$560"));
    }

    @Test
    public void calculateExpression() throws WrongValueException, WrongExpressionFormatException {
        String result_1 = Calculator.calculateExpression(
                "toDollars(737p + toRubles($85,4))"
        );
        Assert.assertEquals(
                Double.parseDouble("97.6833"),
                Double.parseDouble(
                        result_1.substring(1)
                                .replace(Settings.SPLIT, ".")
                ), 0.1
        );

        String result_2 = Calculator.calculateExpression(
                "35,87p + toDollars(350p + $25,81)"
        );
        Assert.assertEquals(
                Double.parseDouble("1934.4"),
                Double.parseDouble(
                        result_2.substring(0, result_2.length() - 1)
                                .replace(Settings.SPLIT, ".")
                ), 0.1
        );

        String result_3 = Calculator.calculateExpression(
                "35,87p + toDollars(350p + $25,81 - 15p) - toRubles($19,25)"
        );
        Assert.assertEquals(
                Double.parseDouble("764.4697"),
                Double.parseDouble(
                        result_3.substring(0, result_3.length() - 1)
                                .replace(Settings.SPLIT, ".")
                ), 0.1
        );

        String result_4 = Calculator.calculateExpression(
                "35,87p + toDollars(350p + $25,81 - 15p) - toRubles($19,25) + toRubles(toDollars($150 - toDollars(1800,15234p)))"
        );
        Assert.assertEquals(
                Double.parseDouble("7964.3"),
                Double.parseDouble(
                        result_4.substring(0, result_4.length() - 1)
                                .replace(Settings.SPLIT, ".")
                ), 0.1
        );

        String result_5 = Calculator.calculateExpression(
                "35,87p + toDollars(350p + $25,81 - 15p) - toRubles($19,25) + toDollars($6,12 + 569,58p + toRubles(toDollars($150 - toDollars(1800,15234p) - 895p - $32 + 17p))) + 1597,256p"
        );
        Assert.assertEquals(
                Double.parseDouble("7700.3"),
                Double.parseDouble(
                        result_5.substring(0, result_5.length() - 1)
                              .replace(Settings.SPLIT, ".")
                ), 0.1
        );
    }

    @Test(expected = WrongValueException.class)
    public void calculateExpressionWithWrongValueException() throws WrongValueException, WrongExpressionFormatException {
        Assert.assertEquals(
                "exception",
                Calculator.calculateExpression(
                        "toDollars($6,12 - 5669,58p + 1800,15234p) + 1597,256p"
                )
        );
        Assert.assertEquals(
                "exception",
                Calculator.calculateExpression(
                        "toDollars($6,12 - 35,58p + 1800,15234p) - 25000,256p + $2364"
                )
        );
    }

    @Test(expected = WrongExpressionFormatException.class)
    public void calculateExpressionWithWrongExpressionFormatException() throws WrongValueException, WrongExpressionFormatException {
        Assert.assertEquals(
                "exception",
                Calculator.calculateExpression(
                        "toDolldsars($6,12 + 569,58p + 1800,15234p) + 1597,256p"
                )
        );
        Assert.assertEquals(
                "exception",
                Calculator.calculateExpression(
                        "toDollars($6,12+ 569,58p + 1800,15234p) + 1597,256p"
                )
        );
    }
}