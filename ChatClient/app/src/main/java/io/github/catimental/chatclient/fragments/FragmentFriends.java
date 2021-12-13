package io.github.catimental.chatclient.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import io.github.catimental.chatclient.R;
import io.github.catimental.chatclient.activities.ChatActivity;
import io.github.catimental.chatclient.client.Client;
import io.github.catimental.chatclient.client.Profile;
import io.github.catimental.chatclient.net.ByteBufReader;
import io.github.catimental.chatclient.net.handlers.CommonHandler;
import io.github.catimental.chatclient.net.handlers.PacketHandler;
import io.github.catimental.chatclient.net.packet.ChatPacket;
import io.github.catimental.chatclient.net.packet.opcode.CInOpcode;

public class FragmentFriends extends Fragment implements PacketHandler {
    //private ArrayList<Profile> friends = new ArrayList<>();
    private ListView lv;
    private KakaoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Client client = Client.getInstance();
        View view = inflater.inflate(R.layout.fragment_friends, container, false);


        adapter = new KakaoAdapter(
                inflater.getContext().getApplicationContext(), // 현재 화면의 제어권자
                R.layout.friends_row,             // 한행을 그려줄 layout
                client.getFriends());                     // 다량의 데이터

        lv = (ListView)view.findViewById(R.id.friendsListView);
        try {
            client.writeAndFlush(
                    ChatPacket.requestFriendList(client.getUid())
            );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lv.setOnItemClickListener((adapterView, view1, position, id) -> {

            Profile profile = (Profile) adapter.getItem(position);
            System.out.println(profile.getDisplayName());
            Intent intent = new Intent(inflater.getContext(), ChatActivity.class);
            intent.putExtra("to", profile.getId());
            startActivity(intent);
        });
        CommonHandler.registerHandler(this, new CInOpcode[]{CInOpcode.FriendsListResult}); //핸들러 레지스트레이션을 클래스 컨스트럭터에서 써도 좋을듯함
        return view;
    }

    @Override
    public void handle(CInOpcode opcode, ByteBufReader b) {
        final Client client = Client.getInstance();
        switch(opcode) {
            case FriendsListResult:
                client.clearFriends();
                int friendCount = b.readInt();
                for(int i = 0; i<friendCount; i++) {
                    client.addFriend(Profile.decode(b));
                }
                updateFriendList();
                break;
        }
    }

    public void updateFriendList() {
        if(lv != null && adapter != null) {
            lv.setAdapter(adapter);
        }
    }
}


class KakaoAdapter extends BaseAdapter {
    Context context;     // 현재 화면의 제어권자
    int layout;              // 한행을 그려줄 layout
    ArrayList<Profile> al;     // 다량의 데이터
    LayoutInflater inf; // 화면을 그려줄 때 필요
    public KakaoAdapter(Context context, int layout, ArrayList<Profile> al) {
        this.context = context;
        this.layout = layout;
        this.al = al;
        this.inf = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItem(ArrayList<Profile> al) {
        this.al = al;
    }

    @Override
    public int getCount() { // 총 데이터의 개수를 리턴
        return al.size();
    }
    @Override
    public Object getItem(int position) { // 해당번째의 데이터 값
        return al.get(position);
    }
    @Override
    public long getItemId(int position) { // 해당번째의 고유한 id 값
        return position;
    }
    @Override // 해당번째의 행에 내용을 셋팅(데이터와 레이아웃의 연결관계 정의)
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inf.inflate(layout, null);

        ImageView iv = (ImageView)convertView.findViewById(R.id.profileImage);
        TextView tvName=(TextView)convertView.findViewById(R.id.name);
        TextView tvDescription =(TextView)convertView.findViewById(R.id.description);

        Profile m = al.get(position);

        //iv.setImageResource(m.img);
        tvName.setText(m.getDisplayName());
        tvDescription.setText(m.getDescription());

        return convertView;
    }
}
