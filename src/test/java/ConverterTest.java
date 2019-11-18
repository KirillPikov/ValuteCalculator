import org.junit.Assert;
import org.junit.Test;

public class ConverterTest {

    @Test
    public void toRubles() throws WrongValueException, WrongExpressionFormatException {
        Assert.assertEquals(String.valueOf(100.0 * Settings.COURS)
                        .replace('.', Settings.SPLIT.charAt(0)) + "p",
                Converter.toRubles("$100")
        );

        Assert.assertEquals(String.valueOf(129.562 * Settings.COURS)
                        .replace('.', Settings.SPLIT.charAt(0)) + "p",
                Converter.toRubles("$129" + Settings.SPLIT + "562")
        );
    }

    @Test
    public void toDollars() throws WrongValueException, WrongExpressionFormatException {
        Assert.assertEquals("$" + String.valueOf(100.0 / Settings.COURS)
                        .replace('.', Settings.SPLIT.charAt(0)),
                Converter.toDollars("100p")
        );

        Assert.assertEquals("$" + String.valueOf(6848.5662 / Settings.COURS)
                        .replace('.', Settings.SPLIT.charAt(0)),
                Converter.toDollars("6848" + Settings.SPLIT + "5662p")
        );
    }

    @Test
    public void getDoubleValue() throws WrongValueException, WrongExpressionFormatException {
        Assert.assertEquals(
                158.314,
                 Converter.getDoubleValue("$158" + Settings.SPLIT + "314"),
                0
        );

        Assert.assertEquals(
                1598.31654,
                 Converter.getDoubleValue("1598" + Settings.SPLIT + "31654p"),
                0
        );

        Assert.assertEquals(
                1598.0,
                 Converter.getDoubleValue("1598" + Settings.SPLIT + "00p"),
                0
        );
    }
}