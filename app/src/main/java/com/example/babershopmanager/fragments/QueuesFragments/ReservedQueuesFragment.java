package com.example.babershopmanager.fragments.QueuesFragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.babershopmanager.sharedDate.DateTimePickerData;
import com.example.babershopmanager.utils.DateHelper;
import com.example.babershopmanager.R;
import com.example.babershopmanager.utils.dataStructures.ReservedQueue;
import com.example.babershopmanager.fragments.dialogFragments.SimpleMethod;
import com.example.babershopmanager.fragments.dialogFragments.AlertDialog;
import com.example.babershopmanager.fragments.dialogFragments.ChooseDateFragment;
import com.example.babershopmanager.fragments.dialogFragments.ChooseTimeFragment;
import com.example.babershopmanager.server.ReservedQueues;
import com.example.babershopmanager.server.ServerRequest;
import com.example.babershopmanager.fragments.QueuesFragment;
import com.example.babershopmanager.sharedDate.SharedData;
import com.example.babershopmanager.sharedDate.QueuesData;

import java.util.Date;
import java.util.GregorianCalendar;


public class ReservedQueuesFragment extends Fragment
{

    private ReservedQueue queueClicked;
    private LinearLayout reservedQueuesLayout;
    private View popUpLayout; //for show queue information
    private TextView popUpText;
    public Button popUpCleanBtn,popUpDeleteBtn,popUpChangeQueueBtn;
    public ProgressBar loadingView;
    private View reservedQueuesFrame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_reserved_queues, container, false);
        loadingView = view.findViewById(R.id.reservedQueuesFragmentLoadingView);
        reservedQueuesFrame = view.findViewById(R.id.reservedQueuesFrame);
        reservedQueuesLayout = view.findViewById(R.id.reservedQueuesScrollViewLayout);
        popUpLayout = view.findViewById(R.id.popUpLayout);
        popUpChangeQueueBtn = view.findViewById(R.id.popUpChangeQueueBtn);
        popUpText  = view.findViewById(R.id.popUpText);
        Button popUpBackBtn = view.findViewById(R.id.popUpBackBtn);
        popUpDeleteBtn = view.findViewById(R.id.popUpDeleteBtn);
        popUpCleanBtn = view.findViewById(R.id.popUpCleanBtn);
        Button popUpCallBtn = view.findViewById(R.id.popUpCallBtn);
        SharedData.reservedQueuesFragment = this;
        popUpBackBtn.setOnClickListener(this::popUpBackBtn);
        popUpCallBtn.setOnClickListener(this::popUpCallBtn);
        popUpDeleteBtn.setOnClickListener(this::popUpDeleteBtn);
        popUpCleanBtn.setOnClickListener(this::popUpCleanBtn);
        popUpChangeQueueBtn.setOnClickListener(this::popUpChangeQueueBtn);
        if (QueuesData.askForReservedQueues == true)
        {
            loadingView.setVisibility(View.VISIBLE);
            ServerRequest serverRequest = new ServerRequest((String response) -> ReservedQueues.getReservedQueuesAns(response));
            serverRequest.getReservedQueues();
        }
        else
            createReservedQueuesViewList();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        popUpBackBtn();
    }

    public void makeVisible()
    {
        reservedQueuesFrame.setVisibility(View.VISIBLE);
    }

    private void popUpCallBtn(View view) //make call to queue owner
    {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + queueClicked.phone));
        startActivity(intent);
    }

    private void popUpChangeQueueBtn(View view)
    {
        ChooseDateFragment.DoOnGetAns doOnGetAnsDatePicker = (int year,int month,int day) ->
        {
            Date date = new GregorianCalendar(year,month,day).getTime();
            DateTimePickerData.data = date;
            ChooseTimeFragment.DoOnGetAns doOnGetAnsTimePicker = (int hour,int min) ->
            {
                DateTimePickerData.min = ChooseTimeFragment.makeMinStr(min);
                DateTimePickerData.hour = ChooseTimeFragment.makeHourStr(hour);
                setPopUpChangeQueuePickersAns();
            };
            DialogFragment timePicker = new ChooseTimeFragment(doOnGetAnsTimePicker);
            timePicker.show(getFragmentManager(), "");
        };
        ChooseDateFragment datePicker = new ChooseDateFragment(doOnGetAnsDatePicker);
        datePicker.show(getFragmentManager(), "");
    }

    private void setPopUpChangeQueuePickersAns()
    {
        String hourStr = DateTimePickerData.hour;
        String minStr = DateTimePickerData.min;
        if (DateHelper.checkIfFutureDate(DateTimePickerData.data,hourStr ,minStr) == false)
            return;
        String newQueueStr = DateHelper.getTimeString(DateTimePickerData.data,hourStr,minStr);
        String alertTitle = "התור שבחרת :\n" + newQueueStr;
        String alertMsg = "(1) שנה והוסף את התור הקודם\n      לרשימת התורים הפנויים\n\n" +
                "(2) שנה ומחק את התור הקודם\n      מרשימת התורים הפנויים\n";
        SimpleMethod doIfOp1 = () ->
        {
            doBeforePopUpServerRequest();
            String newQueue = DateHelper.getTime(DateTimePickerData.data, hourStr, minStr);
            ServerRequest serverRequest = new ServerRequest((String response) -> ReservedQueues.changeReservedQueueAns(response));
            serverRequest.changeReservedQueue(queueClicked.id, queueClicked.getTime(), newQueue,true);
        };
        SimpleMethod doIfOp2 = () ->
        {
            doBeforePopUpServerRequest();
            String newQueue = DateHelper.getTime(DateTimePickerData.data, hourStr, minStr);
            ServerRequest serverRequest = new ServerRequest((String response) -> ReservedQueues.changeReservedQueueAns(response));
            serverRequest.changeReservedQueue(queueClicked.id, queueClicked.getTime(), newQueue,false);
        };
        AlertDialog.showAlertDialog(alertTitle,alertMsg,"1","2",doIfOp1,doIfOp2);
    }

    public void createReservedQueuesViewList()
    {
        loadingView.setVisibility(View.GONE);
        popUpLayout.setVisibility(View.GONE);
        if (QueuesData.reservedQueuesArray == null)
            return;
        reservedQueuesLayout.removeAllViewsInLayout();
        if (QueuesData.reservedQueuesArray.length == 0)
        {
            QueuesFragment.addTextToLayout(reservedQueuesLayout,"אין תורים קבועים",20);
            return;
        }
        int queuesArrayLength = QueuesData.reservedQueuesArray.length;
        String date = QueuesData.reservedQueuesArray[0].getDateAndDayString();
        QueuesFragment.addTextToLayout(reservedQueuesLayout,date,20);
        ReservedQueue prevQueue = QueuesData.reservedQueuesArray[0];
        for (int i = 0; i < queuesArrayLength ; i+= 1)
        {
            ReservedQueue crntQueue = QueuesData.reservedQueuesArray[i];
            if (crntQueue.date.equals(prevQueue.date) == false)
                QueuesFragment.addTextToLayout(reservedQueuesLayout, crntQueue.getDateAndDayString(), 20);
            addBtnToTheLayout(crntQueue.getHourAndNameString(),i);
            prevQueue = crntQueue;
        }
    }

    private void addBtnToTheLayout(String btnText,int btnId)
    {
        Button btn = new Button(SharedData.mainActivity);
        btn.setText(btnText);
        btn.setOnClickListener(this::queueClicked);
        btn.setId(btnId);
        reservedQueuesLayout.addView(btn);
    }

    private void popUpDeleteBtn(View view)
    {
        String alertTitle = "למחוק את התור?";
        String alertMsg = "התור לא יופיע יותר גם בתורים פנויים" ;
        SimpleMethod doIfUserPressOk = () ->
        {
            doBeforePopUpServerRequest();
            ServerRequest serverRequest = new ServerRequest( (String response) ->ReservedQueues.deleteReservedQueueAns(response));
            serverRequest.deleteUserReservedQueue(queueClicked.getTime(),queueClicked.id);
        };
        AlertDialog.showAlertDialog(alertTitle,alertMsg,doIfUserPressOk);
    }

    private void popUpCleanBtn(View view)
    {
        String alertTitle = "לבטל למשתמש את התור?";
        String alertMsg = "התור יחזור להופיע כתור פנוי" ;
        SimpleMethod doIfUserPressOk = () ->
        {
            doBeforePopUpServerRequest();
            String id = queueClicked.id;
            ServerRequest serverRequest = new ServerRequest((String response) -> ReservedQueues.cleanReservedQueueAns(response));
            serverRequest.cleanReservedQueue(queueClicked.getTime(), id);
        };
        AlertDialog.showAlertDialog(alertTitle,alertMsg,doIfUserPressOk);
    }

    public void doBeforePopUpServerRequest()
    {
        loadingView.setVisibility(View.VISIBLE);
        popUpDeleteBtn.setEnabled(false);
        popUpCleanBtn.setEnabled(false);
        popUpChangeQueueBtn.setEnabled(false);
    }

    public void doWhenPopUpServerAns()
    {
        loadingView.setVisibility(View.INVISIBLE);
        popUpCleanBtn.setEnabled(true);
        popUpDeleteBtn.setEnabled(true);
        popUpChangeQueueBtn.setEnabled(true);
    }

    private void popUpBackBtn(View view)
    {
        popUpBackBtn();
    }

    public void popUpBackBtn()
    {
        popUpLayout.setVisibility(View.GONE);
        reservedQueuesFrame.setVisibility(View.VISIBLE);
    }

    private void queueClicked(View view)
    {
        reservedQueuesFrame.setVisibility(View.GONE);
        queueClicked = QueuesData.reservedQueuesArray[view.getId()];
        popUpLayout.setVisibility(View.VISIBLE);
        String text = DateHelper.flipDateString(queueClicked.date) + "\n" + queueClicked.hour + "\n";
        text += queueClicked.name + "\n" + "מספר פלאפון :" + "\n" + queueClicked.phone ;
        popUpText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        popUpText.setText(text);
    }
}
