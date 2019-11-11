import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CalculatorTest {
//TODO в expected добавть курс валют + split
    @Test
    public void sum() throws WrongValueException {
        Assert.assertEquals("60000,0", Calculator.sum("$542", "$458"));
        Assert.assertEquals("6355,0", Calculator.sum("$100", "355p"));
        Assert.assertEquals("1605,0", Calculator.sum("1045p", "560p"));
        Assert.assertEquals("1606,1", Calculator.sum("1045,50p", "560,60p"));
    }

    @Test(expected = WrongValueException.class)
    public void sub() throws WrongValueException {
        Assert.assertEquals("5040,0", Calculator.sub("$542", "$458"));
        Assert.assertEquals("5645,0", Calculator.sub("$100", "355p"));
        Assert.assertEquals("485,0", Calculator.sub("1045p", "560p"));
        Assert.assertEquals("ex", Calculator.sub("1045p", "$560"));
    }
}