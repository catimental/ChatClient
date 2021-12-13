package io.github.catimental.chatclient.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import io.github.catimental.chatclient.R;
import io.github.catimental.chatclient.client.Chat;
import io.github.catimental.chatclient.client.Client;
import io.github.catimental.chatclient.client.Profile;
import io.github.catimental.chatclient.net.ByteBufReader;
import io.github.catimental.chatclient.net.handlers.CommonHandler;
import io.github.catimental.chatclient.net.handlers.PacketHandler;
import io.github.catimental.chatclient.net.packet.ChatPacket;
import io.github.catimental.chatclient.net.packet.opcode.CInOpcode;

public class ChatActivity extends AppCompatActivity implements PacketHandler {
    ArrayList<Chat> chats = new ArrayList<>();
    Profile toProfile;
    private ListView lv;
    private ChatAdapter adapter;

    final Handler refreshHandler = new Handler(){
        public void handleMessage(Message msg){
            adapter.refresh();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        final Client client = Client.getInstance();
        super.onCreate(savedInstanceState);
        toProfile = client.getFriend(getIntent().getIntExtra("to", -1));

        setContentView(R.layout.activity_chat);
        adapter = new ChatAdapter(getApplicationContext(), R.layout.chat_rows, chats);
        lv = ((ListView) findViewById(R.id.lvChatting));
        lv.setAdapter(adapter);


        setTitle(toProfile.getDisplayName());

        CommonHandler.registerHandler(this, new CInOpcode[]{CInOpcode.ChatRoomResult, CInOpcode.SendChat});
        try {
            client.writeAndFlush(ChatPacket.requestChatRoomInfo(toProfile.getId(), client.getUid()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        findViewById(R.id.buttonSend).setOnClickListener((i) -> {
            EditText editText = (EditText) findViewById(R.id.editTextSend);
            String msg = editText.getText().toString();
            if(!msg.isEmpty()) {
                editText.setText("");
                try {
                    client.writeAndFlush(ChatPacket.sendChat(toProfile.getId(), client.getUid(), msg));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void handle(CInOpcode opcode, ByteBufReader b) {
        switch(opcode) {
            case ChatRoomResult: {
                int toId = b.readInt();
                int fromId = b.readInt();
                int messageCount = b.readInt();
                ArrayList<Chat> _chats = new ArrayList<>();
                for(int i = 0; i < messageCount; i++) {
                    _chats.add(Chat.decode(b));
                }
                chats.clear();
                chats.addAll(_chats);
            }
            break;
            case SendChat: {
                int toId = b.readInt();
                int fromId = b.readInt();
                String msg = b.readAsciiString();
                chats.add(new Chat(fromId, System.currentTimeMillis(), msg)); // 시간은 임시처리합니다

                Message _msg = refreshHandler.obtainMessage();
                refreshHandler.sendMessage(_msg);
            }
            break;
        }
    }
}
class ChatAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Chat> chatData;
    private LayoutInflater inflater;


    public ChatAdapter(Context applicationContext, int talklist, ArrayList<Chat> list) {
        this.context = applicationContext;
        this.layout = talklist;
        this.chatData = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { // 전체 데이터 개수
        return chatData.size();
    }

    @Override
    public Object getItem(int position) { // position번째 아이템
        return chatData.get(position);
    }

    @Override
    public long getItemId(int position) { // position번째 항목의 id인데 보통 position
        return position;
    }

    public void refresh(){
        //setNotifyOnChange(true);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //항목의 index, 전에 inflate 되어있는 view, listView

//첫항목을 그릴때만 inflate 함 다음거부터는 매개변수로 넘겨줌 (느리기때문) : recycle이라고 함
        ViewHolder holder;

        if(convertView == null){
//어떤 레이아웃을 만들어 줄 것인지, 속할 컨테이너, 자식뷰가 될 것인지
            convertView = inflater.inflate(layout, parent, false); //아이디를 가지고 view를 만든다
            holder = new ViewHolder();
            holder.img= (ImageView)convertView.findViewById(R.id.iv_profile);
            holder.tv_msg = (TextView)convertView.findViewById(R.id.tv_content);
            holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
            holder.my_msg = (TextView)convertView.findViewById(R.id.my_msg);
            holder.my_time = (TextView)convertView.findViewById(R.id.my_time);

            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        final Client client = Client.getInstance();
        final int uid = client.getUid();

//누군지 판별
        if(chatData.get(position).getFrom() == uid){
            holder.tv_time.setVisibility(View.GONE);
            holder.tv_name.setVisibility(View.GONE);
            holder.tv_msg.setVisibility(View.GONE);
            holder.img.setVisibility(View.GONE);

            holder.my_msg.setVisibility(View.VISIBLE);
            holder.my_time.setVisibility(View.VISIBLE);

            //holder.my_time.setText(String.valueOf(chatData.get(position).getTime()));
            holder.my_msg.setText(chatData.get(position).getMsg());
        }else{
            final Profile fromProfile = client.getFriend(chatData.get(position).getFrom());
            holder.tv_time.setVisibility(View.VISIBLE);
            holder.tv_name.setVisibility(View.VISIBLE);
            holder.tv_msg.setVisibility(View.VISIBLE);
            holder.img.setVisibility(View.VISIBLE);

            holder.my_msg.setVisibility(View.GONE);
            holder.my_time.setVisibility(View.GONE);

            //holder.img.setImageResource(chatData.get(position).getImageID()); // 해당 사람의 프사 가져옴
            holder.tv_msg.setText(chatData.get(position).getMsg());
            //holder.tv_time.setText(String.valueOf(chatData.get(position).getTime()));
            holder.tv_name.setText(fromProfile.getDisplayName());
        }
        return convertView;
    }

    //뷰홀더패턴
    public class ViewHolder{
        ImageView img;
        TextView tv_msg;
        TextView tv_time;
        TextView tv_name;
        TextView my_time;
        TextView my_msg;
    }

}
