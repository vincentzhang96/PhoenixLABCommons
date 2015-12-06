package co.phoenixlab.common.lang.io;

import java.io.*;

/**
 * A little-endian version of Java's DataInputStream (which is big-endian)
 */
public class LittleEndianDataInputStream extends FilterInputStream implements DataInput {

    private final DataInputStream parent;

    /**
     * Wraps an existing DataInputStream for reading as little-endian
     * @param in The DataInputStream to wrap
     */
    public LittleEndianDataInputStream(DataInputStream in) {
        super(in);
        parent = in;
    }

    /**
     * Wraps an existing InputStream for reading as a little-endian DataInputStream
     * @param in The InputStream to wrap
     */
    public LittleEndianDataInputStream(InputStream in) {
        this(new DataInputStream(in));
    }

    @Override
    public void readFully(byte[] b) throws IOException {
        parent.readFully(b);
    }

    @Override
    public void readFully(byte[] b, int off, int len) throws IOException {
        parent.readFully(b, off, len);
    }

    @Override
    public int skipBytes(int n) throws IOException {
        return parent.skipBytes(n);
    }

    @Override
    public boolean readBoolean() throws IOException {
        return parent.readBoolean();
    }

    @Override
    public byte readByte() throws IOException {
        return parent.readByte();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return parent.readUnsignedByte();
    }

    @Override
    public short readShort() throws IOException {
        return Short.reverseBytes(parent.readShort());
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return Short.toUnsignedInt(readShort());
    }

    @Override
    public char readChar() throws IOException {
        return Character.reverseBytes(parent.readChar());
    }

    @Override
    public int readInt() throws IOException {
        return Integer.reverseBytes(parent.readInt());
    }

    public long readUnsignedInt() throws IOException {
        return Integer.toUnsignedLong(readInt());
    }

    @Override
    public long readLong() throws IOException {
        return Long.reverseBytes(parent.readLong());
    }

    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public String readLine() throws IOException {
        return parent.readLine();
    }

    @Override
    public String readUTF() throws IOException {
        return parent.readUTF();
    }
}
