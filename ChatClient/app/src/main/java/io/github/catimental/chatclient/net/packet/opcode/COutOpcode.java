package io.github.catimental.chatclient.net.packet.opcode;

import java.util.Arrays;

public enum COutOpcode implements ShortValueHolder{
    NONE(0x00),
    HandShakeRequest(0x01),
    LOGIN_REQUEST(0x02),
    REGISTRATION_REQUEST(0x03),
    FriendsListRequest(0x04),
    ChatRoomRequest(0x05),
    SendChat(0x06),
    ;

    private short opcode;
    private COutOpcode(int opcode) {
        this.opcode = (short) opcode;
    }

    @Override
    public short getValue() {
        return opcode;
    }

    public static COutOpcode getOpcode(short value) {
        return Arrays.stream(COutOpcode.values())
                .filter(opcode -> opcode.getValue() == value)
                .findFirst()
                .orElse(NONE);
    }
}
