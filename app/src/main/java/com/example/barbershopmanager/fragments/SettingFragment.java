package com.example.barbershopmanager.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import androidx.fragment.app.Fragment;
import com.example.barbershopmanager.R;
import com.example.barbershopmanager.fragments.dialogFragments.SimpleMethod;
import com.example.barbershopmanager.fragments.SettingFragments.NotificationsFragment;
import com.example.barbershopmanager.fragments.SettingFragments.SendMsgFragment;
import com.example.barbershopmanager.fragments.SettingFragments.ShowUsersFragment;
import com.example.barbershopmanager.fragments.SettingFragments.UserOptionsFragment;
import com.example.barbershopmanager.fragments.dialogFragments.AlertDialog;
import com.example.barbershopmanager.server.ServerRequest;
import com.example.barbershopmanager.server.Setting;
import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.SettingData;

public class SettingFragment extends Fragment
{
    private ProgressBar loadingView;
    private Switch blockSystemSwitch;
    private Button notificationSettingBtn,sendMsgBtn,getUsersBtn;
    private LinearLayout fragmentLayout;
    private View divider;

    public View onCreateView(LayoutInflater inflater,
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
        else
            SharedData.notificationsFragment.setFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.settingFragment,SharedData.notificationsFragment).commit();
        fragmentLayout.post( ()-> fragmentLayout.setVisibility(View.VISIBLE) ); //for delay if the response come from the server too much fest
        divider.setVisibility(View.VISIBLE);
    }

    private void showUserOptionFragment()
    {
        if (SharedData.userOptionsFragment == null)
            SharedData.userOptionsFragment = new UserOptionsFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.settingFragment,SharedData.userOptionsFragment).commit();
        divider.setVisibility(View.VISIBLE);
        fragmentLayout.setVisibility(View.VISIBLE);
    }

    private void showSendMsgFragment()
    {
        SharedData.sendMsgFragment = new SendMsgFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.settingFragment,SharedData.sendMsgFragment).commit();
        divider.setVisibility(View.VISIBLE);
        makeFragmentLayoutVisibleInDaley();
    }

    public void showUsersFragment() //called from getUsersListAns
    {
        SettingData.menuBtnClicked = SettingData.MenuBtnClicked.GET_USERS; //if i show this fragment without press of button
        if (SharedData.showUsersFragment == null)
            SharedData.showUsersFragment = new ShowUsersFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.settingFragment,SharedData.showUsersFragment).commit();
        divider.setVisibility(View.VISIBLE);
        makeFragmentLayoutVisibleInDaley();
    }

    private void makeFragmentLayoutVisibleInDaley() //let the fragment time to load
    {
        new Handler().postDelayed(()-> fragmentLayout.setVisibility(View.VISIBLE), 50);
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
        SettingData.menuBtnClicked = null;
        getUsersBtn.setBackgroundColor(Color.BLUE);
        notificationSettingBtn.setBackgroundColor(Color.BLUE);
        sendMsgBtn.setBackgroundColor(Color.BLUE);
        divider.setVisibility(View.INVISIBLE);
        fragmentLayout.setVisibility(View.INVISIBLE);
    }

    private void manageFragments()
    {
        if (SettingData.menuBtnClicked == SettingData.MenuBtnClicked.GET_USERS)
        {
            showGetUsersIsClicked();
            if (SettingData.askForUsersList || SettingData.usersWithoutQueue == null)
                askForUsersList();
            else
                showUsersFragment();
        }
        else if (SettingData.menuBtnClicked == SettingData.MenuBtnClicked.USER)
        {
            showUserIsClicked();
            showUserOptionFragment();
        }
        else if (SettingData.menuBtnClicked == SettingData.MenuBtnClicked.SEND_MSG)
        {
            showSendMsgIsClicked();
            showSendMsgFragment();
        }
        else if (SettingData.menuBtnClicked == SettingData.MenuBtnClicked.NOTIFICATIONS_SETTING)
        {
            showNotificationSettingIsClicked();
            askForNotificationSetting();
        }
        else
            showNoneBtnIsClicked();
    }

    public void userClicked() //called from ShowUserFragment
    {
        showNoneBtnIsClicked();
        SettingData.menuBtnClicked = SettingData.MenuBtnClicked.USER;
        showUserOptionFragment();
    }

    private void sendMsgBtn(View view)
    {
        if (SettingData.menuBtnClicked == SettingData.MenuBtnClicked.SEND_MSG)
            showNoneBtnIsClicked();
        else
        {
            SettingData.menuBtnClicked = SettingData.MenuBtnClicked.SEND_MSG;
            showSendMsgIsClicked();
            showSendMsgFragment();
        }
    }

    public void notificationSettingBtn(View view)
    {
        if (SettingData.menuBtnClicked == SettingData.MenuBtnClicked.NOTIFICATIONS_SETTING)
            showNoneBtnIsClicked();
        else
        {
            SettingData.menuBtnClicked = SettingData.MenuBtnClicked.NOTIFICATIONS_SETTING;
            showNotificationSettingIsClicked();
            askForNotificationSetting();
        }
    }

    private void getUsersBtn(View view)
    {
        if (SettingData.menuBtnClicked == SettingData.MenuBtnClicked.GET_USERS)
            showNoneBtnIsClicked();
        else
        {
            SettingData.menuBtnClicked = SettingData.MenuBtnClicked.GET_USERS;
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