package co.phoenixlab.common.lang.io;

import org.junit.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import static org.junit.Assert.*;

public class LittleEndianDataInputStreamTest {

    @Test
    public void testReadByte() throws Exception {
        byte b = 0x12;
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{b});
        DataInputStream dis = new DataInputStream(bais);
        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(dis);
        assertEquals(b, ledis.readByte());
    }

    @Test
    public void testReadUnsignedByte() throws Exception {
        int i = 200;
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{(byte) i});
        DataInputStream dis = new DataInputStream(bais);
        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(dis);
        assertEquals(i, ledis.readUnsignedByte());
    }

    @Test
    public void testReadShort() throws Exception {
        short i = 500;
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{(byte) (i & 0xFF), (byte) ((i >> 8) & 0xFF)});
        DataInputStream dis = new DataInputStream(bais);
        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(dis);
        assertEquals(i, ledis.readShort());
    }

    @Test
    public void testReadUnsignedShort() throws Exception {
        int i = 0xFF20;
        short c = (short) i;
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{(byte) (c & 0xFF), (byte) ((c >> 8) & 0xFF)});
        DataInputStream dis = new DataInputStream(bais);
        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(dis);
        assertEquals(i, ledis.readUnsignedShort());
    }

    @Test
    public void testReadChar() throws Exception {
        char c = 0xFF20;
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{(byte) (c & 0xFF), (byte) ((c >> 8) & 0xFF)});
        DataInputStream dis = new DataInputStream(bais);
        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(dis);
        assertEquals(c, ledis.readChar());
    }

    @Test
    public void testReadInt() throws Exception {
        int i = 0xDEADBEEF;
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{(byte) (i & 0xFF), (byte) ((i >> 8) & 0xFF),
                (byte) ((i >> 16) & 0xFF), (byte) ((i >> 24) & 0xFF)});
        DataInputStream dis = new DataInputStream(bais);
        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(dis);
        assertEquals(i, ledis.readInt());
    }

    @Test
    public void testReadUnsignedInt() throws Exception {
        int i = 0xFAFEBABE;
        long l = Integer.toUnsignedLong(i);
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{(byte) (i & 0xFF), (byte) ((i >> 8) & 0xFF),
                (byte) ((i >> 16) & 0xFF), (byte) ((i >> 24) & 0xFF)});
        DataInputStream dis = new DataInputStream(bais);
        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(dis);
        assertEquals(l, ledis.readUnsignedInt());
    }

    @Test
    public void testReadLong() throws Exception {
        long l = 0xCAFEBABEDEADBEEFL;
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{
                (byte) (l & 0xFF), (byte) ((l >> 8) & 0xFF),
                (byte) ((l >> 16) & 0xFF), (byte) ((l >> 24) & 0xFF),
                (byte) ((l >> 32) & 0xFF), (byte) ((l >> 40) & 0xFF),
                (byte) ((l >> 48) & 0xFF), (byte) ((l >> 56) & 0xFF)
        });
        DataInputStream dis = new DataInputStream(bais);
        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(dis);
        assertEquals(l, ledis.readLong());
    }

    @Test
    public void testReadFloat() throws Exception {

    }

    @Test
    public void testReadDouble() throws Exception {

    }
}
