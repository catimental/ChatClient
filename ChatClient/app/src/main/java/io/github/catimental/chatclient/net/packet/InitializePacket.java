package io.github.catimental.chatclient.net.packet;


import io.github.catimental.chatclient.net.ByteBufWriter;
import io.github.catimental.chatclient.net.packet.opcode.CInOpcode;

public class InitializePacket {
    public static byte[] HandshakeRequest() {
        ByteBufWriter bw = new ByteBufWriter();
        bw.writeOpcode(CInOpcode.HandShakeResult);
        bw.writeAsciiString("reulst packet");
        return bw.getPacket();
    }
}
