package co.phoenixlab.common.testutils;

import org.junit.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TestUtils {

    public static void testIsUtilityClass(Class<?> clazz) throws Exception {
        assertTrue(Modifier.isFinal(clazz.getModifiers()));
        assertEquals(1, clazz.getDeclaredConstructors().length);
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        assertFalse(constructor.isAccessible());
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
        constructor.setAccessible(false);
        Arrays.stream(clazz.getMethods()).
                filter(m -> !Modifier.isStatic(m.getModifiers())).
                filter(m -> m.getDeclaringClass().equals(clazz)).
                findFirst().
                map(Method::toString).
                ifPresent(Assert::fail);
    }
}
