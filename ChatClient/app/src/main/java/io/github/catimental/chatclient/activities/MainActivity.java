package io.github.catimental.chatclient.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.github.catimental.chatclient.R;
import io.github.catimental.chatclient.fragments.FragmentChatRoom;
import io.github.catimental.chatclient.fragments.FragmentFriends;
import io.github.catimental.chatclient.fragments.FragmentInfo;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentFriends fragmentFriends = new FragmentFriends();
    private FragmentChatRoom fragmentChatRoom = new FragmentChatRoom();
    private FragmentInfo fragmentInfo = new FragmentInfo();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentFriends).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());


    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.friends:
                    transaction.replace(R.id.frameLayout, fragmentFriends).commitAllowingStateLoss();

                    break;
                case R.id.chatroom:
                    transaction.replace(R.id.frameLayout, fragmentChatRoom).commitAllowingStateLoss();
                    break;
                case R.id.info:
                    transaction.replace(R.id.frameLayout, fragmentInfo).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}

