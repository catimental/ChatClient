package io.github.catimental.chatclient.client;

import io.github.catimental.chatclient.constants.ServerConstants;
import io.github.catimental.chatclient.net.packet.InitializePacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;

public class Client {

    private static Client instance;
    private Channel channel;
    private String accountId;
    private Profile profile;
    private HashMap<Integer, ChatRoom> chatRoomHap = new HashMap<>();
    private ArrayList<Profile> friends = new ArrayList<>();

    private Client() {

    }

    static {
        try {
            instance = new Client();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new Initializer());
            this.channel = bootstrap.connect(ServerConstants.HOST, ServerConstants.PORT).sync().channel();
            writeAndFlush(InitializePacket.HandshakeRequest());
        } finally {
            //group.shutdownGracefully();
        }

    }

    //todo ChannelFuture
    public void writeAndFlush(byte[] packet) throws InterruptedException {
        ByteBuf writeBuf = Unpooled.wrappedBuffer(packet).order(ByteOrder.LITTLE_ENDIAN);
        ChannelFuture lastWriteFuture = channel.writeAndFlush(
                writeBuf);
        if(lastWriteFuture != null) {
            lastWriteFuture.sync();
        }
    }

    public void close() throws InterruptedException {
        channel.closeFuture().sync();
    }

    public static Client getInstance() {
        return instance;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getUid() {
        return profile.getId();
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    public ChatRoom getChatRoom(int id) {
        if(!chatRoomHap.containsKey(id)) {
            chatRoomHap.put(id, new ChatRoom(id, getUid()));
        }
        return chatRoomHap.get(id);
    }

    public void clearFriends() {
        this.clearFriends(true);
    }

    public void clearFriends(boolean isAppendMySelf) {
        friends.clear();;
        if(isAppendMySelf) {
            friends.add(new Profile(getUid(), getProfile().getDisplayName(), getProfile().getDescription()));
        }
    }

    public void addFriend(Profile friend) {
        friends.add(friend);
    }

    public ArrayList<Profile> getFriends() {
        return friends;
    }

    public Profile getFriend(int id) {
        return friends.stream().filter(friend -> friend.getId() == id)
                .findFirst().get();
    }
}
