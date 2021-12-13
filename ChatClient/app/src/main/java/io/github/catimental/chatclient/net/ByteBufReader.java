package io.github.catimental.chatclient.net;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;

public class ByteBufReader {
    private final ByteBuf wrapped;

    public ByteBufReader(ByteBuf wrapped) {
        this.wrapped = wrapped;
    }

    public boolean readBoolean() {
        return readByte() == 1 ? true : false;
    }

    public byte readByte() {
        return wrapped.readByte();
    }

    public char readChar() {
        return wrapped.readChar();
    }

    public short readShort() {
        return wrapped.readShort();
    }

    public int readInt() {
        return wrapped.readInt();
    }

    public long readLong() {
        return wrapped.readLong();
    }

    public float readFloat() {
        return wrapped.readFloat();
    }

    public double readDouble() {
        return wrapped.readDouble();
    }

    public String readAsciiString(int n) {
        char[] string = new char[n];
        for (int x = 0; x < n; ++x) {
            string[x] = (char) readByte();
        }
        return String.valueOf(string);
    }

    public String readAsciiString() {
        return readAsciiString(readShort());
    }

    public byte[] read(int num) {
        byte[] readed = new byte[num];
        wrapped.readBytes(readed);
        return readed;
    }

    public long available() {
        return wrapped.readableBytes();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (byte data : wrapped.array()) {
            builder.append(String.format("%02X ", data));
        }
        return builder.toString();
    }

}
