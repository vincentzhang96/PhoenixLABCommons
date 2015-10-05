package co.phoenixlab.common.localization;

import co.phoenixlab.common.lang.ReverseListIterator;

import java.util.*;

import static co.phoenixlab.common.localization.Localizer.internalIsFlagBitSet;
import static co.phoenixlab.common.localization.Localizer.stripFlags;

public class LocalizerImpl implements Localizer {

    private final int maxRepeatCount;
    private final Locale locale;
    private final List<LocaleStringProvider> providers;
    private final Collection<LocaleStringProvider> providersUnmodifiable;

    public LocalizerImpl(Locale locale) {
        Objects.requireNonNull(locale, "Locale cannot be null");
        this.locale = locale;
        this.providers = new ArrayList<>();
        this.providersUnmodifiable = Collections.unmodifiableCollection(providers);
        maxRepeatCount = Integer.getInteger("co.phoenixlab.localizer.fmt.limits.repeat", 8);
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void addLocaleStringProvider(LocaleStringProvider provider) {
        Objects.requireNonNull(provider, "Provider cannot be null");
        provider.setActiveLocale(locale);
        providers.add(provider);
    }

    @Override
    public void removeLocaleStringProvider(LocaleStringProvider provider) {
        Objects.requireNonNull(provider, "Provider cannot be null");
        providers.remove(provider);
    }

    @Override
    public Collection<LocaleStringProvider> getLocaleStringProviders() {
        return providersUnmodifiable;
    }

    @Override
    public void removeAllLocaleStringProviders() {
        providers.clear();
    }

    @Override
    public boolean containsKey(String key) {
        return providers.stream().
                anyMatch(p -> p.contains(key));
    }

    @Override
    public String localize(String key) {
        if (key == null) {
            return LOCALE_STRING_NOT_FOUND;
        }

        return null;
    }

    @Override
    public String localize(String key, Object arg0) {
        if (key == null) {
            return LOCALE_STRING_NOT_FOUND;
        }

        return null;
    }

    @Override
    public String localize(String key, Object arg0, Object arg1) {
        if (key == null) {
            return LOCALE_STRING_NOT_FOUND;
        }

        return null;
    }

    @Override
    public String localize(String key, Object... args) {
        if (key == null) {
            return LOCALE_STRING_NOT_FOUND;
        }
        String cleanKey = stripFlags(key);
        String ret = cleanKey;
        if (!internalIsFlagBitSet(key, PREFIX_FLAG_DO_NOT_LOCALIZE_BIT)) {
            //  Localize
            ret = lookup(cleanKey);
            if (ret == null) {
                return LOCALE_STRING_NOT_FOUND;
            }
        }
        if (!internalIsFlagBitSet(key, PREFIX_FLAG_DO_NOT_FORMAT_BIT)) {
            //  Format
            ret = format(cleanKey, ret, args);
        }
        return ret;
    }

    /**
     * Finds the value of the given key from the providers. This method iterates through the providers in a stack-like
     * fashion; that is, the last provider added is checked first, second last provider checked second, and so on.
     * This allows for proper overriding/priority of providers.
     *
     * @param key The key to look up
     * @return The value associated with the given key, or null if no provider could provide the requested value
     */
    private String lookup(String key) {
        ReverseListIterator<LocaleStringProvider> iterator = new ReverseListIterator<>(providers.listIterator());
        for (LocaleStringProvider provider : iterator) {
            String val = provider.get(key);
            if (val != null) {
                return val;
            }
        }
        return null;
    }

    /**
     * Performs the formatting step, as detailed in {@link #localize(String, Object...)}
     *
     * @param s    The string containing optional tags to be formatted
     * @param args The arguments for formatting
     * @return The formatted string
     */
    private String format(String key, String s, Object[] args) {
        //  Formatting is done by repeatedly resolving curly brace tokens and then square bracket tokens repeatedly
        //  until no more of either remain in the resultant string
        //  There is a maximum repeat limit to prevent infinite loops or unbounded string
        //  growth, governed by the system property co.phoenixlab.localizer.fmt.limits.repeat

        //  Number of times we've passed over the entire string
        int repeatCount = 0;
        //  Our current working string
        String working = s;
        //  StringBuilder for building the resultant for each pass
        StringBuilder builder = new StringBuilder();
        //  Whether or not any substitution was made
        boolean substitution = true;
        while (repeatCount < maxRepeatCount && substitution) {
            try {
                //  Resolve curly brace tokens first
                char[] chars = working.toCharArray();
                substitution = processCurlyTokens(builder, chars, args);

                //  Now resolve square bracket tags
                //  Reset
                int len = builder.length();
                if (len == chars.length) {
                    builder.getChars(0, len, chars, 0);
                } else {
                    chars = builder.toString().toCharArray();
                }
                builder.setLength(0);
                substitution |= processSquareBracketTokens(builder, chars, key);
                //  Prep for next iteration
                working = builder.toString();
                repeatCount++;
            } catch (IllegalArgumentException e) {
                return INVALID_FORMAT_STRING;
            }
        }
        return working;
    }

    private boolean processCurlyTokens(StringBuilder builder, char[] chars, Object[] args) {
        boolean hasSubstitutionBeenMade = false;
        StringBuilder tokenBuilder = new StringBuilder();
        boolean isNextCharEscaped = false;
        boolean isInTag = false;
        int braceDepth = 0;
        for (char c : chars) {
            if (isNextCharEscaped) {
                if (isInTag) {
                    tokenBuilder.append(c);
                } else {
                    builder.append(c);
                }
                isNextCharEscaped = false;
            } else {
                switch (c) {
                    case '\\':
                        //  Escape next character
                        isNextCharEscaped = true;
                        break;
                    case '{':
                        //  Start curly brace tag
                        isInTag = true;
                        braceDepth++;
                        //  Reset our token StringBuilder
                        tokenBuilder.setLength(0);
                        break;
                    case '}':
                        if (isInTag) {
                            braceDepth--;
                            if (braceDepth == 0) {
                                builder.append(processCurlyBraceToken(tokenBuilder.toString(), args));
                                isInTag = false;
                                hasSubstitutionBeenMade = true;
                                break;
                            }
                            //  FALL THROUGH if not outmoster layer of tag
                        }
                        //  FALL THROUGH if not in curly tag
                    default:
                        if (isInTag) {
                            tokenBuilder.append(c);
                        } else {
                            builder.append(c);
                        }
                }
            }
        }
        if (isNextCharEscaped) {
            //  Backslash at end of string, not fatal so we just insert it
            builder.append('\\');
        }
        if (braceDepth > 0) {
            //  There's an unclosed tag somewhere
            throw new IllegalArgumentException();
        }
        return hasSubstitutionBeenMade;
    }

    private boolean processSquareBracketTokens(StringBuilder builder, char[] chars, String key) {
        boolean hasSubstitutionBeenMade = false;
        StringBuilder tokenBuilder = new StringBuilder();
        boolean isNextCharEscaped = false;
        boolean isInTag = false;
        for (char c : chars) {
            if (isNextCharEscaped) {
                if (isInTag) {
                    tokenBuilder.append(c);
                } else {
                    builder.append(c);
                }
                isNextCharEscaped = false;
            } else {
                switch (c) {
                    case '\\':
                        //  Escape next character
                        isNextCharEscaped = true;
                        break;
                    case '[':
                        //  Start square bracket tag
                        isInTag = true;
                        //  Reset our token StringBuilder
                        tokenBuilder.setLength(0);
                        break;
                    case ']':
                        if (isInTag) {
                            builder.append(resolveSubkey(key, tokenBuilder.toString()));
                            isInTag = false;
                            hasSubstitutionBeenMade = true;
                            break;
                        }
                        //  FALL THROUGH if not in square tag
                    default:
                        if (isInTag) {
                            tokenBuilder.append(c);
                        } else {
                            builder.append(c);
                        }
                }
            }
        }
        if (isNextCharEscaped) {
            //  Backslash at end of string, not fatal so we just insert it
            builder.append('\\');
        }
        if (isInTag) {
            //  There's an unclosed tag somewhere
            throw new IllegalArgumentException();
        }
        return hasSubstitutionBeenMade;
    }

    private String processCurlyBraceToken(String tokenContents, Object[] args) {
        /*
        Format:
        ARG_NUMBER|FORMAT_DESCRIPTOR
        ARG_NUMBER: The argument index to use
        FORMAT_DESCRIPTOR: The way the argument should be formatted when inserted into the string

        FORMAT_DESCRIPTOR format:
        %FORMAT_STRING: Standard String.format() format string
        #date[|DATE_FORMAT_STRING]: Formats the argument as a date, using the locale default short format if
            DATE_FORMAT_STRING is not provided
        #time[|TIME_FORMAT_STRING]: Formats the argument as a time, using the locale default short format if
            TIME_FORMAT_STRING is not provided
        #datetime[|DATE_TIME_FORMAT_STRING]: Formats the argument as a date and time, using the default short
            format if DATE_TIME_FORMAT_STRING is not provided
        (PLURALITY_ID,TEXT)[,more...]: A list of plurality matchers, using the given argument as the number.

        Plurality rules are evaluated left to right; whichever rule matches first will be used
         */
        String[] splits = tokenContents.split("\\|", 2);
        if (splits.length < 2) {
            throw new IllegalArgumentException();
        }
        int argId;
        try {
            argId = Integer.parseInt(splits[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        final Object arg = args[argId];
        String formatDescriptor = splits[1];
        char first = formatDescriptor.charAt(0);
        switch (first) {
            case '%':
                return handleStringFormat(formatDescriptor, arg);
            case '#':
                return handleDateTimeFormat(formatDescriptor, arg);
            case '(':
                return handlePluralityRules(formatDescriptor, arg);
            default:
                throw new IllegalArgumentException();
        }
    }

    private String handleStringFormat(String fmt, Object arg) {
        return null;
    }

    private String handleDateTimeFormat(String fmt, Object arg) {
        return null;
    }

    private String handlePluralityRules(String rules, Object arg) {
        return null;
    }

    private String resolveSubkey(String baseKey, String tokenContents) {
        if (tokenContents.length() == 0) {
            return LOCALE_STRING_NOT_FOUND;
        }
        boolean isRelative = tokenContents.charAt(0) == '.';
        String key;
        if (isRelative) {
            key = baseKey + tokenContents;
        } else {
            key = tokenContents;
        }
        return Optional.ofNullable(lookup(key)).orElse(LOCALE_STRING_NOT_FOUND);
    }

    @Override
    public String localizeOrDefault(String key, String def) {
        if (key == null) {
            return def;
        }

        return null;
    }

    @Override
    public String localizeOrDefault(String key, String def, Object arg0) {
        if (key == null) {
            return def;
        }

        return null;
    }

    @Override
    public String localizeOrDefault(String key, String def, Object arg0, Object arg1) {
        if (key == null) {
            return def;
        }

        return null;
    }

    @Override
    public String localizeOrDefault(String key, String def, Object... args) {
        if (key == null) {
            return def;
        }

        return null;
    }
}
