package co.phoenixlab.common.lang.number;

/**
 * Internal helper class for parsing decimal and hexadecimal digits
 */
class Digit {

    private Digit() {}

    /**
     * Returns the integer value of a character or throws an exception if the character does not represent a decimal
     * digit. Decimal digits are characters matching {@code [0-9]}.
     * @param c The character to convert to an integer value.
     * @return The integer value of the character, values from 0 to 9 inclusive.
     * @throws NumberFormatException If the provided character falls outside of {@code [0-9]}
     */
    static int decDigit(char c) throws NumberFormatException {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        throw new NumberFormatException("Unknown dec digit character '" + c + "'");
    }

    /**
     * Returns the integer value of a character or throws an exception if the character does not represent a hexadecimal
     * digit. Hexadecimal digits are characters matching {@code [0-9A-Z]}. This method does NOT accept lowercase.
     * @param c The character to convert to an integer value.
     * @return The integer value of the character, values from 0 to 15 inclusive.
     * @throws NumberFormatException If the provided character falls outside of {@code [0-9A-Z]}
     */
    static int hexDigit(char c) throws NumberFormatException {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            //  equiv to c - 'A' + 10, since 'A' is ASCII #65 and '7' is #55
            return c - '7';
        }
        throw new NumberFormatException("Unknown hex digit character '" + c + "'");
    }
}
