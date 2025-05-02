package com.example.barbershopmanager.fragments.SettingFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.barbershopmanager.R;
import com.example.barbershopmanager.server.ServerRequest;
import com.example.barbershopmanager.server.Setting;
import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.SettingData;


public class NotificationsFragment extends Fragment
{

    private ProgressBar loadingView;
    private NumberPicker getNotificationsTypePicker;
    public static final int NEVER = 0 , EVER = 1;
    private final int ACCORDING_TIMES = 2;
    private NumberPicker getNotificationsHoursDaysPicker;
    private final int HOURS = 0 , DAYS = 1;
    private NumberPicker getNotificationsNumPicker;
    private Button getNotificationOkBtn;
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
        sendNotificationsBlockUser.setOnClickListener(this::sendUserBlockNotificationsClicked);
        sendNotificationsUnblockUser.setOnClickListener(this::sendUserUnblockNotificationsClicked);
        sendNotificationsRemoveUser.setOnClickListener(this::sendUserRemoveNotificationsInRequestClicked);
        setFragment();
        return view;
    }

    public void setFragment()
    {
        checkIfInRequest();
        fillInfoFromServerToPickers();
        fillInfoFromServerSwitches();
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
        if (showLoadingView)
            loadingView.setVisibility(View.VISIBLE);
    }

    public void sendUserBlockNotificationsRefreshSwitch()
    {
        sendNotificationsBlockUser.setChecked(SettingData.sendUserBlockNotifications);
    }


    public void sendUserRemoveNotificationsRefreshSwitch()
    {
        sendNotificationsRemoveUser.setChecked(SettingData.sendUserRemoveNotifications);
    }

    public void sendUserUnblockNotificationsRefreshSwitch()
    {
        sendNotificationsUnblockUser.setChecked(SettingData.sendUserUnblockNotifications);
    }

    private void sendUserBlockNotificationsClicked(View view)
    {
        boolean crntState = ! sendNotificationsBlockUser.isChecked(); //because the click flip it
        sendNotificationsBlockUser.setChecked(crntState);
        sendNotificationsBlockUser.setEnabled(false);
        loadingView.setVisibility(View.VISIBLE);
        SettingData.sendUserBlockNotificationsInRequest = true;
        ServerRequest serverRequest = new ServerRequest((String response) -> Setting.setSendUserBlockNotificationsAns(response));
        serverRequest.setSendUserBlockNotifications(! crntState);
    }

    private void sendUserRemoveNotificationsInRequestClicked(View view)
    {
        boolean crntState = ! sendNotificationsRemoveUser.isChecked(); //because the click flip it
        sendNotificationsRemoveUser.setChecked(crntState);
        sendNotificationsRemoveUser.setEnabled(false);
        loadingView.setVisibility(View.VISIBLE);
        SettingData.sendUserRemoveNotificationsInRequest = true;
        ServerRequest serverRequest = new ServerRequest((String response) -> Setting.setSendUserRemoveNotificationsAns(response));
        serverRequest.setSendUserRemoveNotifications(! crntState);
    }

    private void sendUserUnblockNotificationsClicked(View view)
    {
        boolean crntState = ! sendNotificationsUnblockUser.isChecked(); //because the click flip it
        sendNotificationsUnblockUser.setChecked(crntState);
        sendNotificationsUnblockUser.setEnabled(false);
        loadingView.setVisibility(View.VISIBLE);
        SettingData.sendUserUnblockNotificationsInRequest = true;
        ServerRequest serverRequest = new ServerRequest((String response) -> Setting.setSendUserUnblockNotificationsAns(response));
        serverRequest.setSendUserUnblockNotifications(! crntState);
    }

    private void fillInfoFromServerSwitches()
    {
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
        getNotificationsTypePicker.setValue(SettingData.secondsAmountToGetNotification);
        if (SettingData.secondsAmountToGetNotification == ACCORDING_TIMES)
        {
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
        int sendToServer = getNotificationsTypePicker.getValue();
        if (getNotificationsTypePicker.getValue() == ACCORDING_TIMES )
        {
            if (getNotificationsHoursDaysPicker.getValue() == HOURS)
                sendToServer = getNotificationsNumPicker.getValue() * 60 * 60; //60  * 60 is num of seconds in hour
            else // (getNotificationsHoursDaysPicker.getValue() == DAYS)
                sendToServer = getNotificationsNumPicker.getValue() *  60 * 60 * 24 ;//60  * 60 * 24 is num of seconds in day
        }
        if (sendToServer == SettingData.secondsAmountToGetNotification)
            Toast.makeText(SharedData.mainActivity, "נתון זה כבר קיים במערת", Toast.LENGTH_SHORT).show();
        else
        {
            SettingData.setSecondsAmountToSendNotificationInRequest = true;
            loadingView.setVisibility(View.VISIBLE);
            disablePickerArea();
            ServerRequest serverRequest = new ServerRequest((String response) -> Setting.setSecondsAmountToSendNotificationAns(response));
            serverRequest.setSecondsAmountToSendNotification(sendToServer);
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