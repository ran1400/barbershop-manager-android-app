package com.example.barbershopmanager.fragments.SettingFragments;

import androidx.fragment.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barbershopmanager.sharedDate.DateTimePickerData;
import com.example.barbershopmanager.utils.DateHelper;
import com.example.barbershopmanager.R;
import com.example.barbershopmanager.fragments.dialogFragments.SimpleMethod;

import com.example.barbershopmanager.fragments.dialogFragments.AlertDialog;
import com.example.barbershopmanager.fragments.dialogFragments.ChooseDateFragment;
import com.example.barbershopmanager.fragments.dialogFragments.ChooseTimeFragment;
import com.example.barbershopmanager.server.ServerRequest;
import com.example.barbershopmanager.server.Setting;
import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.SettingData;
import com.example.barbershopmanager.utils.dataStructures.User;

import java.util.Date;
import java.util.GregorianCalendar;

public class UserOptionsFragment extends Fragment
{
    private TextView nameText,phoneText;
    private Button callBtn,removeBtn,blockBtn,queueBtn;
    private ProgressBar loadingView;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_options, container, false);
        SharedData.userOptionsFragment = this;
        loadingView = view.findViewById(R.id.userOptionFragmentLoadingView);
        nameText = view.findViewById(R.id.userOptionFragmentName);
        phoneText = view.findViewById(R.id.userOptionFragmentPhone);
        callBtn = view.findViewById(R.id.userOptionFragmentPhoneCallBtn);
        removeBtn = view.findViewById(R.id.userOptionFragmentRemoveBtn);
        blockBtn = view.findViewById(R.id.userOptionFragmentBlockBtn);
        queueBtn = view.findViewById(R.id.userOptionFragmentQueueBtn);
        blockBtn.setOnClickListener(this::blockBtn);
        removeBtn.setOnClickListener(this::removeUserBtn);
        callBtn.setOnClickListener(this::callBtn);
        queueBtn.setOnClickListener(this::queueBtn);
        user = getUser(SettingData.userClickedBtnId);
        nameText.setText(user.name);
        phoneText.setText(user.phone);
        showButtons();
        updateLoadingView();
        return view;
    }


    private void showButtons()
    {
        if(user.blocked)
        {
            blockBtn.setText("בטל חסימת משתמש");
            queueBtn.setVisibility(View.GONE);
        }
        else
        {
            if (user.queue != null) //to user have queue
                queueBtn.setText("שנה תור");
        }
    }

    private User getUser(int index)
    {
        if (index < SettingData.usersWithQueue.length) //is user with queue
            return SettingData.usersWithQueue[index];
        else if (index < SettingData.usersWithQueue.length + SettingData.usersWithoutQueue.length) //is user without queue
            return SettingData.usersWithoutQueue[index - SettingData.usersWithQueue.length];
        // is blocked user
        return SettingData.blockedUsers[index - SettingData.usersWithQueue.length - SettingData.usersWithoutQueue.length];
    }

    public void doBeforeServerRequest()
    {
        SettingData.showUserFragmentInRequest = true;
        loadingView.setVisibility(View.VISIBLE);
        blockBtn.setEnabled(false);
        removeBtn.setEnabled(false);
        if (! user.blocked)
            queueBtn.setEnabled(false);
    }

    public void doWhenGetResponseFromTheServer()
    {
        loadingView.setVisibility(View.INVISIBLE);
        blockBtn.setEnabled(true);
        removeBtn.setEnabled(true);
        if (! user.blocked)
            queueBtn.setEnabled(true);
    }

    public void updateLoadingView()
    {
        if (SettingData.showUserFragmentInRequest)
        {
            doBeforeServerRequest();
            Toast.makeText(SharedData.mainActivity, "לא ניתן לבצע פעולות, המתן לסיום ביצוע הפעולה הקודמת", Toast.LENGTH_SHORT).show();
        }
        else
            doWhenGetResponseFromTheServer();
    }

    private void queueBtn(View view)
    {
        if (user.queue == null) //to the user not have queue
            addQueueBtn();
        else
            changeQueueBtn();
    }

    private void addQueueBtn()
    {
        ChooseDateFragment.DoOnGetAns doOnGetAnsDatePicker = (int year,int month,int day) ->
        {
            Date date = new GregorianCalendar(year,month,day).getTime();
            DateTimePickerData.data = date;
            ChooseTimeFragment.DoOnGetAns doOnGetAnsTimePicker = (int hour, int min) ->
            {
                DateTimePickerData.min = ChooseTimeFragment.makeMinStr(min);
                DateTimePickerData.hour = ChooseTimeFragment.makeHourStr(hour);
                addQueueDatePickerAns();
            };
            DialogFragment timePicker = new ChooseTimeFragment(doOnGetAnsTimePicker);
            timePicker.show(getFragmentManager(), "");
        };
        ChooseDateFragment datePicker = new ChooseDateFragment(doOnGetAnsDatePicker);
        datePicker.show(getFragmentManager(), "");
    }

    private void addQueueDatePickerAns()
    {
        if (DateHelper.checkIfFutureDate(DateTimePickerData.data,DateTimePickerData.hour ,DateTimePickerData.min))  //make toast if false
        {
            String title = "לקבוע את התור?";
            SimpleMethod doIfUserPressOk = () ->
            {
                doBeforeServerRequest();
                String queue = DateHelper.getTime(DateTimePickerData.data, DateTimePickerData.hour, DateTimePickerData.min); //for send to server
                ServerRequest serverRequest = new ServerRequest((String response) -> Setting.addQueueAns(response));
                serverRequest.addReservedQueue(user.mail, queue);
            };
            AlertDialog.showAlertDialog(title, "", doIfUserPressOk);
        }
    }

    private void changeQueueBtn()
    {
        String prevQueueStr = DateHelper.flipDateAndHour(user.queue);
        String title = "למשתמש יש תור ב\n" + prevQueueStr;
        SimpleMethod doIfOp1 = ()->
        {
            ChooseDateFragment.DoOnGetAns doOnGetAnsDatePicker = (int year,int month,int day) ->
            {
                Date date = new GregorianCalendar(year,month,day).getTime();
                DateTimePickerData.data = date;
                ChooseTimeFragment.DoOnGetAns doOnGetAnsTimePicker = (int hour,int min) ->
                {
                    DateTimePickerData.min = ChooseTimeFragment.makeMinStr(min);
                    DateTimePickerData.hour = ChooseTimeFragment.makeHourStr(hour);
                    changeQueueDatePickerAns();
                };
                DialogFragment timePicker = new ChooseTimeFragment(doOnGetAnsTimePicker);
                timePicker.show(getFragmentManager(), "");
            };
            ChooseDateFragment datePicker = new ChooseDateFragment(doOnGetAnsDatePicker);
            datePicker.show(getFragmentManager(), "");
        };
        SimpleMethod doIfOp2 = ()-> deleteQueue();
        AlertDialog.showAlertDialog(title,"","שנה תור","בטל תור",doIfOp1,doIfOp2);
    }

    private void deleteQueue()
    {
        String title = "ביטול תור\n";
        String msg = "(1) בטל את התור והחזר אותו\n      לרשימת התורים הפנויים\n\n" +
                     "(2) מחק את התור\n";
        SimpleMethod doIfOp1 = ()->
        {
            doBeforeServerRequest();
            ServerRequest serverRequest = new ServerRequest((String response) ->Setting.cleanQueueAns(response));
            serverRequest.cleanReservedQueue(user.queue,user.mail);
        };
        SimpleMethod doIfOp2 = ()->
        {
            doBeforeServerRequest();
            ServerRequest serverRequest = new ServerRequest((String response) ->Setting.deleteQueueAns(response));
            serverRequest.deleteReservedQueueByMail(user.queue,user.mail);
        };
        AlertDialog.showAlertDialog(title,msg,"1","2",doIfOp1,doIfOp2);
    }

    public void changeQueueDatePickerAns()
    {
        if (DateHelper.checkIfFutureDate(DateTimePickerData.data,DateTimePickerData.hour ,DateTimePickerData.min)) //make toast if false
        {
            String newQueueStr = DateHelper.getTimeString(DateTimePickerData.data,DateTimePickerData.hour,DateTimePickerData.min);
            String alertTitle = "התור שבחרת :\n" + newQueueStr;
            String alertMsg = "(1) שנה והוסף את התור הקודם\n      לרשימת התורים הפנויים\n\n" +
                    "(2) שנה ומחק את התור הקודם\n      מרשימת התורים הפנויים\n";
            SimpleMethod doIfOp1 = () ->
            {
                doBeforeServerRequest();
                String newQueue = DateHelper.getTime(DateTimePickerData.data,DateTimePickerData.hour,DateTimePickerData.min);
                ServerRequest serverRequest = new ServerRequest((String response) -> Setting.changeQueueAns(response));
                serverRequest.changeReservedQueue(user.mail, user.queue, newQueue, true);
            };
            SimpleMethod doIfOp2 = () ->
            {
                doBeforeServerRequest();
                String newQueue = DateHelper.getTime(DateTimePickerData.data,DateTimePickerData.hour,DateTimePickerData.min);
                ServerRequest serverRequest = new ServerRequest((String response) -> Setting.changeQueueAns(response));
                serverRequest.changeReservedQueue(user.mail, user.queue, newQueue, false);
            };
            AlertDialog.showAlertDialog(alertTitle, alertMsg, "1", "2", doIfOp1, doIfOp2);
        }
    }

    private void callBtn(View view)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + user.phone));
        startActivity(intent);
    }

    private void unblockUserBtn()
    {
        String alertTitle = "לבטל את חסימת המשתמש?";
        SimpleMethod doIfUserPressOk = () ->
        {
            doBeforeServerRequest();
            ServerRequest serverRequest = new ServerRequest((String response) ->Setting.unblockUserAns(response));
            serverRequest.unblockUser(user.mail);
        };
        AlertDialog.showAlertDialog(alertTitle,"",doIfUserPressOk);
    }

    private void blockUserBtn()
    {
        String alertTitle = "לחסום את המשתמש?";
        SimpleMethod doIfUserPressOk = () ->
        {
            boolean haveQueue = user.queue != null;
            doBeforeServerRequest();
            ServerRequest serverRequest = new ServerRequest((String response) ->Setting.blockUserAns(response,haveQueue));
            serverRequest.blockUser(user.mail);
        };
        String alertMsg;
        if (user.queue != null) // to user have queue
            alertMsg = "התור של המשתמש יעבור לרשימת התורים הפנויים" ;
        else
            alertMsg = "";
        AlertDialog.showAlertDialog(alertTitle,alertMsg,doIfUserPressOk);
    }

    private void blockBtn(View view)
    {
        if (user.blocked)
            unblockUserBtn();
        else
            blockUserBtn();
    }

    private void removeUserBtn(View view)
    {
        String alertTitle = "למחוק את המשתמש?";
        String alertMsg = "המחיקה כוללת תורים עתידיים של המשתמש";
        SimpleMethod doIfUserPressOk = () ->
        {
            doBeforeServerRequest();
            boolean haveQueue = user.queue != null;
            ServerRequest serverRequest = new ServerRequest((String response) ->Setting.removeUserAns(response,haveQueue));
            serverRequest.removeUser(user.mail,user.name);
        };
        AlertDialog.showAlertDialog(alertTitle,alertMsg,doIfUserPressOk);
    }
}