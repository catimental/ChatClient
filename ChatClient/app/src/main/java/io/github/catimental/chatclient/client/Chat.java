package io.github.catimental.chatclient.client;

import io.github.catimental.chatclient.net.ByteBufReader;
import io.github.catimental.chatclient.net.ByteBufWriter;

public class Chat {
    private int from;
    private long time;
    private String msg;

    public Chat(int from, long time, String msg) {
        this.from = from;
        this.time = time;
        this.msg = msg;
    }

    public void encode(ByteBufWriter w) {
        w.writeInt(from);
        w.writeLong(time);
        w.writeAsciiString(msg);
    }

    public static Chat decode(ByteBufReader r) {
        return new Chat(
            r.readInt(),
            r.readLong(),
            r.readAsciiString()
        );
    }

    public int getFrom() {
        return from;
    }

    public String getMsg() {
        return msg;
    }

    public long getTime() {
        return time;
    }
}
