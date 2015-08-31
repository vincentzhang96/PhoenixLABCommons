package co.phoenixlab.common.lang.number;

import java.util.OptionalLong;

import static java.util.OptionalLong.*;

/**
 * Contains various methods for fast integer parsing of decimal and hexadecimal strings.
 */
public class ParseLong {

    private static final String HEX_PREFIX = "0X";
    private static final int HEX_PREFIX_LEN = HEX_PREFIX.length();

    private ParseLong() {}

    /**
     * Attempts to parse a long from the given String. This method tests for a "0x"/"0X" prefix to determine
     * whether to parse the number as a hexadecimal number or a decimal number.
     * <p>
     * This method does <strong>NOT</strong> check for under/overflow.
     * @param s The String to parse
     * @return The parsed long
     * @throws NumberFormatException If the String was not able to be parsed as a long either in hex or dec or was
     * null
     * @see #parseHex(String)
     * @see #parseDec(String)
     */
    public static long parse(String s) throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("null");
        }
        //  Directly call parseDec0/parseHex0 since we don't want to duplicate checks
        s = s.trim().toUpperCase();
        char[] chars = s.toCharArray();
        if (chars.length == 0) {
            throw new NumberFormatException("empty string");
        }
        if (s.startsWith(HEX_PREFIX)) {
            if (chars.length == 2) {
                throw new NumberFormatException("missing hex digits");
            }
            return parseHex0(chars, HEX_PREFIX_LEN);
        } else {
            return parseDec0(chars);
        }
    }

    /**
     * Attempts to parse a long from the given String in decimal form. This method accepts an optional '+'/'-'
     * prefix to indicate sign.
     * <p>
     * This method does <strong>NOT</strong> check for under/overflow.
     * @param s The String containing a decimal long to be parsed
     * @return The parsed long
     * @throws NumberFormatException If the String was not able to be parsed or was null
     */
    public static long parseDec(String s) throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("null");
        }
        char[] chars = s.toLowerCase().trim().toCharArray();
        if (chars.length == 0) {
            throw new NumberFormatException("empty string");
        }
        return parseDec0(chars);
    }

    /**
     * Parses the given char array as a decimal long <strong>without performing sanitation or over/underflow
     * checks.</strong> Consider using {@link #parseDec(String)} instead.
     * <p>
     * This method expects an array of characters, optionally starting with '+'/'-' at index 0, consisting only of the
     * characters from '0' to '9'.
     * @param chars An array of chars consisting of the characters to be parsed as a long
     * @return The parsed long
     * @throws NumberFormatException If the chars do not represent a valid sequence of decimal digits optionally
     * prefixed with a '+'/'-'
     */
    public static long parseDec0(char[] chars) throws NumberFormatException {
        int length = chars.length;
        int startPos = 0;
        int neg = 1;
        if (chars[0] == '-') {
            neg = -1;
            startPos = 1;
        } else if (chars[0] == '+') {
            startPos = 1;
        }
        long ret = 0;
        long placeVal = 1;
        //  Read the number from right to left (smallest place to largest)
        //  WARNING: NO UNDER/OVERFLOW CHECKING IS DONE
        for (int i = length - 1; i >= startPos; i--) {
            ret += Digit.decDigit(chars[i]) * placeVal;
            placeVal *= 10;
        }
        return ret * neg;
    }

    /**
     * Attempts to parse a long from the given String in hexadecimal form. This method accepts an optional "0x"/"0X"
     * prefix which will be ignored. This method does <strong>NOT</strong> support using sign prefix ('+'/'-').
     * <p>
     * This method does <strong>NOT</strong> check for under/overflow.
     * @param s The String containing a decimal long to be parsed
     * @return The parsed long
     * @throws NumberFormatException If the String was not able to be parsed or was null
     */
    public static long parseHex(String s) throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("null");
        }
        s = s.trim().toUpperCase();
        int start = 0;
        if (s.startsWith(HEX_PREFIX)) {
            start = HEX_PREFIX_LEN;
        }
        char[] chars = s.toCharArray();
        if (chars.length == start) {
            throw new NumberFormatException("missing hex digits");
        }
        return parseHex0(s.toCharArray(), start);
    }

    /**
     * Parses the given char array as a hexadecimal long <strong>without performing sanitation or over/underflow
     * checks.</strong> Consider using {@link #parseHex(String)} instead.
     * <p>
     * This method expects an array of characters, <strong>without the "0x"/"0X" prefix, consisting only of the
     * characters from '0' to '9' and 'A' to 'Z' (no lowercase).
     * @param chars An array of chars consisting of the characters to be parsed as a long
     * @param start The starting index to start parsing from
     * @return The parsed long
     * @throws NumberFormatException If the chars do not represent a valid sequence of hexadecimal digits
     */
    public static long parseHex0(char[] chars, int start) throws NumberFormatException {
        int length = chars.length;
        long ret = 0;
        //  Read the number from right to left (smallest place to largest)
        //  WARNING: NO UNDER/OVERFLOW CHECKING IS DONE
        for (int i = length - 1; i >= start; i--) {
            ret += ((long)Digit.hexDigit(chars[i])) << ((length - 1 - i) * 4);
        }
        return ret;
    }

    /**
     * Parses the given String using {@link #parse(String)}, returning {@code def} if it failed to parse.
     * @param s The String to parse
     * @param def The default value to return if parsing fails
     * @return The parsed value from the String, or {@code def} if parsing failed
     * @see #parse(String)
     */
    public static long parseOrDefault(String s, int def) {
        try {
            return parse(s);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Parses the given String using {@link #parseDec(String)}, returning {@code def} if it failed to parse.
     * @param s The String to parse
     * @param def The default value to return if parsing fails
     * @return The parsed value from the String, or {@code def} if parsing failed
     * @see #parseDec(String)
     */
    public static long parseDecOrDefault(String s, int def) {
        try {
            return parseDec(s);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Parses the given String using {@link #parseHex(String)}, returning {@code def} if it failed to parse.
     * @param s The String to parse
     * @param def The default value to return if parsing fails
     * @return The parsed value from the String, or {@code def} if parsing failed
     * @see #parseHex(String)
     */
    public static long parseHexOrDefault(String s, int def) {
        try {
            return parseHex(s);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Parses the given String using {@link #parse(String)}, returning {@link OptionalLong#empty()} if it failed
     * to parse.
     * @param s The String to parse
     * @return An {@link OptionalLong} with the parsed value or empty if parsing failed.
     * @see #parse(String)
     */
    public static OptionalLong parseOptional(String s) {
        try {
            return of(parse(s));
        } catch (Exception e) {
            return empty();
        }
    }

    /**
     * Parses the given String using {@link #parseDec(String)}, returning {@link OptionalLong#empty()} if it failed
     * to parse.
     * @param s The String to parse
     * @return An {@link OptionalLong} with the parsed value or empty if parsing failed.
     * @see #parseDec(String)
     */
    public static OptionalLong parseDecOptional(String s) {
        try {
            return of(parseDec(s));
        } catch (Exception e) {
            return empty();
        }
    }

    /**
     * Parses the given String using {@link #parseHex(String)}, returning {@link OptionalLong#empty()} if it failed
     * to parse.
     * @param s The String to parse
     * @return An {@link OptionalLong} with the parsed value or empty if parsing failed.
     * @see #parseHex(String)
     */
    public static OptionalLong parseHexOptional(String s) {
        try {
            return of(parseHex(s));
        } catch (Exception e) {
            return empty();
        }
    }

}
