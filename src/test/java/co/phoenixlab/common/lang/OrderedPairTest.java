package co.phoenixlab.common.lang;

import org.junit.*;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class OrderedPairTest {

    private OrderedPair<Object, Object> pair;
    private OrderedPair<Object, Object> nullPair;
    private Object tObj;
    private Object uObj;

    @Before
    public void setUp() {
        tObj = new Object();
        uObj = new Object();
        pair = new OrderedPair<>(tObj, uObj);
        nullPair = new OrderedPair<>(null, null);
    }

    @Test
    public void testGetT() throws Exception {
        Object t = pair.getT();
        assertSame(tObj, t);
    }

    @Test
    public void testGetU() throws Exception {
        Object u = pair.getU();
        assertSame(uObj, u);
    }

    @Test
    public void testGetTNull() throws Exception {
        Object t = nullPair.getT();
        assertNull(t);
    }

    @Test
    public void testGetUNull() throws Exception {
        Object u = nullPair.getU();
        assertNull(u);
    }

    @Test
    public void testEquals() throws Exception {
        OrderedPair<Object, Object> op = new OrderedPair<>(tObj, uObj);
        assertEquals(pair, op);
    }

    @Test
    public void testEqualsOther() throws Exception {
        assertNotEquals(pair, new Object());
    }

    @Test
    public void testEqualsNull() throws Exception {
        OrderedPair<Object, Object> op = new OrderedPair<>(null, null);
        assertEquals(nullPair, op);
    }

    @Test
    public void testEqualsIdentity() throws Exception {
        assertEquals(pair, pair);
    }

    @Test
    public void testEqualsIdentityNull() throws Exception {
        assertEquals(nullPair, nullPair);
    }

    @Test
    public void testNotEqualsOrder() throws Exception {
        OrderedPair<Object, Object> op = new OrderedPair<>(uObj, tObj);
        assertNotEquals(pair, op);
    }

    @Test
    public void testNotEqualsNull() throws Exception {
        assertNotEquals(nullPair, pair);
    }

    @Test
    public void testPair() throws Exception {
        OrderedPair<Object, Object> op = OrderedPair.pair(tObj, uObj);
        assertSame(tObj, op.getT());
        assertSame(uObj, op.getU());
    }

    @Test
    public void testTwin() throws Exception {
        OrderedPair<Object, Object> op = OrderedPair.twin(tObj);
        assertSame(tObj, op.getT());
        assertSame(tObj, op.getU());
    }

    @Test
    public void testHashCode() throws Exception {
        OrderedPair<Object, Object> op = new OrderedPair<>(uObj, tObj);
        assertThat(op.hashCode(), is(anything()));
    }
}
