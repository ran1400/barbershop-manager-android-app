package com.example.babershopmanager.fragments.SettingFragments;


import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.babershopmanager.R;
import com.example.babershopmanager.fragments.dialogFragments.SimpleMethod;
import com.example.babershopmanager.fragments.dialogFragments.AlertDialog;
import com.example.babershopmanager.notifications.MyFirebaseMessagingService;
import com.example.babershopmanager.server.ServerRequest;
import com.example.babershopmanager.server.Setting;
import com.example.babershopmanager.sharedDate.SharedData;
import com.example.babershopmanager.sharedDate.SettingData;


public class SendMsgFragment extends Fragment
{
    private Button sendMsgBtn,sendTestMsgBtn;
    private EditText pushMsgTitleEditTest,pushMsgBodyEditText,inAppMsgEditText;
    private ProgressBar loadingView;
    private CheckBox quietMsgCheckBox;
    private ScrollView inAppMsgWindow,notificationMsgWindow;
    private RadioGroup selectMsgSwitch;
    private CheckBox cancelInAppMsgCheckBox;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_send_msg, container, false);
        SharedData.sendMsgFragment = this;
        Button inAppUpdateMsgBtn = view.findViewById(R.id.inAppUpdateMsgBtn);
        cancelInAppMsgCheckBox = view.findViewById(R.id.cancelInAppMsgCheckBox);
        inAppMsgEditText = view.findViewById(R.id.inAppMsgContent);
        selectMsgSwitch = view.findViewById(R.id.selectMsgSwitch);
        inAppMsgWindow = view.findViewById(R.id.inAppMsgWindow);
        notificationMsgWindow = view.findViewById(R.id.notificationMsgWindow);
        sendMsgBtn = view.findViewById(R.id.sendPushMsgBtn);
        sendTestMsgBtn = view.findViewById(R.id.sendTestPushMsgBtn);
        pushMsgTitleEditTest = view.findViewById(R.id.pushMsgTitle);
        pushMsgBodyEditText = view.findViewById(R.id.pushMsgBody);
        loadingView = view.findViewById(R.id.sendMsgLoadingView);
        quietMsgCheckBox = view.findViewById(R.id.quietPushMsgCheckBox);
        sendMsgBtn.setOnClickListener(this::sendMsgBtn);
        inAppUpdateMsgBtn.setOnClickListener(this::setInAppMsgBtn);
        sendTestMsgBtn.setOnClickListener(this::sendTestMsgBtn);
        inAppMsgEditText.setOnTouchListener(this::inAppMsgEditTextTouchListener);
        cancelInAppMsgCheckBox.setOnClickListener(this::cancelInAppMsgCheckBoxTouchListener);
        selectMsgSwitch.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> selectMsgSwitchChanged(checkedId));
        updateViews();
        return view;
    }

    public void sendPushMsgInRequest()
    {
        SettingData.sendPushMsgInRequest = true;
        loadingView.setVisibility(View.VISIBLE);
        sendMsgBtn.setEnabled(false);
        sendTestMsgBtn.setEnabled(false);
    }

    public void sendPushMsgNotInRequest()
    {
        SettingData.sendPushMsgInRequest = false;
        loadingView.setVisibility(View.INVISIBLE);
        sendMsgBtn.setEnabled(true);
        sendTestMsgBtn.setEnabled(true);
    }

    private void updateViews()
    {
        if (SettingData.sendPushMsgInRequest)
            sendPushMsgInRequest();
        else
            sendPushMsgNotInRequest();
    }

    public boolean inAppMsgEditTextTouchListener(View view, MotionEvent motionEvent)
    {
        cancelInAppMsgCheckBox.setChecked(false);
        return false;
    }

    private void cancelInAppMsgCheckBoxTouchListener(View view)
    {
        if (cancelInAppMsgCheckBox.isChecked())
        {
            inAppMsgEditText.setText("");
            inAppMsgEditText.clearFocus();
        }
    }

    private void setInAppMsgBtn(View view)
    {
        String newMsg = inAppMsgEditText.getText().toString();
        if (cancelInAppMsgCheckBox.isChecked() == false && newMsg.isEmpty())
            Toast.makeText(SharedData.mainActivity, "הכנס טקסט", Toast.LENGTH_SHORT).show();
        else if (newMsg.contains("<"))
            Toast.makeText(SharedData.mainActivity, "התו '>' / '<' אסור לשימוש", Toast.LENGTH_SHORT).show();
        else
        {
            String alertTitle = "לעדכן את ההודעה המוצגת למשתמש?";
            String alertMsg;
            if (newMsg.isEmpty())
                alertMsg = "לא תופיע למשתמש הודעה";
            else
                alertMsg = "ההודעה :\n" + newMsg;
            SimpleMethod doIfUserPressOk = () ->
            {
                doBeforeUpdateInMsgRequest();
                ServerRequest serverRequest = new ServerRequest((String response) -> Setting.setInAppMsgAns(response));
                serverRequest.setInAppMsg(newMsg);
            };
            AlertDialog.showAlertDialog(alertTitle,alertMsg,doIfUserPressOk);
        }
    }

    public void doBeforeUpdateInMsgRequest()
    {
        SettingData.setInAppMsgInRequest = true;
        inAppMsgWindow.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
    }

    public void MakeLoadingViewGone()
    {
        if (SettingData.setInAppMsgInRequest == false)
            loadingView.setVisibility(View.GONE);
    }

    public void showInAppMsgWindow(String msgContent)
    {
        if (selectMsgSwitch.getCheckedRadioButtonId() != R.id.inAppMsgRadioBtn)
            return;
        if (msgContent.isEmpty())
        {
            cancelInAppMsgCheckBox.setChecked(true);
            inAppMsgEditText.setText("");
        }
        else
        {
            inAppMsgEditText.setText(msgContent);
            cancelInAppMsgCheckBox.setChecked(false);
        }
        inAppMsgWindow.setVisibility(View.VISIBLE);
    }

    private void selectMsgSwitchChanged(int checkId)
    {
        if (checkId == R.id.notificationMsgRadioBtn)
        {
            inAppMsgWindow.setVisibility(View.GONE);
            notificationMsgWindow.setVisibility(View.VISIBLE);
        }
        else // (checkId == R.id.inAppMsgRadioBtn)
        {
            inAppMsgRadioBtn();
        }
    }

    private void inAppMsgRadioBtn()
    {
        notificationMsgWindow.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        ServerRequest serverRequest = new ServerRequest((String response) ->Setting.getInAppMsgContentAns(response));
        serverRequest.getInAppMsg();
    }


    private void sendMsgBtn(View view)
    {
        String msgTitle = pushMsgTitleEditTest.getText().toString();
        String msgBody = pushMsgBodyEditText.getText().toString();
        String alertTitle = "לשלוח התראה לכל המשתמשים?" ;
        String alertMsg = "כותרת ההודעה :\n" + msgTitle + "\nתוכן ההודעה :\n" + msgBody;
        SimpleMethod doIfUserPressOk = () ->
        {
            sendPushMsgInRequest();
            MyFirebaseMessagingService.sendNotificationToAllUsers(msgTitle,msgBody,quietMsgCheckBox.isChecked());
        };
        AlertDialog.showAlertDialog(alertTitle,alertMsg,doIfUserPressOk);
    }

    private void sendTestMsgBtn(View view)
    {
        sendPushMsgInRequest();
        String title = pushMsgTitleEditTest.getText().toString();
        String body = pushMsgBodyEditText.getText().toString();
        MyFirebaseMessagingService.sendTestNotification(title,body,quietMsgCheckBox.isChecked());
    }
}