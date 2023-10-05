package com.example.babershopmanager.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import androidx.annotation.NonNull;
import android.app.Fragment;
import com.example.babershopmanager.R;
import com.example.babershopmanager.fragments.dialogFragments.SimpleMethod;
import com.example.babershopmanager.fragments.SettingFragments.NotificationsFragment;
import com.example.babershopmanager.fragments.SettingFragments.SendMsgFragment;
import com.example.babershopmanager.fragments.SettingFragments.ShowUsersFragment;
import com.example.babershopmanager.fragments.SettingFragments.UserOptionsFragment;
import com.example.babershopmanager.fragments.dialogFragments.AlertDialog;
import com.example.babershopmanager.server.ServerRequest;
import com.example.babershopmanager.server.Setting;
import com.example.babershopmanager.sharedDate.SharedData;
import com.example.babershopmanager.sharedDate.SettingData;

public class SettingFragment extends Fragment
{
    private ProgressBar loadingView;
    private Switch blockSystemSwitch;
    private Button notificationSettingBtn,sendMsgBtn,getUsersBtn;
    private LinearLayout fragmentLayout;
    private View divider;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        SharedData.settingFragment = this;
        divider = view.findViewById(R.id.settingDivider);
        fragmentLayout = view.findViewById(R.id.settingFragment);
        loadingView = view.findViewById(R.id.settingFragmentLoadingView);
        getUsersBtn = view.findViewById(R.id.getUsersBtn);
        sendMsgBtn = view.findViewById(R.id.sendMsgBtn);
        notificationSettingBtn = view.findViewById(R.id.notificationSettingBtn);
        blockSystemSwitch = view.findViewById(R.id.blockSystemSwitch);
        getUsersBtn.setOnClickListener(this::getUsersBtn);
        sendMsgBtn.setOnClickListener(this::sendMsgBtn);
        notificationSettingBtn.setOnClickListener(this::notificationSettingBtn);
        blockSystemSwitch.setOnClickListener(this::blockSystemSwitchClicked);
        view.post(()->blockSystemSwitch.setChecked(SettingData.userCmdIsLock)); //post for wait to finish of build fragment
        updateLoadingView();
        manageFragments();
        return view;
    }


    public void showNotificationFragment() //called from Setting.getNotificationsSettingAns()
    {
        if (SharedData.notificationsFragment == null)
            SharedData.notificationsFragment = new NotificationsFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.settingFragment, SharedData.notificationsFragment);
        fragmentTransaction.commit(); // save the changes
        fragmentLayout.post( ()-> fragmentLayout.setVisibility(View.VISIBLE) ); //for delay if the response come from the server too much fest
        divider.setVisibility(View.VISIBLE);
    }

    private void showUserOptionFragment()
    {
        if (SharedData.userOptionsFragment == null)
            SharedData.userOptionsFragment = new UserOptionsFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.settingFragment, SharedData.userOptionsFragment);
        fragmentTransaction.commit(); // save the changes
        divider.setVisibility(View.VISIBLE);
        fragmentLayout.setVisibility(View.VISIBLE);
    }

    private void showSendMsgFragment()
    {
        if (SharedData.sendMsgFragment == null)
            SharedData.sendMsgFragment = new SendMsgFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.settingFragment, SharedData.sendMsgFragment);
        fragmentTransaction.commit(); // save the changes
        divider.setVisibility(View.VISIBLE);
        fragmentLayout.setVisibility(View.VISIBLE);
    }

    public void showUsersFragment() //called from getUsersListAns
    {
        SettingData.btnClicked  = SettingData.BtnClicked.GET_USERS; //if i show this fragment without press of button
        if (SharedData.showUsersFragment == null)
            SharedData.showUsersFragment = new ShowUsersFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.settingFragment, SharedData.showUsersFragment);
        fragmentTransaction.commit(); // save the changes
        divider.setVisibility(View.VISIBLE);
        fragmentLayout.setVisibility(View.VISIBLE);
    }

    public void refreshUsersList() //called from ShowUserFragment
    {
        SharedData.showUsersFragment = new ShowUsersFragment();
        askForUsersList();
    }


    public void askForUsersList()
    {
        getUsersBtn.setEnabled(false);
        sendMsgBtn.setEnabled(false);
        notificationSettingBtn.setEnabled(false);
        loadingView.setVisibility(View.VISIBLE);
        divider.setVisibility(View.INVISIBLE);
        fragmentLayout.setVisibility(View.INVISIBLE);
        SettingData.getUsersListInRequest = true;
        ServerRequest serverRequest = new ServerRequest((String response) -> Setting.getUsersListAns(response));
        serverRequest.getUsersList(); //after get the data go to showUsersFragment()
    }

    private void askForNotificationSetting()
    {
        getUsersBtn.setEnabled(false);
        sendMsgBtn.setEnabled(false);
        notificationSettingBtn.setEnabled(false);
        loadingView.setVisibility(View.VISIBLE);
        divider.setVisibility(View.INVISIBLE);
        fragmentLayout.setVisibility(View.INVISIBLE);
        SettingData.getSettingFragmentInRequest = true;
        ServerRequest serverRequest = new ServerRequest((String response) -> Setting.getNotificationsSettingAns(response));
        serverRequest.getNotificationsSetting();
    }

    public void showGetUsersIsClicked()
    {
        getUsersBtn.setBackgroundColor(Color.GRAY);
        sendMsgBtn.setBackgroundColor(Color.BLUE);
        notificationSettingBtn.setBackgroundColor(Color.BLUE);
    }

    private void showUserIsClicked()
    {
        notificationSettingBtn.setBackgroundColor(Color.BLUE);
        getUsersBtn.setBackgroundColor(Color.BLUE);
        sendMsgBtn.setBackgroundColor(Color.BLUE);
    }

    private void showSendMsgIsClicked()
    {
        sendMsgBtn.setBackgroundColor(Color.GRAY);
        getUsersBtn.setBackgroundColor(Color.BLUE);
        notificationSettingBtn.setBackgroundColor(Color.BLUE);
    }

    private void showNotificationSettingIsClicked()
    {
        notificationSettingBtn.setBackgroundColor(Color.GRAY);
        sendMsgBtn.setBackgroundColor(Color.BLUE);
        getUsersBtn.setBackgroundColor(Color.BLUE);
    }

    public void showNoneBtnIsClicked()
    {
        getUsersBtn.setBackgroundColor(Color.BLUE);
        notificationSettingBtn.setBackgroundColor(Color.BLUE);
        sendMsgBtn.setBackgroundColor(Color.BLUE);
        divider.setVisibility(View.INVISIBLE);
        fragmentLayout.setVisibility(View.INVISIBLE);
    }

    private void manageFragments()
    {
        if (SettingData.btnClicked == SettingData.BtnClicked.GET_USERS)
        {
            showGetUsersIsClicked();
            if (SettingData.askForUsersList || SettingData.usersWithoutQueue == null)
                askForUsersList();
            else
                showUsersFragment();
        }
        else if (SettingData.btnClicked == SettingData.BtnClicked.USER)
        {
            showUserIsClicked();
            showUserOptionFragment();
        }
        else if (SettingData.btnClicked == SettingData.BtnClicked.SEND_MSG)
        {
            showSendMsgIsClicked();
            showSendMsgFragment();
        }
        else if (SettingData.btnClicked == SettingData.BtnClicked.NOTIFICATIONS_SETTING)
        {
            showNotificationSettingIsClicked();
            askForNotificationSetting();
        }
        else
            showNoneBtnIsClicked();
    }

    public void userClicked() //called from ShowUserFragment
    {
        SettingData.btnClicked = SettingData.BtnClicked.USER;
        showNoneBtnIsClicked();
        showUserOptionFragment();
    }



    private void sendMsgBtn(View view)
    {
        if (SettingData.btnClicked == SettingData.BtnClicked.SEND_MSG)
        {
            SettingData.btnClicked = null;
            showNoneBtnIsClicked();
        }
        else
        {
            SettingData.btnClicked = SettingData.BtnClicked.SEND_MSG;
            showSendMsgIsClicked();
            showSendMsgFragment();
        }
    }

    public void notificationSettingBtn(View view)
    {
        if (SettingData.btnClicked == SettingData.BtnClicked.NOTIFICATIONS_SETTING)
        {
            SettingData.btnClicked = null;
            showNoneBtnIsClicked();
        }
        else
        {
            SettingData.btnClicked = SettingData.BtnClicked.NOTIFICATIONS_SETTING;
            showNotificationSettingIsClicked();
            askForNotificationSetting();
        }
    }

    private void getUsersBtn(View view)
    {
        if (SettingData.btnClicked == SettingData.BtnClicked.GET_USERS)
        {
            SettingData.btnClicked = null;
            showNoneBtnIsClicked();
        }
        else
        {
            SettingData.btnClicked = SettingData.BtnClicked.GET_USERS;
            showGetUsersIsClicked();
            if (SettingData.askForUsersList || SettingData.usersWithoutQueue == null)
                askForUsersList();
            else
                showUsersFragment();
        }
    }

    private void blockSystemSwitchBeforeRequest()
    {
        blockSystemSwitch.setEnabled(false);
        loadingView.setVisibility(View.VISIBLE);
        SettingData.userCmdIsLockInRequest = true;
    }

    private void blockSystemSwitchClicked(View view)
    {
        blockSystemSwitch.setChecked(SettingData.userCmdIsLock);
        if (SettingData.userCmdIsLock)
            unlockSystem();
        else
            lockSystem();
    }

    private void lockSystem()
    {
        String alertTitle = "לנעול את המערכת?";
        String alertMsg = "משתמשים לא יוכלו לקבוע תורים או לשנות את התור שנקבע להם";
        SimpleMethod doIfUserPressOk = () ->
        {
            blockSystemSwitchBeforeRequest();
            ServerRequest serverRequest = new ServerRequest((String response) ->Setting.lockUserCmdAns(response));
            serverRequest.lockUserCmd();
        };
        AlertDialog.showAlertDialog(alertTitle,alertMsg,doIfUserPressOk);
    }

    private void unlockSystem()
    {
        String alertTitle = "להסיר את נעילת המערכת?";
        String alertMsg = "משתמשים יוכלו לקבוע תורים או לשנות את התור שנקבע להם" ;
        SimpleMethod doIfUserPressOk = () ->
        {
            blockSystemSwitchBeforeRequest();
            ServerRequest serverRequest = new ServerRequest((String response) ->Setting.unlockUserCmdAns(response));
            serverRequest.unlockUserCmd();
        };
        AlertDialog.showAlertDialog(alertTitle,alertMsg,doIfUserPressOk);
    }

    public void setBlockSystemSwitch(boolean bool)
    {
        blockSystemSwitch.setChecked(bool);
    }

    public void setBlockSystemSwitchEnabled()
    {
        blockSystemSwitch.setEnabled(true);
    }

    public void updateLoadingView()
    {
        if (SettingData.getUsersListInRequest ||
                SettingData.userCmdIsLockInRequest ||
                SettingData.getSettingFragmentInRequest)
        {
            loadingView.setVisibility(View.VISIBLE);
            if (SettingData.getUsersListInRequest)
                getUsersBtn.setEnabled(false);
            if (SettingData.userCmdIsLockInRequest)
                blockSystemSwitch.setEnabled(false);
            if (SettingData.getSettingFragmentInRequest)
                notificationSettingBtn.setEnabled(false);
        }
        else
            loadingView.setVisibility(View.INVISIBLE);
    }

    public void checkIfRemoveLoadingView()
    {
        if (SettingData.getUsersListInRequest)
            return;
        if (SettingData.userCmdIsLockInRequest)
            return;
        if (SettingData.getSettingFragmentInRequest)
            return;
        getUsersBtn.setEnabled(true);
        sendMsgBtn.setEnabled(true);
        notificationSettingBtn.setEnabled(true);
        loadingView.setVisibility(View.INVISIBLE);
    }
}