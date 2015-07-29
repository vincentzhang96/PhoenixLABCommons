package co.phoenixlab.common.number;

import org.junit.*;

import java.util.OptionalLong;

import static org.junit.Assert.*;

public class ParseLongTest {

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
        long i = 123456;
        String s = Long.toString(i);
        assertEquals(i, ParseLong.parse(s));
    }

    //  Test dec/hex picking
    @Test
    public void testParse_Hex() throws Exception {
        long i = 0x123ABC;
        String s = "0x" + Long.toHexString(i);
        assertEquals(i, ParseLong.parse(s));
    }

    //  Test dec/hex dec trimming
    @Test
    public void testParse_DecTrim() throws Exception {
        long i = 123456;
        String s = WHITESPACE + Long.toString(i) + WHITESPACE;
        assertEquals(i, ParseLong.parse(s));
    }

    //  Test dec/hex hex trimming
    @Test
    public void testParse_HexTrim() throws Exception {
        long i = 0x123ABC;
        String s = WHITESPACE + "0x" + Long.toHexString(i) + WHITESPACE;
        assertEquals(i, ParseLong.parse(s));
    }

    //  Invalid hex/dec handled in tests for parseDec0 and parseHex0

    //  Test null reject
    @Test(expected = NumberFormatException.class)
    public void testParse_Null() throws Exception {
        ParseLong.parse(null);
    }

    //  Test empty string reject
    @Test(expected = NumberFormatException.class)
    public void testParse_EmptyDec() throws Exception {
        String s = "";
        ParseLong.parse(s);
    }

    //  Test missing hex digits reject
    @Test(expected = NumberFormatException.class)
    public void testParse_EmptyHex() throws Exception {
        String s = "0x";
        ParseLong.parse(s);
    }

    @Test
    public void testParseDec() throws Exception {
        long i = 123456;
        String s = Long.toString(i);
        assertEquals(i, ParseLong.parseDec(s));
    }

    //  Test negative sign prefix
    @Test
    public void testParseDec_Negative() throws Exception {
        long i = -123456;
        String s = Long.toString(i);
        assertEquals(i, ParseLong.parseDec(s));
    }

    @Test
    public void testParseDec_MaxSigned() throws Exception {
        long i = Long.MAX_VALUE;
        String s = Long.toString(i);
        assertEquals(i, ParseLong.parseDec(s));
    }

    @Test
    public void testParseDec_MaxUnsigned() throws Exception {
        long i = 0xFFFFFFFF;
        String s = "18446744073709551615";
        assertEquals(i, ParseLong.parseDec(s));
    }

    //  For values larger than the unsigned max, the method shouldn't thrown an exception but it will return a
    //  garbage result
    @Test
    public void testParseDec_UnsignedOverflow() throws Exception {
        String s = "18446744073709551616";
        ParseLong.parseDec(s);
    }

    @Test
    public void testParseDec_MinSigned() throws Exception {
        long i = Long.MIN_VALUE;
        String s = Long.toString(i);
        assertEquals(i, ParseLong.parseDec(s));
    }

    //  For values lower than the signed min, the method shouldn't thrown an exception but it will return a
    //  garbage result
    @Test
    public void testParseDec_MinSignedUnderflow() throws Exception {
        String s = "-9223372036854775809";
        ParseLong.parseDec(s);
    }

    //  Test bad
    @Test(expected = NumberFormatException.class)
    public void testParseDec_Bad() throws Exception {
        ParseLong.parseDec("12AB-**&1'");
    }

    //  Test null
    @Test(expected = NumberFormatException.class)
    public void testParseDec_Null() throws Exception {
        ParseLong.parseDec(null);
    }

    //  Test positive sign prefix
    @Test
    public void testParseDec_Positive() throws Exception {
        long i = 123456;
        String s = "+" + Long.toString(i);
        assertEquals(i, ParseLong.parseDec(s));
    }

    //  Test negative sign inside
    @Test(expected = NumberFormatException.class)
    public void testParseDec_NegativeBad() throws Exception {
        ParseLong.parseDec("-12-34");
    }

    //  Test positive sign inside
    @Test(expected = NumberFormatException.class)
    public void testParseDec_PositiveBad() throws Exception {
        ParseLong.parseDec("+12+34");
    }

    //  parseDec0() is technically internal and makes no public guarantees besides declaring what it accepts.
    //  It is fully tested by parseDec()'s tests

    @Test
    public void testParseHex() throws Exception {
        long i = 123456;
        String s = Long.toHexString(i);
        assertEquals(i, ParseLong.parseHex(s));
    }

    @Test
    public void testParseHexPrefixEquals() throws Exception {
        long i = 123456;
        String s = Long.toHexString(i);
        String s1 = "0x" + s;
        String s2 = "0X" + s;
        //  Transitive property means we only have to test twice
        assertEquals(ParseLong.parseHex(s), ParseLong.parseHex(s1));
        assertEquals(ParseLong.parseHex(s1), ParseLong.parseHex(s2));
    }

    @Test
    public void testParseHexMaxSigned() throws Exception {
        long i = Long.MAX_VALUE;
        String s = "0x" + Long.toHexString(i);
        assertEquals(i, ParseLong.parseHex(s));
    }

    @Test
    public void testParseHexMaxUnsigned() throws Exception {
        long i = 0xFFFFFFFFFFFFFFFFL;
        String s = "0xFFFFFFFFFFFFFFFF";
        assertEquals(i, ParseLong.parseHex(s));
    }

    //  For values larger than the unsigned max, the method shouldn't thrown an exception but it will return a
    //  garbage result
    @Test
    public void testParseHexMaxUnsignedOverflow() throws Exception {
        String s = "0x10000000000000000";
        ParseLong.parseHex(s);
    }

    //  Technically this is the same as MaxUnsigned
    @Test
    public void testParseHexMinSigned() throws Exception {
        long i = Long.MIN_VALUE;
        String s = "0x" + Long.toHexString(i);
        assertEquals(i, ParseLong.parseHex(s));
    }

    //  Test negative sign prefix reject (before prefix)
    @Test(expected = NumberFormatException.class)
    public void testParseHex_Negative1() throws Exception {
        String s = "-0x12AC";
        ParseLong.parseHex(s);
    }

    //  Test negative sign prefix reject (after prefix)
    @Test(expected = NumberFormatException.class)
    public void testParseHex_Negative2() throws Exception {
        String s = "0x-12AC";
        ParseLong.parseHex(s);
    }

    //  Test negative sign prefix reject (no prefix)
    @Test(expected = NumberFormatException.class)
    public void testParseHex_Negative3() throws Exception {
        String s = "-12AC";
        ParseLong.parseHex(s);
    }

    //  Test bad
    @Test(expected = NumberFormatException.class)
    public void testParseHex_Bad() throws Exception {
        ParseLong.parseHex("12AB-**&1'");
    }

    //  Test null
    @Test(expected = NumberFormatException.class)
    public void testParseHex_Null() throws Exception {
        ParseLong.parseHex(null);
    }

    //  Test positive sign prefix reject (before prefix)
    @Test(expected = NumberFormatException.class)
    public void testParseHex_Positive1() throws Exception {
        String s = "+0x12AB";
        ParseLong.parseHex(s);
    }

    //  Test positive sign prefix reject (after prefix)
    @Test(expected = NumberFormatException.class)
    public void testParseHex_Positive2() throws Exception {
        String s = "0x+12AB";
        ParseLong.parseHex(s);
    }

    //  Test positive sign prefix reject (no prefix)
    @Test(expected = NumberFormatException.class)
    public void testParseHex_Positive3() throws Exception {
        String s = "+12AB";
        ParseLong.parseHex(s);
    }

    //  parseHex0() is technically internal and makes no public guarantees besides declaring what it accepts.
    //  It is fully tested by parseHex()'s tests

    @Test
    public void testParseOrDefaultDecOk() throws Exception {
        long i = 12;
        String s = Long.toString(i);
        int def = 14;
        assertEquals(i, ParseLong.parseOrDefault(s, def));
    }

    @Test
    public void testParseOrDefaultHexOk() throws Exception {
        long i = 0x12;
        String s = "0x" + Long.toHexString(i);
        int def = 14;
        assertEquals(i, ParseLong.parseOrDefault(s, def));
    }

    @Test
    public void testParseOrDefaultDecDefault() throws Exception {
        String s = "abcd";
        int def = 14;
        assertEquals(def, ParseLong.parseOrDefault(s, def));
    }

    @Test
    public void testParseOrDefaultHexDefault() throws Exception {
        String s = "0xq12.g";
        int def = 14;
        assertEquals(def, ParseLong.parseOrDefault(s, def));
    }

    @Test
    public void testParseOrDefaultNullDefault() throws Exception {
        int def = 14;
        assertEquals(def, ParseLong.parseOrDefault(null, def));
    }

    @Test
    public void testParseDecOrDefaultOk() throws Exception {
        long i = 12;
        String s = Long.toString(i);
        int def = 14;
        assertEquals(i, ParseLong.parseDecOrDefault(s, def));
    }

    @Test
    public void testParseDecOrDefaultFail() throws Exception {
        String s = "abc";
        int def = 14;
        assertEquals(def, ParseLong.parseDecOrDefault(s, def));
    }

    @Test
    public void testParseDecOrDefaultNull() throws Exception {
        int def = 14;
        assertEquals(def, ParseLong.parseDecOrDefault(null, def));
    }

    @Test
    public void testParseHexOrDefaultOk() throws Exception {
        long i = 0x12;
        String s = "0x" + Long.toHexString(i);
        int def = 14;
        assertEquals(i, ParseLong.parseHexOrDefault(s, def));
    }

    @Test
    public void testParseHexOrDefaultFail() throws Exception {
        String s = "aq12.g";
        int def = 14;
        assertEquals(def, ParseLong.parseHexOrDefault(s, def));
    }

    @Test
    public void testParseHexOrDefaultNull() throws Exception {
        int def = 14;
        assertEquals(def, ParseLong.parseHexOrDefault(null, def));
    }


    @Test
    public void testParseOptionalDecOk() throws Exception {
        long i = 14;
        String s = Long.toString(i);
        OptionalLong ret = ParseLong.parseOptional(s);
        assertTrue(ret.isPresent());
        assertEquals(i, ret.getAsLong());
    }

    @Test
    public void testParseOptionalHexOk() throws Exception {
        long i = 0x14;
        String s = "0x" + Long.toHexString(i);
        OptionalLong ret = ParseLong.parseOptional(s);
        assertTrue(ret.isPresent());
        assertEquals(i, ret.getAsLong());
    }

    @Test
    public void testParseOptionalDecFail() throws Exception {
        String s = "12akjhsdr?";
        OptionalLong ret = ParseLong.parseOptional(s);
        assertFalse(ret.isPresent());
    }

    @Test
    public void testParseOptionalHexFail() throws Exception {
        String s = "0x12akjhsdr?";
        OptionalLong ret = ParseLong.parseOptional(s);
        assertFalse(ret.isPresent());
    }

    @Test
    public void testParseOptionalNullFail() throws Exception {
        OptionalLong ret = ParseLong.parseOptional(null);
        assertFalse(ret.isPresent());
    }

    @Test
    public void testParseDecOptionalOk() throws Exception {
        long i = 14;
        String s = Long.toString(i);
        OptionalLong ret = ParseLong.parseDecOptional(s);
        assertTrue(ret.isPresent());
        assertEquals(i, ret.getAsLong());
    }

    @Test
    public void testParseDecOptionalFail() throws Exception {
        String s = "astg2148465klkast12hkls";
        OptionalLong ret = ParseLong.parseDecOptional(s);
        assertFalse(ret.isPresent());
    }

    @Test
    public void testParseDecOptionalNull() throws Exception {
        OptionalLong ret = ParseLong.parseDecOptional(null);
        assertFalse(ret.isPresent());
    }

    @Test
    public void testParseHexOptionalOk() throws Exception {
        long i = 0x14;
        String s = Long.toHexString(i);
        OptionalLong ret = ParseLong.parseHexOptional(s);
        assertTrue(ret.isPresent());
        assertEquals(i, ret.getAsLong());
    }

    @Test
    public void testParseHexOptionalFail() throws Exception {
        String s = "astg2148465klkast12hkls";
        OptionalLong ret = ParseLong.parseHexOptional(s);
        assertFalse(ret.isPresent());
    }

    @Test
    public void testParseHexOptionalNull() throws Exception {
        OptionalLong ret = ParseLong.parseHexOptional(null);
        assertFalse(ret.isPresent());
    }
}
