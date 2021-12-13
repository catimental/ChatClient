package io.github.catimental.chatclient.net.handlers;

import java.util.HashMap;

import io.github.catimental.chatclient.net.ByteBufReader;
import io.github.catimental.chatclient.net.packet.opcode.CInOpcode;

public class CommonHandler {
    private static HashMap<CInOpcode, PacketHandler> handlers = new HashMap<>();
    static {
        int maxOpcode = CInOpcode.NONE.getValue();
        for (CInOpcode opcode : CInOpcode.values()) {
            if(opcode.getValue() > maxOpcode) {
                maxOpcode = opcode.getValue();
            }
        }
    }

    public static void registerHandler(PacketHandler packetHandler, CInOpcode[] opcodes) {
        for (CInOpcode opcode : opcodes) {
            handlers.put(opcode, packetHandler);
        }
    }


    public static void handle(CInOpcode opcode, ByteBufReader b) throws InterruptedException {
//        android.os.Handler handler = new LoginActivityHandler();
//        Message message = handler.obtainMessage();
//        message.what = opcode.getValue();

//        message.obj = (Object) b;
//        handler.sendMessage(message);
        handlers.get(opcode).handle(opcode, b);
//        PacketHandler packetHandler = packetHandlers[opcode.getValue()];
//        Activity activity = activityInstances.entrySet().stream() //야매처리
//                .filter(activityMap -> activityMap.getValue().indexOf(opcode) != -1)
//                .findFirst().get().getKey();
//        System.out.println(packetHandler.getClass());
//        packetHandler.handle(activity, b);
//        switch(opcode) {
//            case HandShakeResult:
//                //setContentView(R.layout.activity_main);
//                break;
//            case LoginResult:
//            case RegistrationResult:
//                //LoginActivity.handle(opcode, b);
//                break;
//        }
    }

}
