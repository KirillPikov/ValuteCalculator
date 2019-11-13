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


}