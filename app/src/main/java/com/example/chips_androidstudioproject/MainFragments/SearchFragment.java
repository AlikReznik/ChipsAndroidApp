package com.example.chips_androidstudioproject.MainFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.chips_androidstudioproject.R;
import com.example.chips_androidstudioproject.modules.DataFlowControl;
import com.example.chips_androidstudioproject.modules.User.UserAdapter;
import com.example.chips_androidstudioproject.modules.User.BaseUser;
import com.example.chips_androidstudioproject.modules.User.UserFirestore;
import com.example.chips_androidstudioproject.userProfile_activity;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    private Button btn_search;
    private EditText txt_search;

    public static List<BaseUser> users;
    private ListView lv_data;
    private static UserAdapter userAdapter;

    public SearchFragment() {
        super(R.layout.fragment_search);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initLayout();
        initInfo();
        initButtonListeners();
    }
    public static void updateListViewProfileUserAdapter() {
        userAdapter.notifyDataSetChanged();
    }
    private void initInfo() {
        users = new ArrayList<BaseUser>();

        userAdapter = new UserAdapter(users);
        lv_data.setAdapter(userAdapter);
    }

    private void initButtonListeners() {
        btn_search.setOnClickListener(view ->
        {
            DataFlowControl.loadingDialog.startLoadingDialog();
            if(txt_search.getText().toString().equals("")){
                UserFirestore.getUsersProfiles();
            }

            if(!txt_search.getText().toString().equals("")){
                UserFirestore.getUserProfileByUsername(txt_search.getText().toString());
            }
        });

        lv_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView <?> adapter, View view, int position, long arg){
                Intent intent = new Intent(DataFlowControl.context, userProfile_activity.class);
                intent.putExtra("UserId", position);
                startActivity(intent);
            }
        });
    }

    private void initLayout(){
        btn_search = getActivity().findViewById(R.id.btn_search);
        lv_data = getActivity().findViewById(R.id.lv_data);
        txt_search = getActivity().findViewById(R.id.txt_search);
    }
}