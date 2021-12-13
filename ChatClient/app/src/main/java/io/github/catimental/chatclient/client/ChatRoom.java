package io.github.catimental.chatclient.client;

import java.util.ArrayList;

import io.github.catimental.chatclient.net.ByteBufReader;

public class ChatRoom {
    private int to;
    private int from;
    private ArrayList<Chat> chats = new ArrayList<>();
    public ChatRoom(int to, int from) {
        this.to = to;
        this.from = from;
    }

}
