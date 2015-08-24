package co.phoenixlab.common.lang.io;

import org.junit.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import static org.junit.Assert.*;

public class LittleEndianDataInputStreamTest {

    @Test
    public void testReadByte() throws Exception {
        byte b = (byte) 0x12;
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {b});
        DataInputStream dis = new DataInputStream(bais);
        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(dis);
        assertEquals(b, ledis.readByte());
    }

    @Test
    public void testReadUnsignedByte() throws Exception {
        int i = 200;
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {(byte) i});
        DataInputStream dis = new DataInputStream(bais);
        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(dis);
        assertEquals(i, ledis.readUnsignedByte());
    }

    @Test
    public void testReadShort() throws Exception {

    }

    @Test
    public void testReadUnsignedShort() throws Exception {

    }

    @Test
    public void testReadChar() throws Exception {

    }

    @Test
    public void testReadInt() throws Exception {

    }

    @Test
    public void testReadUnsignedInt() throws Exception {

    }

    @Test
    public void testReadLong() throws Exception {

    }

    @Test
    public void testReadFloat() throws Exception {

    }

    @Test
    public void testReadDouble() throws Exception {

    }
}
