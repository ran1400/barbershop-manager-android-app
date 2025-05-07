package com.example.barbershopmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;


import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.Manifest;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.barbershopmanager.fragments.AddQueuesFragment;
import com.example.barbershopmanager.fragments.DeleteQueuesFragment;
import com.example.barbershopmanager.fragments.QueuesFragment;
import com.example.barbershopmanager.fragments.SettingFragment;
import com.example.barbershopmanager.server.Main;
import com.example.barbershopmanager.sharedDate.AddQueuesData;
import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.server.ServerRequest;
import com.example.barbershopmanager.sharedDate.DeleteQueuesData;
import com.example.barbershopmanager.sharedDate.QueuesData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.google.firebase.messaging.FirebaseMessaging;




public class MainActivity extends AppCompatActivity
{

    private ConstraintLayout loadingWindow;
    private TextView loadingWindowText;
    private Button loadingWindowBtn;
    private ProgressBar loadingView;
    private BottomNavigationView navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedData.mainActivity = this;
        SharedData.mainActivity.changeActivityTitle(getResources().getString(R.string.app_name));
        SharedData.crntWindow = null;
        loadingWindow = findViewById(R.id.loadingWindow);
        loadingWindowBtn = findViewById(R.id.loadingViewButton);
        loadingWindowText = findViewById(R.id.loadingViewText);
        loadingView = findViewById(R.id.activityMainLoadingView);
        navigationBar = findViewById(R.id.navigationBar);
        navigationBar.setOnItemSelectedListener(getNavigationBarListener());
        loadingView.setVisibility(View.VISIBLE);
        QueuesData.refreshQueues(); // for notifications click
        if (SharedData.getFromMemory("firstEnter",true))
            firstEnter();
        ServerRequest serverRequest = new ServerRequest( (String response) -> Main.checkIfUserCmdEnabledAns(response) );
        serverRequest.checkIfUserCmdEnabled();

    }

    private void firstEnter()
    {
        createNotificationChannels();
        FirebaseMessaging.getInstance().subscribeToTopic("userUpdates");
        FirebaseMessaging.getInstance().subscribeToTopic("managerTests");
        SharedData.writeToMemory("firstEnter",false);
    }

    private void createNotificationChannels()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel;
        channel = new NotificationChannel("userAddQueue", "קביעת תורים", NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);
        channel = new NotificationChannel("userDeleteQueue", "ביטול תורים", NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);
        channel = new NotificationChannel("userUpdateQueue", "עדכון תורים", NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);
        channel = new NotificationChannel("testMsg", "הודעת ניסיון", NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);
        channel = new NotificationChannel("testMsgQuiet", "הודעת ניסיון שקטה", NotificationManager.IMPORTANCE_LOW);
        manager.createNotificationChannel(channel);
    }

    public void changeActivityTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }

    public void thereIsInternet() // called from checkIfUserCmdEnabledAns
    {
        navigationBar.setSelectedItemId(R.id.navigation_queues);
        setLoadingWindowGone();
    }

    public void showQueuesFragment()
    {
        if (SharedData.queuesFragment == null)
            SharedData.queuesFragment = new QueuesFragment();
        else if (SharedData.isQueuesCrntWindows())
            SharedData.queuesFragment.setReservedQueuesRadioBtnChecked();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragment,SharedData.queuesFragment).commit();
        SharedData.setQueuesCrntWindow();
    }

    private void showAddQueuesFragment()
    {
        if (SharedData.addQueuesFragment == null)
            SharedData.addQueuesFragment = new AddQueuesFragment();
        else if (SharedData.isAddQueuesCrntWindows() && AddQueuesData.addQueueWindowsIsVisible())
            SharedData.addQueuesFragment.switchToAddQueuesWindow();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragment,SharedData.addQueuesFragment).commit();
        SharedData.setAddQueuesCrntWindow();
        changeActivityTitle("הוספת תורים");
    }

    private void showDeleteQueuesFragment()
    {
        if (SharedData.deleteQueuesFragment == null)
            SharedData.deleteQueuesFragment = new DeleteQueuesFragment();
        else if ( SharedData.isDeleteQueuesCrntWindows() && DeleteQueuesData.deleteQueueWindowsIsVisible())
                SharedData.deleteQueuesFragment.switchToDeleteQueuesWindow();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragment,SharedData.deleteQueuesFragment).commit();
        SharedData.setDeleteQueuesCrntWindow();
        changeActivityTitle("מחיקת תורים");
    }

    private void showSettingFragment()
    {
        if (SharedData.settingFragment == null)
            SharedData.settingFragment = new SettingFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragment,SharedData.settingFragment).commit();
        SharedData.setSettingQueuesCrntWindow();
        changeActivityTitle("הגדרות");
    }

    private NavigationBarView.OnItemSelectedListener getNavigationBarListener()
    {
        return new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem item)
            {
                item.setChecked(true);
                switch(item.getItemId())
                {
                    case R.id.navigation_queues:
                        showQueuesFragment();
                        break;
                    case R.id.navigation_add_queues:
                        showAddQueuesFragment();
                        break;
                    case R.id.navigation_delete_queues:
                        showDeleteQueuesFragment();
                        break;
                    case R.id.navigation_setting:
                        showSettingFragment();
                        break;
                }
                return false;
            }
        };
    }


    public void setLoadingWindowGone()
    {
        loadingWindow.setVisibility(View.GONE);
    }

    public void showLoadingWindowContent()
    {
        loadingWindowBtn.setVisibility(View.VISIBLE);
        loadingWindowText.setVisibility(View.VISIBLE);
        loadingWindow.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
    }

    public void loadingWindowTryAgainBtn(View view)
    {
        loadingWindowBtn.setVisibility(View.INVISIBLE);
        loadingWindowText.setVisibility(View.INVISIBLE);
        loadingView.setVisibility(View.VISIBLE);
        ServerRequest serverRequest = new ServerRequest( (String response) ->Main.checkIfUserCmdEnabledAns(response));
        serverRequest.checkIfUserCmdEnabled();
    }


}