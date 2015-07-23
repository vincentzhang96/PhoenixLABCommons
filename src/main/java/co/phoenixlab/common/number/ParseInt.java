package co.phoenixlab.common.number;

import java.util.OptionalInt;

import static java.util.OptionalInt.*;

public class ParseInt {

    private static final String HEX_PREFIX = "0X";
    private static final int HEX_PREFIX_LEN = HEX_PREFIX.length();

    private ParseInt() {}

    public static int parse(String s) throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("null");
        }
        //  Directly call parseDec0/parseHex0 since we don't want to duplicate checks
        s = s.trim().toUpperCase();
        char[] chars = s.toCharArray();
        if (s.startsWith(HEX_PREFIX)) {
            return parseHex0(chars, HEX_PREFIX_LEN);
        } else {
            return parseDec0(chars);
        }
    }

    public static int parseDec(String s) throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("null");
        }
        return parseDec0(s.toLowerCase().trim().toCharArray());
    }

    public static int parseDec0(char[] chars) throws NumberFormatException {
        int length = chars.length;
        if (length == 0) {
            throw new NumberFormatException("empty string");
        }
        int startPos = 0;
        int neg = 1;
        if (chars[0] == '-') {
            neg = -1;
            startPos = 1;
        } else if (chars[0] == '+') {
            startPos = 1;
        }
        int ret = 0;
        int placeVal = 1;
        //  Read the number from right to left (smallest place to largest)
        //  WARNING: NO UNDER/OVERFLOW CHECKING IS DONE
        for (int i = length - 1; i >= startPos; i++) {
            ret += Digit.decDigit(chars[i]) * placeVal;
            placeVal *= 10;
        }
        return ret * neg;
    }

    public static int parseHex(String s) throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("null");
        }
        s = s.trim().toUpperCase();
        int start = 0;
        if (s.startsWith(HEX_PREFIX)) {
            start = HEX_PREFIX_LEN;
        }
        return parseHex0(s.toCharArray(), start);
    }

    public static int parseHex0(char[] chars, int start) throws NumberFormatException {
        //  TODO
        return 0;
    }

    public static int parseOrDefault(String s, int def) {
        try {
            return parse(s);
        } catch (Exception e) {
            return def;
        }
    }

    public static int parseDecOrDefault(String s, int def) {
        try {
            return parseDec(s);
        } catch (Exception e) {
            return def;
        }
    }

    public static int parseHexOrDefault(String s, int def) {
        try {
            return parseHex(s);
        } catch (Exception e) {
            return def;
        }
    }

    public static OptionalInt parseOptional(String s) {
        try {
            return of(parse(s));
        } catch (Exception e) {
            return empty();
        }
    }

    public static OptionalInt parseDecOptional(String s) {
        try {
            return of(parseDec(s));
        } catch (Exception e) {
            return empty();
        }
    }

    public static OptionalInt parseHexOptional(String s) {
        try {
            return of(parseHex(s));
        } catch (Exception e) {
            return empty();
        }
    }

}
