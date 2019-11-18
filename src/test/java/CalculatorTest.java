import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CalculatorTest {
    @Test
    public void sum() throws WrongValueException, WrongExpressionFormatException {
        Assert.assertEquals(String.valueOf((542.0 + 458.0) * Settings.cours).replace(".", Settings.split) + "p", Calculator.sum("$542", "$458"));
        Assert.assertEquals(String.valueOf((100.0  * Settings.cours + 355.0)).replace(".", Settings.split) + "p", Calculator.sum("$100", "355p"));
        Assert.assertEquals(String.valueOf((1045.0 + 560.0)).replace(".", Settings.split) + "p", Calculator.sum("1045p", "560p"));
        Assert.assertEquals(String.valueOf((1045.50 + 560.60)).replace(".", Settings.split) + "p", Calculator.sum("1045,50p", "560,60p"));
    }

    @Test(expected = WrongValueException.class)
    public void sub() throws WrongValueException, WrongExpressionFormatException {
        Assert.assertEquals(String.valueOf((542.0 - 458.0) * Settings.cours).replace(".", Settings.split) + "p", Calculator.sub("$542", "$458"));
        Assert.assertEquals(String.valueOf((100.0  * Settings.cours - 355.0)).replace(".", Settings.split) + "p", Calculator.sub("$100", "355p"));
        Assert.assertEquals(String.valueOf((1045.0 - 560.0)).replace(".", Settings.split) + "p", Calculator.sub("1045p", "560p"));
        Assert.assertEquals("ex", Calculator.sub("1045p", "$560"));
    }

    @Test
    public void calculateExpression() throws WrongValueException, WrongExpressionFormatException {
        Assert.assertEquals(
                "$97,6833",
                Calculator.calculateExpression(
                        "toDollars(737p + toRubles($85,4))")

        );
        Assert.assertEquals(
                "1934,4499p",
                Calculator.calculateExpression(
                "35,87p + toDollars(350p + $25,81)")

        );
        Assert.assertEquals(
                "764,4490p",
                Calculator.calculateExpression(
                        "35,87p + toDollars(350p + $25,81 - 15p) - toRubles($19,25)")

        );
        Assert.assertEquals(
                "7964,329p",
                Calculator.calculateExpression(
                        "35,87p + toDollars(350p + $25,81 - 15p) - toRubles($19,25) + toRubles(toDollars($150 - toDollars(1800,15234p)))")

        );
        Assert.assertEquals(
                "7683,2840p",
                Calculator.calculateExpression(
                        "35,87p + toDollars(350p + $25,81 - 15p) - toRubles($19,25) + toDollars($6,12 + 569,58p + toRubles(toDollars($150 - toDollars(1800,15234p) - 895p - $32))) + 1597,256p")

        );
    }
}