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
        //  StringBuilder for accumulating the contents of a token
        StringBuilder tokenBuilder = new StringBuilder();
        //  Whether or not any substitution was made
        boolean substitution = true;
        while (repeatCount < maxRepeatCount && substitution) {
            substitution = false;
            //  Resolve curly brace tokens first
            char[] chars = working.toCharArray();
            boolean isEscaped = false;
            boolean isInTag = false;
            //  Track how many layers deep we are for curly brace tags - each pass will strip away the outermost layer
            int curlyBraceDepth = 0;
            for (int pos = 0; pos < chars.length; pos++) {
                char c = chars[pos];
                if (isEscaped) {
                    if (isInTag) {
                        tokenBuilder.append(c);
                    } else {
                        builder.append(c);
                    }
                    isEscaped = false;
                } else {
                    switch (c) {
                        case '\\':
                            //  Escape next character
                            isEscaped = true;
                            break;
                        case '{':
                            //  Start curly brace tag
                            isInTag = true;
                            curlyBraceDepth++;
                            //  Reset our token StringBuilder
                            tokenBuilder.setLength(0);
                            break;
                        case '}':
                            if (isInTag) {
                                curlyBraceDepth--;
                                if (curlyBraceDepth == 0) {
                                    builder.append(processCurlyBraceToken(tokenBuilder.toString(), args));
                                    isInTag = false;
                                    substitution = true;
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
            //  Now resolve square bracket tags
            //  Reset token related vars
            working = builder.toString();
            chars = working.toCharArray();
            builder.setLength(0);
            tokenBuilder.setLength(0);
            isEscaped = false;
            isInTag = false;
            for (int pos = 0; pos < chars.length; pos++) {
                char c = chars[pos];
                if (isEscaped) {
                    if (isInTag) {
                        tokenBuilder.append(c);
                    } else {
                        builder.append(c);
                    }
                    isEscaped = false;
                } else {
                    switch (c) {
                        case '\\':
                            //  Escape next character
                            isEscaped = true;
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
                                substitution = true;
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
            //  Prep for next iteration
            working = builder.toString();
            repeatCount++;
        }
        return working;
    }

    private String processCurlyBraceToken(String tokenContents, Object[] args) {
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
