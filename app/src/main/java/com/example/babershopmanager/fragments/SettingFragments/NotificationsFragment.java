package com.example.babershopmanager.fragments.SettingFragments;

import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.example.babershopmanager.R;
import com.example.babershopmanager.server.ServerRequest;
import com.example.babershopmanager.server.Setting;
import com.example.babershopmanager.sharedDate.SharedData;
import com.example.babershopmanager.sharedDate.SettingData;


public class NotificationsFragment extends Fragment
{

    private ProgressBar loadingView;
    private NumberPicker getNotificationsTypePicker;
    private final int NEVER = 0 , EVER = 1 ,ACCORDING_TIMES = 2;
    private NumberPicker getNotificationsHoursDaysPicker;
    private final int HOURS = 0 , DAYS = 1;
    private NumberPicker getNotificationsNumPicker;
    private Button getNotificationOkBtn;
    private Switch sendNotificationsQueuesUpdates;
    private Switch sendNotificationsBlockUser;
    private Switch sendNotificationsUnblockUser;
    private Switch sendNotificationsRemoveUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        SharedData.notificationsFragment = this;
        loadingView = view.findViewById(R.id.notificationsFragmentLoadingView);
        getNotificationsTypePicker = view.findViewById(R.id.getNotificationsTypePicker);
        getNotificationsNumPicker = view.findViewById(R.id.getNotificationsNumPicker);
        getNotificationsHoursDaysPicker = view.findViewById(R.id.getNotificationsHoursDaysPicker);
        getNotificationOkBtn = view.findViewById(R.id.getNotificationOkBtn);
        sendNotificationsQueuesUpdates = view.findViewById(R.id.sendNotificationsQueuesUpdates);
        sendNotificationsBlockUser = view.findViewById(R.id.sendNotificationsBlockUser);
        sendNotificationsUnblockUser = view.findViewById(R.id.sendNotificationsUnblockUser);
        sendNotificationsRemoveUser= view.findViewById(R.id.sendNotificationsRemoveUser);
        getNotificationOkBtn.setOnClickListener(this::getNotificationOkBtnClicked);
        getNotificationsTypePicker.setMinValue(0);
        getNotificationsTypePicker.setMaxValue(2);
        getNotificationsTypePicker.setDisplayedValues( new String[] {"אף פעם", "תמיד", "שיחולו עוד פחות מ" } );
        getNotificationsNumPicker.setMinValue(1);
        getNotificationsNumPicker.setMaxValue(23);
        getNotificationsNumPicker.setWrapSelectorWheel(false); //make it scroll only down on init state
        getNotificationsNumPicker.setVisibility(View.GONE);
        getNotificationsHoursDaysPicker.setVisibility(View.GONE);
        getNotificationsHoursDaysPicker.setOnValueChangedListener((NumberPicker n,int i1,int i2)-> pickerValueChanged());
        getNotificationsNumPicker.setOnValueChangedListener((NumberPicker n,int i1,int i2)-> pickerValueChanged());
        getNotificationsTypePicker.setOnValueChangedListener((NumberPicker numberPicker, int i, int i1)-> typePickerChanged(i1));
        getNotificationsHoursDaysPicker.setMinValue(0);
        getNotificationsHoursDaysPicker.setMaxValue(1);
        getNotificationsHoursDaysPicker.setDisplayedValues( new String[] { "שעות", "ימים" } );
        sendNotificationsBlockUser.setOnClickListener(this::sendUserBlockNotificationsCLicked);
        sendNotificationsUnblockUser.setOnClickListener(this::sendUserUnblockNotificationsClicked);
        sendNotificationsRemoveUser.setOnClickListener(this::sendUserRemoveNotificationsInRequestClicked);
        sendNotificationsQueuesUpdates.setOnClickListener(this::sendUserQueueNotificationsCLicked);
        checkIfInRequest();
        fillInfoFromServerToPickers();
        fillInfoFromServerSwitches();
        return view;
    }

    private void checkIfInRequest()
    {
        boolean showLoadingView = false;
        if (SettingData.setSecondsAmountToSendNotificationInRequest)
        {
            disablePickerArea();
            showLoadingView = true;
        }
        if (SettingData.sendUserBlockNotificationsInRequest)
        {
            sendNotificationsBlockUser.setEnabled(false);
            showLoadingView = true;
        }
        if (SettingData.sendUserUnblockNotificationsInRequest)
        {
            sendNotificationsUnblockUser.setEnabled(false);
            showLoadingView = true;
        }
        if(SettingData.sendUserRemoveNotificationsInRequest)
        {
            sendNotificationsRemoveUser.setEnabled(false);
            showLoadingView = true;
        }
        if(SettingData.sendUserQueueNotificationsInRequest)
        {
            sendNotificationsQueuesUpdates.setEnabled(false);
            showLoadingView = true;
        }
        if (showLoadingView)
            loadingView.setVisibility(View.VISIBLE);
    }

    public void sendUserBlockNotificationsRefreshSwitch()
    {
        sendNotificationsBlockUser.setChecked(SettingData.sendUserBlockNotifications);
    }

    public void sendUserQueueNotificationsRefreshSwitch()
    {
        sendNotificationsQueuesUpdates.setChecked(SettingData.sendUserQueueNotifications);
    }

    public void sendUserRemoveNotificationsRefreshSwitch()
    {
        sendNotificationsRemoveUser.setChecked(SettingData.sendUserRemoveNotifications);
    }

    public void sendUserUnblockNotificationsRefreshSwitch()
    {
        sendNotificationsUnblockUser.setChecked(SettingData.sendUserUnblockNotifications);
    }

    private void sendUserBlockNotificationsCLicked(View view)
    {
        boolean crntState = ! sendNotificationsBlockUser.isChecked();
        sendNotificationsBlockUser.setChecked(crntState);
        sendNotificationsBlockUser.setEnabled(false);
        loadingView.setVisibility(View.VISIBLE);
        SettingData.sendUserBlockNotificationsInRequest = true;
        ServerRequest serverRequest = new ServerRequest((String response) -> Setting.setSendUserBlockNotificationsAns(response));
        serverRequest.setSendUserBlockNotifications(! crntState);
    }

    private void sendUserQueueNotificationsCLicked(View view)
    {
        boolean crntState = ! sendNotificationsQueuesUpdates.isChecked();
        sendNotificationsQueuesUpdates.setChecked(crntState);
        sendNotificationsQueuesUpdates.setEnabled(false);
        loadingView.setVisibility(View.VISIBLE);
        SettingData.sendUserQueueNotificationsInRequest = true;
        ServerRequest serverRequest = new ServerRequest((String response) -> Setting.setSendUserQueueNotificationsAns(response));
        serverRequest.setSendUserQueueNotifications(! crntState);
    }

    private void sendUserRemoveNotificationsInRequestClicked(View view)
    {
        boolean crntState = ! sendNotificationsRemoveUser.isChecked();
        sendNotificationsRemoveUser.setChecked(crntState);
        sendNotificationsRemoveUser.setEnabled(false);
        loadingView.setVisibility(View.VISIBLE);
        SettingData.sendUserRemoveNotificationsInRequest = true;
        ServerRequest serverRequest = new ServerRequest((String response) -> Setting.setSendUserRemoveNotificationsAns(response));
        serverRequest.setSendUserRemoveNotifications(! crntState);
    }

    private void sendUserUnblockNotificationsClicked(View view)
    {
        boolean crntState = ! sendNotificationsUnblockUser.isChecked();
        sendNotificationsUnblockUser.setChecked(crntState);
        sendNotificationsUnblockUser.setEnabled(false);
        loadingView.setVisibility(View.VISIBLE);
        SettingData.sendUserUnblockNotificationsInRequest = true;
        ServerRequest serverRequest = new ServerRequest((String response) -> Setting.setSendUserUnblockNotificationsAns(response));
        serverRequest.setSendUserUnblockNotifications(! crntState);
    }

    private void fillInfoFromServerSwitches()
    {
        sendNotificationsQueuesUpdates.setChecked(SettingData.sendUserQueueNotifications);
        sendNotificationsBlockUser.setChecked(SettingData.sendUserBlockNotifications);
        sendNotificationsUnblockUser.setChecked(SettingData.sendUserUnblockNotifications);
        sendNotificationsRemoveUser.setChecked(SettingData.sendUserRemoveNotifications);
    }

    public void checkIfGoneLoadingView()
    {
        if (SettingData.sendUserBlockNotificationsInRequest)
            return;
        if (SettingData.sendUserQueueNotificationsInRequest)
            return;
        if (SettingData.sendUserRemoveNotificationsInRequest)
            return;
        if (SettingData.sendUserUnblockNotificationsInRequest)
            return;
        if (SettingData.setSecondsAmountToSendNotificationInRequest)
            return;
        loadingView.setVisibility(View.GONE);
    }

    public void enableSendNotificationsQueuesUpdates()
    {
        sendNotificationsQueuesUpdates.setEnabled(true);
    }

    public void enableSendNotificationsUnblockUser()
    {
        sendNotificationsUnblockUser.setEnabled(true);
    }
    public void enableSendNotificationsRemoveUser()
    {
        sendNotificationsRemoveUser.setEnabled(true);
    }

    public void enableSendNotificationsBlockUser()
    {
        sendNotificationsBlockUser.setEnabled(true);
    }

    public void fillInfoFromServerToPickers()
    {
        getNotificationOkBtn.setVisibility(View.GONE);
        if (SettingData.secondsAmountToGetNotification == NEVER)
            getNotificationsTypePicker.setValue(NEVER);
        else if (SettingData.secondsAmountToGetNotification == EVER)
            getNotificationsTypePicker.setValue(EVER);
        else // (sharedData.secondsAmountToGetNotification == ACCORDING_TIMES)
        {
            getNotificationsTypePicker.setValue(ACCORDING_TIMES);
            getNotificationsNumPicker.setVisibility(View.VISIBLE);
            getNotificationsHoursDaysPicker.setVisibility(View.VISIBLE);
            int hours = SettingData.secondsAmountToGetNotification / 60 / 60; //60  * 60 is num of seconds in hour
            if (hours < 24)
                getNotificationsNumPicker.setValue(HOURS);
            else
            {
                int days = hours / 24;
                getNotificationsHoursDaysPicker.setValue(DAYS);
                getNotificationsNumPicker.setValue(days);
            }
        }
    }

    public void getNotificationOkBtnClicked(View view)
    {
        SettingData.setSecondsAmountToSendNotificationInRequest = true;
        loadingView.setVisibility(View.VISIBLE);
        disablePickerArea();
        ServerRequest serverRequest = new ServerRequest((String response) -> Setting.setSecondsAmountToSendNotificationAns(response));
        Log.d("line249",getNotificationsTypePicker.getValue()+"");
        if (getNotificationsTypePicker.getValue() == EVER)
            serverRequest.everSendQueueUpdates();
        else if (getNotificationsTypePicker.getValue() == NEVER)
            serverRequest.neverSendQueueUpdates();
        else // (getNotificationsTypePicker.getValue() == ACCORDING_TIMES )
        {
            int numOfSeconds;
            if (getNotificationsHoursDaysPicker.getValue() == HOURS)
                numOfSeconds = getNotificationsNumPicker.getValue() * 60 * 60; //60  * 60 is num of seconds in hour
            else // (getNotificationsHoursDaysPicker.getValue() == DAYS)
                numOfSeconds = getNotificationsNumPicker.getValue() *  60 * 60 * 24 ;//60  * 60 * 24 is num of seconds in day
            serverRequest.setSecondsAmountToSendNotification(numOfSeconds);
        }
    }

    private void disablePickerArea()
    {
        getNotificationOkBtn.setEnabled(false);
        getNotificationsTypePicker.setEnabled(false);
        getNotificationsNumPicker.setEnabled(false);
        getNotificationsHoursDaysPicker.setEnabled(false);
    }

    public void enabledPickersArea()
    {
        getNotificationOkBtn.setEnabled(true);
        getNotificationsTypePicker.setEnabled(true);
        getNotificationsNumPicker.setEnabled(true);
        getNotificationsHoursDaysPicker.setEnabled(true);
    }

    private void pickerValueChanged() //hours/day picker and numbers picker
    {
        getNotificationOkBtn.setVisibility(View.VISIBLE);
    }

    private void typePickerChanged(int type)
    {
        getNotificationOkBtn.setVisibility(View.VISIBLE);
        if (type == ACCORDING_TIMES)
        {
            getNotificationsNumPicker.setVisibility(View.VISIBLE);
            getNotificationsHoursDaysPicker.setVisibility(View.VISIBLE);
        }
        else // (type == NEVER || type == EVER)
        {
            getNotificationsNumPicker.setVisibility(View.GONE);
            getNotificationsHoursDaysPicker.setVisibility(View.GONE);
        }
    }


}