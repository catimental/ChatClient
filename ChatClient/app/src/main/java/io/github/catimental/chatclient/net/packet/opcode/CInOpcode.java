package io.github.catimental.chatclient.net.packet.opcode;

import java.util.Arrays;

public enum CInOpcode implements ShortValueHolder{
    NONE(0x00),
    HandShakeResult(0x01),
    LoginResult(0x02),
    RegistrationResult(0x03),
    FriendsListResult(0x04),
    ChatRoomResult(0x05),
    SendChat(0x06)
    ;

    private short opcode;

    private CInOpcode(int opcode) {
        this.opcode = (short) opcode;
    }

    @Override
    public short getValue() {
        return opcode;
    }

    public static CInOpcode getOpcode(short value) {
        return Arrays.stream(CInOpcode.values())
                .filter(opcode -> opcode.getValue() == value)
                .findFirst()
                .orElse(NONE);
    }
}