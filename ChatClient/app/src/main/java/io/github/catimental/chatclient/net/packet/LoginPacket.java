package io.github.catimental.chatclient.net.packet;

import io.github.catimental.chatclient.net.ByteBufWriter;
import io.github.catimental.chatclient.net.packet.opcode.COutOpcode;

public class LoginPacket {
    public static byte[] LoginRequest(String accountId, String password) {
        ByteBufWriter w = new ByteBufWriter();
        w.writeOpcode(COutOpcode.LOGIN_REQUEST);
        writeIDPWInfo(w, accountId, password);
        return w.getPacket();
    }

    public static byte[] RegistrationRequest(String accountId, String password) {
        ByteBufWriter w = new ByteBufWriter();
        w.writeOpcode(COutOpcode.REGISTRATION_REQUEST);
        writeIDPWInfo(w, accountId, password);
        return w.getPacket();
    }

    private static void writeIDPWInfo(ByteBufWriter w, String accountId, String password) {
        w.writeAsciiString(accountId);
        w.writeAsciiString(password);
    }
}
