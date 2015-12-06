package co.phoenixlab.common.localization;

import org.junit.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

public class LocalizerTest {

    private Map<String, String> strings;
    private Localizer localizer;

    @Before
    public void setup() {
        strings = new HashMap<>();
        localizer = new LocalizerImpl(Locale.US);
        localizer.registerPluralityRules(LocalizerImpl.defaultPluralityRules());
        localizer.addLocaleStringProvider(new LocaleStringProvider() {
            @Override
            public void setActiveLocale(Locale locale) {
                //  Ignore
            }

            @Override
            public String get(String key) {
                return strings.get(key);
            }

            @Override
            public boolean contains(String key) {
                return strings.containsKey(key);
            }
        });
    }

    @Test
    public void testExampleBaseStr() throws Exception {
        final String key = "test.string";
        final String val = "potato";
        strings.clear();
        strings.put(key, val);
        final String ret = localizer.localize(key);
        assertEquals(val, ret);
    }

    @Test
    public void testExampleStrFmt() throws Exception {
        final String key = "test.string";
        final String val = "{1|%d} potato {0|%s}";
        final String arg0 = "baked";
        final int arg1 = 45;
        final String expected = "45 potato baked";
        strings.clear();
        strings.put(key, val);
        final String ret = localizer.localize(key, arg0, arg1);
        assertEquals(expected, ret);
    }

    @Test
    public void testSimplePluralOne() throws Exception {
        final String key = "test.string";
        final String val = "{0|%d} {0|(one;potato),(en_plural;potatoes)}";
        final int arg0 = 1;
        final String expected = "1 potato";
        strings.clear();
        strings.put(key, val);
        final String ret = localizer.localize(key, arg0);
        assertEquals(expected, ret);
    }

    @Test
    public void testSimplePluralEnPlural() throws Exception {
        final String key = "test.string";
        final String val = "{0|%d} {0|(one;potato),(en_plural;potatoes)}";
        final int arg0 = 2;
        final String expected = "2 potatoes";
        strings.clear();
        strings.put(key, val);
        final String ret = localizer.localize(key, arg0);
        assertEquals(expected, ret);
    }

    @Test
    public void testOrPluralEnPlural() throws Exception {
        final String key = "test.string";
        final String val = "{0|(one,en_plural;potatoes)}";
        final int arg1 = 1;
        final int arg2 = 2;
        final String expected = "potatoes";
        strings.clear();
        strings.put(key, val);
        final String ret1 = localizer.localize(key, arg1);
        final String ret2 = localizer.localize(key, arg2);
        assertEquals(expected, ret1);
        assertEquals(expected, ret2);
    }

    @Test
    public void testAndPluralEnPlural() throws Exception {
        final String key = "test.string";
        final String val = "{0|(zero+en_plural;potatoes),(en_plural;potato)}";
        final int arg1 = 2;
        final String expected = "potato";
        strings.clear();
        strings.put(key, val);
        final String ret1 = localizer.localize(key, arg1);
        assertEquals(expected, ret1);
    }
}
