package io.github.catimental.chatclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import io.github.catimental.chatclient.R;
import io.github.catimental.chatclient.client.Client;
import io.github.catimental.chatclient.client.Profile;
import io.github.catimental.chatclient.net.ByteBufReader;
import io.github.catimental.chatclient.net.handlers.CommonHandler;
import io.github.catimental.chatclient.net.handlers.PacketHandler;
import io.github.catimental.chatclient.net.packet.LoginPacket;
import io.github.catimental.chatclient.net.packet.opcode.CInOpcode;

public class LoginActivity extends BaseActivity implements PacketHandler {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginBtn = (Button) findViewById(R.id.btn_login);
        Button registrationBtn = (Button) findViewById(R.id.btn_registration);
        EditText id = (EditText) findViewById(R.id.login_id);
        EditText pw = (EditText) findViewById(R.id.login_pw);
        Client client = Client.getInstance();

        loginBtn.setOnClickListener((i) -> {
            try {
                client.writeAndFlush(LoginPacket.LoginRequest(id.getText().toString(), pw.getText().toString()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        registrationBtn.setOnClickListener((i) -> {
            try {
                client.writeAndFlush(LoginPacket.RegistrationRequest(id.getText().toString(), pw.getText().toString()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        CommonHandler.registerHandler(this, new CInOpcode[]{CInOpcode.LoginResult, CInOpcode.RegistrationResult});

    }


    public void handle(CInOpcode opcode, ByteBufReader b) {
//        Activity activity = MainApplication.instance.getCurrentActivity();

        switch(opcode) {
            case LoginResult:
                Client client = Client.getInstance();
                boolean isValidLogin = b.readBoolean();
                String accountId = b.readAsciiString();
                Profile profile = Profile.decode(b);
                if(isValidLogin) {
                    client.setAccountId(accountId);
                    client.setProfile(profile);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case RegistrationResult:
                boolean isSuccess = b.readBoolean();
                System.out.println("hello world2");
//                //if(isSuccess) {
//                    Handler handler = new Handler(Looper.getMainLooper());
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run()
//                        {
//                            Toast.makeText(LoginActivity.this, "Sender : 서버와 연결이 끊어졌습니다", Toast.LENGTH_SHORT).show();
//                        }
//                    }, 0);
//
//                //}
                break;
        }
    }
}
