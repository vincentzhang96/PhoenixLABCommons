package co.phoenixlab.common.lang.number;

import org.junit.*;

import java.util.OptionalInt;

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

    @Test
    public void testParseDec_MaxSigned() throws Exception {
        int i = Integer.MAX_VALUE;
        String s = Integer.toString(i);
        assertEquals(i, ParseInt.parseDec(s));
    }

    @Test
    public void testParseDec_MaxUnsigned() throws Exception {
        int i = 0xFFFFFFFF;
        String s = "4294967295";
        assertEquals(i, ParseInt.parseDec(s));
    }

    //  For values larger than the unsigned max, the method shouldn't thrown an exception but it will return a
    //  garbage result
    @Test
    public void testParseDec_UnsignedOverflow() throws Exception {
        String s = "4294967296";
        ParseInt.parseDec(s);
    }

    @Test
    public void testParseDec_MinSigned() throws Exception {
        int i = Integer.MIN_VALUE;
        String s = Integer.toString(i);
        assertEquals(i, ParseInt.parseDec(s));
    }

    //  For values lower than the signed min, the method shouldn't thrown an exception but it will return a
    //  garbage result
    @Test
    public void testParseDec_MinSignedUnderflow() throws Exception {
        String s = "-2147483649";
        ParseInt.parseDec(s);
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

    //  parseDec0() is technically internal and makes no public guarantees besides declaring what it accepts.
    //  It is fully tested by parseDec()'s tests

    @Test
    public void testParseHex() throws Exception {
        int i = 123456;
        String s = Integer.toHexString(i);
        assertEquals(i, ParseInt.parseHex(s));
    }

    @Test
    public void testParseHexPrefixEquals() throws Exception {
        int i = 123456;
        String s = Integer.toHexString(i);
        String s1 = "0x" + s;
        String s2 = "0X" + s;
        //  Transitive property means we only have to test twice
        assertEquals(ParseInt.parseHex(s), ParseInt.parseHex(s1));
        assertEquals(ParseInt.parseHex(s1), ParseInt.parseHex(s2));
    }

    @Test
    public void testParseHexMaxSigned() throws Exception {
        int i = Integer.MAX_VALUE;
        String s = "0x" + Integer.toHexString(i);
        assertEquals(i, ParseInt.parseHex(s));
    }

    @Test
    public void testParseHexMaxUnsigned() throws Exception {
        int i = 0xFFFFFFFF;
        String s = "0xFFFFFFFF";
        assertEquals(i, ParseInt.parseHex(s));
    }

    //  For values larger than the unsigned max, the method shouldn't thrown an exception but it will return a
    //  garbage result
    @Test
    public void testParseHexMaxUnsignedOverflow() throws Exception {
        String s = "0x100000000";
        ParseInt.parseHex(s);
    }

    //  Technically this is the same as MaxUnsigned
    @Test
    public void testParseHexMinSigned() throws Exception {
        int i = Integer.MIN_VALUE;
        String s = "0x" + Integer.toHexString(i);
        assertEquals(i, ParseInt.parseHex(s));
    }

    //  Test negative sign prefix reject (before prefix)
    @Test(expected = NumberFormatException.class)
    public void testParseHex_Negative1() throws Exception {
        String s = "-0x12AC";
        ParseInt.parseHex(s);
    }

    //  Test negative sign prefix reject (after prefix)
    @Test(expected = NumberFormatException.class)
    public void testParseHex_Negative2() throws Exception {
        String s = "0x-12AC";
        ParseInt.parseHex(s);
    }

    //  Test negative sign prefix reject (no prefix)
    @Test(expected = NumberFormatException.class)
    public void testParseHex_Negative3() throws Exception {
        String s = "-12AC";
        ParseInt.parseHex(s);
    }

    //  Test bad
    @Test(expected = NumberFormatException.class)
    public void testParseHex_Bad() throws Exception {
        ParseInt.parseHex("12AB-**&1'");
    }

    //  Test null
    @Test(expected = NumberFormatException.class)
    public void testParseHex_Null() throws Exception {
        ParseInt.parseHex(null);
    }

    //  Test positive sign prefix reject (before prefix)
    @Test(expected = NumberFormatException.class)
    public void testParseHex_Positive1() throws Exception {
        String s = "+0x12AB";
        ParseInt.parseHex(s);
    }

    //  Test positive sign prefix reject (after prefix)
    @Test(expected = NumberFormatException.class)
    public void testParseHex_Positive2() throws Exception {
        String s = "0x+12AB";
        ParseInt.parseHex(s);
    }

    //  Test positive sign prefix reject (no prefix)
    @Test(expected = NumberFormatException.class)
    public void testParseHex_Positive3() throws Exception {
        String s = "+12AB";
        ParseInt.parseHex(s);
    }

    //  parseHex0() is technically internal and makes no public guarantees besides declaring what it accepts.
    //  It is fully tested by parseHex()'s tests

    @Test
    public void testParseOrDefaultDecOk() throws Exception {
        int i = 12;
        String s = Integer.toString(i);
        int def = 14;
        assertEquals(i, ParseInt.parseOrDefault(s, def));
    }

    @Test
    public void testParseOrDefaultHexOk() throws Exception {
        int i = 0x12;
        String s = "0x" + Integer.toHexString(i);
        int def = 14;
        assertEquals(i, ParseInt.parseOrDefault(s, def));
    }

    @Test
    public void testParseOrDefaultDecDefault() throws Exception {
        String s = "abcd";
        int def = 14;
        assertEquals(def, ParseInt.parseOrDefault(s, def));
    }

    @Test
    public void testParseOrDefaultHexDefault() throws Exception {
        String s = "0xq12.g";
        int def = 14;
        assertEquals(def, ParseInt.parseOrDefault(s, def));
    }

    @Test
    public void testParseOrDefaultNullDefault() throws Exception {
        int def = 14;
        assertEquals(def, ParseInt.parseOrDefault(null, def));
    }

    @Test
    public void testParseDecOrDefaultOk() throws Exception {
        int i = 12;
        String s = Integer.toString(i);
        int def = 14;
        assertEquals(i, ParseInt.parseDecOrDefault(s, def));
    }

    @Test
    public void testParseDecOrDefaultFail() throws Exception {
        String s = "abc";
        int def = 14;
        assertEquals(def, ParseInt.parseDecOrDefault(s, def));
    }

    @Test
    public void testParseDecOrDefaultNull() throws Exception {
        int def = 14;
        assertEquals(def, ParseInt.parseDecOrDefault(null, def));
    }

    @Test
    public void testParseHexOrDefaultOk() throws Exception {
        int i = 0x12;
        String s = "0x" + Integer.toHexString(i);
        int def = 14;
        assertEquals(i, ParseInt.parseHexOrDefault(s, def));
    }

    @Test
    public void testParseHexOrDefaultFail() throws Exception {
        String s = "aq12.g";
        int def = 14;
        assertEquals(def, ParseInt.parseHexOrDefault(s, def));
    }

    @Test
    public void testParseHexOrDefaultNull() throws Exception {
        int def = 14;
        assertEquals(def, ParseInt.parseHexOrDefault(null, def));
    }


    @Test
    public void testParseOptionalDecOk() throws Exception {
        int i = 14;
        String s = Integer.toString(i);
        OptionalInt ret = ParseInt.parseOptional(s);
        assertTrue(ret.isPresent());
        assertEquals(i, ret.getAsInt());
    }

    @Test
    public void testParseOptionalHexOk() throws Exception {
        int i = 0x14;
        String s = "0x" + Integer.toHexString(i);
        OptionalInt ret = ParseInt.parseOptional(s);
        assertTrue(ret.isPresent());
        assertEquals(i, ret.getAsInt());
    }

    @Test
    public void testParseOptionalDecFail() throws Exception {
        String s = "12akjhsdr?";
        OptionalInt ret = ParseInt.parseOptional(s);
        assertFalse(ret.isPresent());
    }

    @Test
    public void testParseOptionalHexFail() throws Exception {
        String s = "0x12akjhsdr?";
        OptionalInt ret = ParseInt.parseOptional(s);
        assertFalse(ret.isPresent());
    }

    @Test
    public void testParseOptionalNullFail() throws Exception {
        OptionalInt ret = ParseInt.parseOptional(null);
        assertFalse(ret.isPresent());
    }

    @Test
    public void testParseDecOptionalOk() throws Exception {
        int i = 14;
        String s = Integer.toString(i);
        OptionalInt ret = ParseInt.parseDecOptional(s);
        assertTrue(ret.isPresent());
        assertEquals(i, ret.getAsInt());
    }

    @Test
    public void testParseDecOptionalFail() throws Exception {
        String s = "astg2148465klkast12hkls";
        OptionalInt ret = ParseInt.parseDecOptional(s);
        assertFalse(ret.isPresent());
    }

    @Test
    public void testParseDecOptionalNull() throws Exception {
        OptionalInt ret = ParseInt.parseDecOptional(null);
        assertFalse(ret.isPresent());
    }

    @Test
    public void testParseHexOptionalOk() throws Exception {
        int i = 0x14;
        String s = Integer.toHexString(i);
        OptionalInt ret = ParseInt.parseHexOptional(s);
        assertTrue(ret.isPresent());
        assertEquals(i, ret.getAsInt());
    }

    @Test
    public void testParseHexOptionalFail() throws Exception {
        String s = "astg2148465klkast12hkls";
        OptionalInt ret = ParseInt.parseHexOptional(s);
        assertFalse(ret.isPresent());
    }

    @Test
    public void testParseHexOptionalNull() throws Exception {
        OptionalInt ret = ParseInt.parseHexOptional(null);
        assertFalse(ret.isPresent());
    }
}
