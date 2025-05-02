package com.example.barbershopmanager.fragments.SettingFragments;


import static com.example.barbershopmanager.sharedDate.SharedData.mainActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import com.example.barbershopmanager.R;
import com.example.barbershopmanager.fragments.dialogFragments.SimpleMethod;
import com.example.barbershopmanager.fragments.dialogFragments.AlertDialog;
import com.example.barbershopmanager.notifications.MyFirebaseMessagingService;
import com.example.barbershopmanager.server.ServerRequest;
import com.example.barbershopmanager.server.Setting;
import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.SettingData;


public class SendMsgFragment extends Fragment
{
    private Button sendMsgBtn,sendTestMsgBtn,sendMailBtn,inAppUpdateMsgBtn;
    private EditText pushMsgTitleEditTest,pushMsgBodyEditText;

    private EditText inAppMsgEditText;

    private EditText mailTitleEditText,mailBodyEditText;
    private ProgressBar inAppMsgLoadingView,sendMailLoadingView,notificationMsgLoadingView;
    private ScrollView inAppMsgWindow,notificationMsgWindow,sendMailWindow;
    private RadioGroup selectMsgSwitch;
    private CheckBox cancelInAppMsgCheckBox;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_send_msg, container, false);
        SharedData.sendMsgFragment = this;
        inAppUpdateMsgBtn = view.findViewById(R.id.inAppUpdateMsgBtn);
        cancelInAppMsgCheckBox = view.findViewById(R.id.cancelInAppMsgCheckBox);
        inAppMsgEditText = view.findViewById(R.id.inAppMsgContent);
        mailTitleEditText = view.findViewById(R.id.mailTitle);
        mailBodyEditText = view.findViewById(R.id.mailBody);
        selectMsgSwitch = view.findViewById(R.id.selectMsgSwitch);
        inAppMsgWindow = view.findViewById(R.id.inAppMsgWindow);
        sendMailWindow = view.findViewById(R.id.mailWindow);
        notificationMsgWindow = view.findViewById(R.id.notificationMsgWindow);
        sendMsgBtn = view.findViewById(R.id.sendPushMsgBtn);
        sendTestMsgBtn = view.findViewById(R.id.sendTestPushMsgBtn);
        sendMailBtn = view.findViewById(R.id.sendMailBtn);
        pushMsgTitleEditTest = view.findViewById(R.id.pushMsgTitle);
        pushMsgBodyEditText = view.findViewById(R.id.pushMsgBody);
        notificationMsgLoadingView = view.findViewById(R.id.notificationMsgLoadingView);
        inAppMsgLoadingView = view.findViewById(R.id.inAppMsgLoadingView);
        sendMailLoadingView = view.findViewById(R.id.sendMailLoadingView);
        sendMsgBtn.setOnClickListener(this::sendPushMsgBtn);
        inAppUpdateMsgBtn.setOnClickListener(this::setInAppMsgBtn);
        sendTestMsgBtn.setOnClickListener(this::sendTestPushMsgBtn);
        inAppMsgEditText.setOnTouchListener(this::inAppMsgEditTextTouchListener);
        sendMailBtn.setOnClickListener(this::sendMailBtn);
        cancelInAppMsgCheckBox.setOnClickListener(this::cancelInAppMsgCheckBoxTouchListener);
        selectMsgSwitch.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> selectMsgSwitchChanged(checkedId));
        return view;
    }

    public void sendNotificationMsgInRequest()
    {
        SettingData.sendPushMsgInRequest = true;
        notificationMsgLoadingView.setVisibility(View.VISIBLE);
        sendMsgBtn.setEnabled(false);
        sendTestMsgBtn.setEnabled(false);
    }

    public void sendMailInRequest()
    {
        SettingData.sendMailInRequest = true;
        sendMailLoadingView.setVisibility(View.VISIBLE);
        sendMailBtn.setEnabled(false);
    }

    public void sendMailNotInRequest()
    {
        sendMailLoadingView.setVisibility(View.INVISIBLE);
        sendMailBtn.setEnabled(true);
    }

    public void sendNotificationMsgNotInRequest()
    {
        notificationMsgLoadingView.setVisibility(View.INVISIBLE);
        sendMsgBtn.setEnabled(true);
        sendTestMsgBtn.setEnabled(true);
    }

    public void inAppMsgNotInRequest(boolean stayInCrntFragment)//if there are error sometimes we need to exit from the fragment
    {
        inAppMsgLoadingView.setVisibility(View.GONE);
        if (stayInCrntFragment)
        {
            cancelInAppMsgCheckBox.setVisibility(View.VISIBLE);
            inAppMsgEditText.setVisibility(View.VISIBLE);
            inAppUpdateMsgBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            selectMsgSwitch.check(R.id.sendMailRadioBtn);
            SharedData.settingFragment.showNoneBtnIsClicked();
        }

    }

    private void inAppMsgInRequest()
    {
        SettingData.setInAppMsgInRequest = true;
        inAppMsgLoadingView.setVisibility(View.VISIBLE);
        cancelInAppMsgCheckBox.setVisibility(View.GONE);
        inAppMsgEditText.setVisibility(View.GONE);
        inAppUpdateMsgBtn.setVisibility(View.GONE);
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
        if (cancelInAppMsgCheckBox.isChecked() == false && newMsg.trim().isEmpty())
            Toast.makeText(SharedData.mainActivity, "הכנס טקסט", Toast.LENGTH_SHORT).show();
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
                inAppMsgInRequest();
                ServerRequest serverRequest = new ServerRequest((String response) -> Setting.setInAppMsgAns(response));
                serverRequest.setInAppMsg(newMsg);
            };
            AlertDialog.showAlertDialog(alertTitle,alertMsg,doIfUserPressOk);
        }
    }


    public void showInAppMsgWindow(String msgContent) // get call from server response
    {
        if (selectMsgSwitch.getCheckedRadioButtonId() != R.id.inAppMsgRadioBtn)
            return; // check if i not move to under page
        if (msgContent == null)
        {
            cancelInAppMsgCheckBox.setChecked(true);
            inAppMsgEditText.setText("");
        }
        else
        {
            inAppMsgEditText.setText(msgContent);
            cancelInAppMsgCheckBox.setChecked(false);
        }
    }

    private void selectMsgSwitchChanged(int checkId)
    {
        if (checkId == R.id.notificationMsgRadioBtn)
        {
            if (SettingData.sendPushMsgInRequest)
                sendNotificationMsgInRequest();
            else
                sendNotificationMsgNotInRequest();
            inAppMsgWindow.setVisibility(View.GONE);
            sendMailWindow.setVisibility(View.GONE);
            notificationMsgWindow.setVisibility(View.VISIBLE);
        }
        else if (checkId == R.id.sendMailRadioBtn)
        {
            if (SettingData.sendMailInRequest)
                sendMailInRequest();
            else
                sendMailNotInRequest();
            inAppMsgWindow.setVisibility(View.GONE);
            sendMailWindow.setVisibility(View.VISIBLE);
            notificationMsgWindow.setVisibility(View.GONE);
        }
        else // (checkId == R.id.inAppMsgRadioBtn)
        {
            inAppMsgInRequest();
            inAppMsgWindow.setVisibility(View.VISIBLE);
            sendMailWindow.setVisibility(View.GONE);
            notificationMsgWindow.setVisibility(View.GONE);
            ServerRequest serverRequest = new ServerRequest((String response) ->Setting.getInAppMsgContentAns(response));
            serverRequest.getInAppMsg();
        }
    }

    private void sendMailBtn(View view)
    {
        String mailTitle = mailTitleEditText.getText().toString();
        if (mailTitle.trim().isEmpty())
        {
            Toast.makeText(mainActivity, "הכותרת ריקה", Toast.LENGTH_SHORT).show();
            return;
        }
        String mailBody = mailBodyEditText.getText().toString();
        String alertTitle = "לשלוח מייל לכל המשתמשים?" ;
        String alertMsg = "כותרת המייל :\n" + mailTitle ;
        if ( ! mailBody.isEmpty())
            alertMsg += "\nתוכן המייל :\n" + mailBody;
        SimpleMethod doIfUserPressOk = () ->
        {
            sendMailInRequest();
            ServerRequest serverRequest = new ServerRequest((String response) -> Setting.sendMailToAllTheUsersAns(response));
            serverRequest.sendMailToAllTheUsers(mailTitle,mailBody);
        };
        AlertDialog.showAlertDialog(alertTitle,alertMsg,doIfUserPressOk);
    }

    private void sendPushMsgBtn(View view)
    {
        String msgTitle = pushMsgTitleEditTest.getText().toString();
        String msgBody = pushMsgBodyEditText.getText().toString();
        if (msgTitle.trim().isEmpty())
        {
            Toast.makeText(SharedData.mainActivity, "הכנס כותרת", Toast.LENGTH_SHORT).show();
            return;
        }
        String alertTitle = "לשלוח התראה לכל המשתמשים?" ;
        String alertMsg = "כותרת ההודעה :\n" + msgTitle ;
        if (! msgBody.isEmpty())
            alertMsg += "\nתוכן ההודעה :\n" + msgBody;
        SimpleMethod doIfUserPressOk = () ->
        {
            sendNotificationMsgInRequest();
            ServerRequest serverRequest = new ServerRequest((response) -> Setting.sendNotificationAns(response));
            serverRequest.sendNotificationToAllUsers(msgTitle,msgBody);
        };
        AlertDialog.showAlertDialog(alertTitle,alertMsg,doIfUserPressOk);
    }

    private void sendTestPushMsgBtn(View view)
    {
        String title = pushMsgTitleEditTest.getText().toString();
        String body = pushMsgBodyEditText.getText().toString();
        if (title.trim().isEmpty())
        {
            Toast.makeText(SharedData.mainActivity, "הכנס כותרת", Toast.LENGTH_SHORT).show();
            return;
        }
        sendNotificationMsgInRequest();
        ServerRequest serverRequest = new ServerRequest((response) -> Setting.sendNotificationAns(response));
        serverRequest.sendNotificationToYourself(title,body);
    }
}