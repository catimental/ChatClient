package io.github.catimental.chatclient.net.handlers;

import io.github.catimental.chatclient.net.ByteBufReader;
import io.github.catimental.chatclient.net.packet.opcode.CInOpcode;

public interface PacketHandler {
    void handle(CInOpcode opcode, ByteBufReader b);
}
