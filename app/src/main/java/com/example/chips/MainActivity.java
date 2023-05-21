package com.example.chips;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.chips.mainFragments.ChatsFragment;
import com.example.chips.mainFragments.ProfileFragment;
import com.example.chips.mainFragments.SearchFragment;
import com.example.chips.utils.DataFlowControl;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    /*
    *   Main activity
    * */
    /*
     *   Layout variables
     * */
    private static BottomNavigationView bottomNavigationView;

    /*
     *   Activity variables
     * */
    /*
    *   Menu variables
    * */
    private ChatsFragment chatsFragment;
    private SearchFragment searchFragment;
    private ProfileFragment profileFragment;

    /*
     *   Permissions variables
     * */
    private ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private boolean isReadPermissionGranted = false;
    private boolean isNotificationPermissionGranted = false;
    private boolean isSendPermissionGranted = false;


    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onStart() {
        super.onStart();

        initLayout();
        initInfo();
    }

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    /*
     *   requestPermission:
     *      Checks if permissions are granted, if not asks for them
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void requestPermission(){
        isReadPermissionGranted = ContextCompat.checkSelfPermission(
                DataFlowControl.context,
                Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED;

        isNotificationPermissionGranted = ContextCompat.checkSelfPermission(
                DataFlowControl.context,
                Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED;

        isSendPermissionGranted = ContextCompat.checkSelfPermission(
                DataFlowControl.context,
                Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED;

        List<String> permissionRequest = new ArrayList<String>();

        if (!isReadPermissionGranted){

            permissionRequest.add(Manifest.permission.READ_CONTACTS);

        }

        if (!isNotificationPermissionGranted){

            permissionRequest.add(Manifest.permission.POST_NOTIFICATIONS);

        }

        if (!isSendPermissionGranted){

            permissionRequest.add(Manifest.permission.SEND_SMS);

        }

        if (!permissionRequest.isEmpty()){

            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));

        }


    }

    /*
     *   startPermissionDialog:
     *      Opens dialog if not all the permissions are granted
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void startPermissionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DataFlowControl.context);
        builder.setTitle("Permission denied");
        builder.setMessage("App cannot run with out permissions, open settings and grant the permissions");
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    /*
     *   initInfo:
     *      Basic information & checks if all permissions are granted
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void initInfo() {
        DataFlowControl.activity = MainActivity.this;
        DataFlowControl.context = MainActivity.this;

        /*
         *   Checks if user agreed to permissions
         * */
        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {

                if (result.get(Manifest.permission.POST_NOTIFICATIONS) != null){

                    isNotificationPermissionGranted = result.get(Manifest.permission.POST_NOTIFICATIONS);

                }else{
                    startPermissionDialog();
                }

                if (result.get(Manifest.permission.READ_CONTACTS) != null){

                    isReadPermissionGranted = result.get(Manifest.permission.READ_CONTACTS);

                }else{
                    startPermissionDialog();
                }

                if (result.get(Manifest.permission.SEND_SMS) != null){

                    isSendPermissionGranted = result.get(Manifest.permission.SEND_SMS);

                }else{
                    startPermissionDialog();
                }

            }
        });

        requestPermission();
    }

    /*
     *   initLayout:
     *      Sets layout variables
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    private void initLayout() {
        chatsFragment = new ChatsFragment();
        searchFragment = new SearchFragment();
        profileFragment = new ProfileFragment();

        MainActivity.bottomNavigationView = findViewById(R.id.nav_view);
        MainActivity.bottomNavigationView
                .setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                      @Override
                      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                          switch (menuItem.getItemId()) {
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
                  });
        MainActivity.bottomNavigationView.setSelectedItemId(R.id.navigation_chats);
    }
}