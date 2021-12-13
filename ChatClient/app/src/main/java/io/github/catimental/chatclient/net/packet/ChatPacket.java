package io.github.catimental.chatclient.net.packet;

import io.github.catimental.chatclient.net.ByteBufWriter;
import io.github.catimental.chatclient.net.packet.opcode.COutOpcode;

public class ChatPacket {
    public static byte[] requestFriendList(int id) {
        ByteBufWriter b = new ByteBufWriter();
        b.writeOpcode(COutOpcode.FriendsListRequest);
        b.writeInt(id);
        return b.getPacket();
    }

    public static byte[] requestChatRoomInfo(int to, int from) {
        ByteBufWriter w = new ByteBufWriter();
        w.writeOpcode(COutOpcode.ChatRoomRequest);
        w.writeInt(to);
        w.writeInt(from);
        return w.getPacket();
    }

    public static byte[] sendChat(int toId, int fromId, String msg) {
        ByteBufWriter w = new ByteBufWriter();
        w.writeOpcode(COutOpcode.SendChat);
        w.writeInt(toId);
        w.writeInt(fromId);
        w.writeAsciiString(msg);
        return w.getPacket();
    }
}
