package io.github.catimental.chatclient.net;


import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.github.catimental.chatclient.net.packet.opcode.ShortValueHolder;


//Little Endian
public class ByteBufWriter {
    private static final Charset ASCII = StandardCharsets.US_ASCII;
    private ByteArrayOutputStream baos;

    public ByteBufWriter() {
        this(32);
    }

    public ByteBufWriter(int size) {
        baos = new ByteArrayOutputStream(size);
    }

    public void writeBoolean(boolean value) {
        baos.write(value ? 1 : 0);
    }
    public void write(byte b) {
        baos.write(b);
    }

    public void write(byte[] bs) {
        for (byte b : bs) {
            write(b);
        }
    }

    public void writeLong(long l) {
        baos.write((byte) (l & 0xFF));
        baos.write((byte) (l >>> 8 & 0xFF));
        baos.write((byte) (l >>> 16 & 0xFF));
        baos.write((byte) (l >>> 24 & 0xFF));
        baos.write((byte) (l >>> 32 & 0xFF));
        baos.write((byte) (l >>> 40 & 0xFF));
        baos.write((byte) (l >>> 48 & 0xFF));
        baos.write((byte) (l >>> 56 & 0xFF));
    }

    public void writeInt(int value) {
        baos.write((byte) (value & 0xFF));
        baos.write((byte) (value >>> 8 & 0xFF));
        baos.write((byte) (value >>> 16 & 0xFF));
        baos.write((byte) (value >>> 24 & 0xFF));
    }

    public void writeShort(short s) {
        baos.write((byte) (s & 0xFF));
        baos.write((byte) (s >>> 8 & 0xFF));
    }

    public void writeAsciiString(String s) {
        writeShort((short)s.length());
        write(s.getBytes(ASCII));
    }

    public void writeOpcode(ShortValueHolder s) {
        writeShort(s.getValue());
    }

    public byte[] getPacket() {
        return baos.toByteArray();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (byte data : getPacket()) {
            builder.append(String.format("%02X ", data));
        }
        return builder.toString();
    }
}
