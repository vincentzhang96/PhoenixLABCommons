package co.phoenixlab.common.lang;

import org.junit.*;

import java.util.Optional;

import static org.junit.Assert.*;

public class SafeNavTest {


    @Test
    public void testOf() throws Exception {
        Object o = new Object();
        SafeNav<Object> nav = SafeNav.of(o);
        assertNotNull(nav);
    }

    @Test
    public void testOfOptional() throws Exception {
        Optional<Object> optional = Optional.empty();
        SafeNav<Object> nav = SafeNav.ofOptional(optional);
        assertNotNull(nav);
    }

    @Test
    public void testGet() throws Exception {
        Object o = new Object();
        SafeNav<Object> nav = SafeNav.of(o);
        Object o1 = nav.get();
        assertSame(o, o1);
    }

    @Test
    public void testGetNull() throws Exception {
        SafeNav<Object> nav = SafeNav.of(null);
        Object o1 = nav.get();
        assertNull(o1);
    }

    @Test
    public void testGetFunct() throws Exception {
        Object o = new Object() {
            @Override
            public String toString() {
                return "potato";
            }
        };
        SafeNav<Object> nav = SafeNav.of(o);
        String s = nav.get(Object::toString);
        assertNotNull(s);
        assertEquals(o.toString(), s);
    }

    @Test
    public void testGetFunctNull() throws Exception {
        SafeNav<Object> nav = SafeNav.of(null);
        String s = nav.get(Object::toString);
        assertNull(s);
    }

    @Test
    public void testOrElse() throws Exception {
        Object o = new Object();
        Object o1 = new Object();
        SafeNav<Object> nav = SafeNav.of(o);
        Object o2 = nav.orElse(o1);
        assertSame(o, o2);
        assertNotSame(o1, o2);
    }

    @Test
    public void testOrElseNull() throws Exception {
        Object o1 = new Object();
        SafeNav<Object> nav = SafeNav.of(null);
        Object o2 = nav.orElse(o1);
        assertSame(o1, o2);
    }

    @Test
    public void testOrElseNullNull() throws Exception {
        SafeNav<Object> nav = SafeNav.of(null);
        Object o = nav.orElse(null);
        assertNull(o);
    }

}