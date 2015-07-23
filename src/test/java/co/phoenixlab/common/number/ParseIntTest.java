package co.phoenixlab.common.number;

import org.junit.*;

import static org.junit.Assert.*;

public class ParseIntTest {

    private static final String WHITESPACE;

    static {
        //  Fill string with all "whitespace" chars (defined by String.trim() as any code below 0x21)
        char[] chars = new char[0x21];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) i;
        }
        WHITESPACE = new String(chars);
    }

    //  Test dec/hex picking
    @Test
    public void testParse_Dec() throws Exception {
        int i = 123456;
        String s = Integer.toString(i);
        assertEquals(i, ParseInt.parse(s));
    }

    //  Test dec/hex picking
    @Test
    public void testParse_Hex() throws Exception {
        int i = 0x123ABC;
        String s = "0x" + Integer.toHexString(i);
        assertEquals(i, ParseInt.parse(s));
    }

    //  Test dec/hex dec trimming
    @Test
    public void testParse_DecTrim() throws Exception {
        int i = 123456;
        String s = WHITESPACE + Integer.toString(i) + WHITESPACE;
        assertEquals(i, ParseInt.parse(s));
    }

    //  Test dec/hex hex trimming
    @Test
    public void testParse_HexTrim() throws Exception {
        int i = 0x123ABC;
        String s = WHITESPACE + "0x" + Integer.toHexString(i) + WHITESPACE;
        assertEquals(i, ParseInt.parse(s));
    }

    //  Invalid hex/dec handled in tests for parseDec0 and parseHex0

    //  Test null reject
    @Test(expected = NumberFormatException.class)
    public void testParse_Null() throws Exception {
        ParseInt.parse(null);
    }

    //  Test empty string reject
    @Test(expected = NumberFormatException.class)
    public void testParse_EmptyDec() throws Exception {
        String s = "";
        ParseInt.parse(s);
    }

    //  Test missing hex digits reject
    @Test(expected = NumberFormatException.class)
    public void testParse_EmptyHex() throws Exception {
        String s = "0x";
        ParseInt.parse(s);
    }

    @Test
    public void testParseDec() throws Exception {
        int i = 123456;
        String s = Integer.toString(i);
        assertEquals(i, ParseInt.parseDec(s));
    }

    //  Test negative sign prefix
    @Test
    public void testParseDec_Negative() throws Exception {
        int i = -123456;
        String s = Integer.toString(i);
        assertEquals(i, ParseInt.parseDec(s));
    }

    //  Test bad
    @Test(expected = NumberFormatException.class)
    public void testParseDec_Bad() throws Exception {
        ParseInt.parseDec("12AB-**&1'");
    }

    //  Test null
    @Test(expected = NumberFormatException.class)
    public void testParseDec_Null() throws Exception {
        ParseInt.parseDec(null);
    }

    //  Test positive sign prefix
    @Test
    public void testParseDec_Positive() throws Exception {
        int i = 123456;
        String s = "+" + Integer.toString(i);
        assertEquals(i, ParseInt.parseDec(s));
    }

    //  Test negative sign inside
    @Test(expected = NumberFormatException.class)
    public void testParseDec_NegativeBad() throws Exception {
        ParseInt.parseDec("-12-34");
    }

    //  Test positive sign inside
    @Test(expected = NumberFormatException.class)
    public void testParseDec_PositiveBad() throws Exception {
        ParseInt.parseDec("+12+34");
    }

    @Test
    public void testParseDec0() throws Exception {

    }

    @Test
    public void testParseHex() throws Exception {

    }

    @Test
    public void testParseHex0() throws Exception {

    }

    @Test
    public void testParseOrDefault() throws Exception {

    }

    @Test
    public void testParseDecOrDefault() throws Exception {

    }

    @Test
    public void testParseHexOrDefault() throws Exception {

    }

    @Test
    public void testParseOptional() throws Exception {

    }

    @Test
    public void testParseDecOptional() throws Exception {

    }

    @Test
    public void testParseHexOptional() throws Exception {

    }
}
