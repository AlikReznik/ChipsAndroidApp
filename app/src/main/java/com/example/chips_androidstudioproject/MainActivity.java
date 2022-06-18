package com.example.chips_androidstudioproject;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chips_androidstudioproject.MainFragments.ChatsFragment;
import com.example.chips_androidstudioproject.MainFragments.ProfileFragment;
import com.example.chips_androidstudioproject.MainFragments.SearchFragment;
import com.example.chips_androidstudioproject.modules.DataFlowControl;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    ChatsFragment chatsFragment = new ChatsFragment();
    SearchFragment searchFragment = new SearchFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataFlowControl.activity = MainActivity.this;
        DataFlowControl.context = MainActivity.this;

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_chats:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, chatsFragment).commit();
                return true;

            case R.id.navigation_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, searchFragment).commit();
                return true;

            case R.id.navigation_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                return true;
        }
        return false;
    }
}