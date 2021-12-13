package io.github.catimental.chatclient.activities;

import android.content.Intent;
import android.os.Bundle;

import io.github.catimental.chatclient.R;
import io.github.catimental.chatclient.client.Client;
import io.github.catimental.chatclient.net.ByteBufReader;
import io.github.catimental.chatclient.net.handlers.CommonHandler;
import io.github.catimental.chatclient.net.handlers.PacketHandler;
import io.github.catimental.chatclient.net.packet.opcode.CInOpcode;

public class LoadingActivity extends BaseActivity implements PacketHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        CommonHandler.registerHandler(this, new CInOpcode[]{CInOpcode.HandShakeResult});
        try {
            Client.getInstance().run();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void handle(CInOpcode opcode, ByteBufReader b) {

    }
}