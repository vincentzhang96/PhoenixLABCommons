package co.phoenixlab.common.lang.number;

import co.phoenixlab.common.testutils.TestUtils;
import org.junit.*;

import static org.junit.Assert.*;

public class DigitTest {

    @Test
    public void testIsUtilityClass() throws Exception{
        TestUtils.testIsUtilityClass(Digit.class);
    }

    @Test
    public void testDecDigit() throws Exception {
        for (int i = 0; i < 10; i++) {
            char c = (char) ('0' + i);
            assertEquals(i, Digit.decDigit(c));
        }
    }

    @Test(expected = NumberFormatException.class)
    public void testDecDigitInvalid() throws Exception {
        Digit.decDigit('a');
    }

    @Test
    public void testHexDigit() throws Exception {
        for (int i = 0; i < 10; i++) {
            char c = (char) ('0' + i);
            assertEquals(i, Digit.hexDigit(c));
        }
        for (int i = 0; i < 6; i++) {
            char c = (char) ('A' + i);
            assertEquals(i + 10, Digit.hexDigit(c));
        }
    }

    @Test(expected = NumberFormatException.class)
    public void testHexDigitInvalid() throws Exception {
        Digit.decDigit('g');
    }

    @Test(expected = NumberFormatException.class)
    public void testHexDigitInvalidLowercase() throws Exception {
        Digit.decDigit('a');
    }
}
