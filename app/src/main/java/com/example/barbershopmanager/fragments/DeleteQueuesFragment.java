package com.example.barbershopmanager.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.barbershopmanager.R;
import com.example.barbershopmanager.utils.DateHelper;
import com.example.barbershopmanager.fragments.dialogFragments.SimpleMethod;
import com.example.barbershopmanager.fragments.dialogFragments.AlertDialog;
import com.example.barbershopmanager.fragments.dialogFragments.ChooseDateFragment;
import com.example.barbershopmanager.fragments.dialogFragments.ChooseTimeFragment;
import com.example.barbershopmanager.server.DeleteQueues;
import com.example.barbershopmanager.server.ServerRequest;
import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.DeleteQueuesData;

import java.util.Date;
import java.util.GregorianCalendar;


public class DeleteQueuesFragment extends Fragment
{

    private Button deleteQueuesBtn,deleteQueueBtn;
    private EditText deleteQueuesFromEditText,deleteQueuesToEditText;
    private EditText deleteQueueDateEditText,deleteQueueHourEditText;
    private CheckBox deleteEmptyQueuesCheckBox,deleteReservedQueuesCheckBox;
    private CheckBox deleteEmptyQueueCheckBox,deleteReservedQueueCheckBox;
    public ProgressBar deleteQueueLoadingView , deleteQueuesLoadingView;
    private Button switchToDeleteQueueWindowsBtn,switchToDeleteQueuesWindowsBtn;
    private View deleteQueueWindows,deleteQueuesWindows;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_delete_queues, container, false);
        SharedData.deleteQueuesFragment = this;
        deleteEmptyQueuesCheckBox = view.findViewById(R.id.deleteEmptyQueuesCheckBox);
        deleteReservedQueuesCheckBox = view.findViewById(R.id.deleteReservedQueuesCheckBox);
        deleteEmptyQueueCheckBox = view.findViewById(R.id.deleteEmptyQueueCheckBox);
        deleteReservedQueueCheckBox = view.findViewById(R.id.deleteReservedQueueCheckBox);
        deleteQueuesFromEditText = view.findViewById(R.id.deleteQueuesFromEditText);
        deleteQueuesToEditText = view.findViewById(R.id.deleteQueuesToEditText);
        deleteQueueDateEditText = view.findViewById(R.id.deleteQueueDateEditText);
        deleteQueueHourEditText = view.findViewById(R.id.deleteQueueHourEditText);
        deleteQueuesBtn = view.findViewById(R.id.deleteQueuesBtn);
        deleteQueueBtn = view.findViewById(R.id.deleteQueueBtn);
        switchToDeleteQueuesWindowsBtn = view.findViewById(R.id.switchToDeleteQueuesWindow);
        switchToDeleteQueueWindowsBtn = view.findViewById(R.id.switchToDeleteQueueWindow);
        deleteQueueLoadingView  = view.findViewById(R.id.deleteQueueLoadingView);
        deleteQueuesLoadingView = view.findViewById(R.id.deleteQueuesLoadingView);
        deleteQueueWindows = view.findViewById(R.id.deleteQueueWindow);
        deleteQueuesWindows = view.findViewById(R.id.deleteQueuesWindow);
        deleteQueueDateEditText.setOnClickListener(this::deleteQueueDateEditTextClicked);
        deleteQueueHourEditText.setOnClickListener(this::deleteQueueHourEditTextClicked);
        deleteQueuesFromEditText.setOnClickListener(this::deleteQueuesFromEditTextClicked);
        deleteQueuesToEditText.setOnClickListener(this::deleteQueuesToEditTextClicked);
        deleteEmptyQueueCheckBox.setOnCheckedChangeListener(this::deleteQueueCheckBoxListener);
        deleteReservedQueueCheckBox.setOnCheckedChangeListener(this::deleteQueueCheckBoxListener);
        deleteEmptyQueuesCheckBox.setOnCheckedChangeListener(this::deleteQueuesCheckBoxListener);
        deleteReservedQueuesCheckBox.setOnCheckedChangeListener(this::deleteQueuesCheckBoxListener);
        switchToDeleteQueueWindowsBtn.setOnClickListener(this::switchToDeleteQueueWindow);
        switchToDeleteQueuesWindowsBtn.setOnClickListener(this::switchToDeleteQueuesWindow);
        deleteQueuesBtn.setOnClickListener(this::deleteQueues);
        deleteQueueBtn.setOnClickListener(this::deleteQueue);
        deleteQueueFillEditTexts();
        deleteQueuesFillEditTexts();
        deleteQueuesFillCb();
        deleteQueueFillCb();
        if (DeleteQueuesData.deleteQueueInRequest)
        {
            deleteQueueBtn.setEnabled(false);
            deleteQueueLoadingView.setVisibility(View.VISIBLE);
        }
        if (DeleteQueuesData.deleteQueuesInRequest)
        {
            deleteQueuesBtn.setEnabled(false);
            deleteQueuesLoadingView.setVisibility(View.VISIBLE);
        }
        showCrntWindow();
        return view;
    }

    public void showCrntWindow()
    {
        if (DeleteQueuesData.deleteQueueWindowsIsVisible())
            switchToDeleteQueueWindow();
    }

    private void deleteQueuesFillCb() //set the check boxes of delete queues window
    {
        deleteEmptyQueuesCheckBox.setChecked(DeleteQueuesData.cbDeleteEmptyQueues);
        deleteReservedQueuesCheckBox.setChecked(DeleteQueuesData.cbDeleteReservedQueues);
    }

    private void deleteQueueFillCb() //set the check boxes of delete queue window
    {
        deleteEmptyQueueCheckBox.setChecked(DeleteQueuesData.cbDeleteEmptyQueue);
        deleteReservedQueueCheckBox.setChecked(DeleteQueuesData.cbDeleteReservedQueue);
    }

    private void deleteQueueFillEditTexts()
    {
        if (DeleteQueuesData.deleteQueueDate != null)
            deleteQueueDateEditText.setText(DateHelper.fromDateToStr(DeleteQueuesData.deleteQueueDate));
        else
            deleteQueueDateEditText.setText("");
        if (DeleteQueuesData.deleteQueueHour != null)
            deleteQueueHourEditText.setText(DateHelper.fromHourToStr(DeleteQueuesData.deleteQueueHour));
        else
            deleteQueueDateEditText.setText("");
    }

    private void deleteQueuesFillEditTexts()
    {
        if (DeleteQueuesData.deleteQueuesFromDate != null && DeleteQueuesData.deleteQueuesFromHour != null)
            deleteQueuesFromEditText.setText(DateHelper.fromTimeToStr(DeleteQueuesData.deleteQueuesFromDate, DeleteQueuesData.deleteQueuesFromHour));
        else
            deleteQueuesFromEditText.setText("");
        if (DeleteQueuesData.deleteQueuesToDate != null && DeleteQueuesData.deleteQueuesToHour != null)
            deleteQueuesToEditText.setText(DateHelper.fromTimeToStr(DeleteQueuesData.deleteQueuesToDate, DeleteQueuesData.deleteQueuesToHour));
        else
            deleteQueuesToEditText.setText("");
    }

    private void switchToDeleteQueueWindow(View view)
    {
        switchToDeleteQueueWindow();
    }

    private void switchToDeleteQueueWindow()
    {
        SharedData.mainActivity.changeActivityTitle("מחיקת תור אחד");
        deleteQueuesWindows.setVisibility(View.GONE);
        deleteQueueWindows.setVisibility(View.VISIBLE);
        switchToDeleteQueueWindowsBtn.setVisibility(View.GONE);
        switchToDeleteQueuesWindowsBtn.setVisibility(View.VISIBLE);
        DeleteQueuesData.setCrntWindowToDeleteQueue();
    }

    private void switchToDeleteQueuesWindow(View view)
    {
        switchToDeleteQueuesWindow();
    }

    public void switchToDeleteQueuesWindow()
    {
        SharedData.mainActivity.changeActivityTitle("מחיקת תורים");
        deleteQueueWindows.setVisibility(View.GONE);
        deleteQueuesWindows.setVisibility(View.VISIBLE);
        switchToDeleteQueuesWindowsBtn.setVisibility(View.GONE);
        switchToDeleteQueueWindowsBtn.setVisibility(View.VISIBLE);
        DeleteQueuesData.setCrntWindowToDeleteQueues();
    }

    public void deleteQueuesNotInRequest()
    {
        deleteQueuesLoadingView.setVisibility(View.GONE);
        deleteQueuesBtn.setEnabled(true);
        DeleteQueuesData.deleteQueuesInRequest = false;
    }

    public void deleteQueueNotInRequest()
    {
        deleteQueueLoadingView.setVisibility(View.GONE);
        deleteQueueBtn.setEnabled(true);
        DeleteQueuesData.deleteQueueInRequest = false;
    }

    private void deleteQueuesFromEditTextClicked(View view)
    {
        ChooseDateFragment.DoOnGetAns doOnGetAnsDatePicker = (int year,int month,int day) ->
        {
            Date date = new GregorianCalendar(year,month,day).getTime();
            DeleteQueuesData.deleteQueuesFromDate = date;
            DeleteQueuesData.deleteQueuesFromStr = day + "." + (month + 1) + "." + year;
            SharedData.deleteQueuesFragment.deleteQueuesFromEditText.setText(""); //delete last choice
            DeleteQueuesData.deleteQueuesFromHour = null;  //delete last choice
            ChooseTimeFragment.DoOnGetAns doOnGetAnsHourPicker = (int hour , int min) ->
            {
                String showHourStr = ChooseTimeFragment.makeHourStr(hour,min);
                String text =  DeleteQueuesData.deleteQueuesFromStr + "  - " + showHourStr  ;
                SharedData.deleteQueuesFragment.deleteQueuesFromEditText.setText(text);
                DeleteQueuesData.deleteQueuesFromHour = hour * 100 + min;
            };
            ChooseTimeFragment hourPicker = new ChooseTimeFragment(doOnGetAnsHourPicker);
            hourPicker.show(this);
        };
        ChooseDateFragment datePicker = new ChooseDateFragment(doOnGetAnsDatePicker);
        datePicker.show(this);
    }

    private void deleteQueuesToEditTextClicked(View view)
    {
        ChooseDateFragment.DoOnGetAns doOnGetAnsDatePicker = (int year,int month,int day) ->
        {
            Date date = new GregorianCalendar(year,month,day).getTime();
            DeleteQueuesData.deleteQueuesToDate = date;
            DeleteQueuesData.deleteQueuesToStr = day + "." + (month + 1) + "." + year;
            SharedData.deleteQueuesFragment.deleteQueuesToEditText.setText(""); //delete last choice
            DeleteQueuesData.deleteQueuesToHour = null;  //delete last choice
            ChooseTimeFragment.DoOnGetAns doOnGetAnsHourPicker = (int hour , int min) ->
            {
                String showHourStr = ChooseTimeFragment.makeHourStr(hour,min);
                String txt = DeleteQueuesData.deleteQueuesToStr + "  - " + showHourStr;
                SharedData.deleteQueuesFragment.deleteQueuesToEditText.setText(txt);
                DeleteQueuesData.deleteQueuesToHour = hour * 100 + min;
            };
            ChooseTimeFragment hourPicker = new ChooseTimeFragment(doOnGetAnsHourPicker);
            hourPicker.show(this);
        };
        ChooseDateFragment datePicker = new ChooseDateFragment(doOnGetAnsDatePicker);
        datePicker.show(this);
    }

    private void deleteQueueDateEditTextClicked(View view)
    {
        ChooseDateFragment.DoOnGetAns doOnGetAns = (int year,int month,int day) ->
        {
            Date date = new GregorianCalendar(year,month,day).getTime();
            SharedData.deleteQueuesFragment.deleteQueueDateEditText.setText(day + "." + (month + 1) + "." + year);
            DeleteQueuesData.deleteQueueDate = date;
        };
        ChooseDateFragment datePicker = new ChooseDateFragment(doOnGetAns);
        datePicker.show(this);
    }

    private void deleteQueueHourEditTextClicked(View view)
    {
        ChooseTimeFragment.DoOnGetAns doOnGetAns = (int hour,int min) ->
        {
            String showHourStr = ChooseTimeFragment.makeHourStr(hour,min);
            SharedData.deleteQueuesFragment.deleteQueueHourEditText.setText(showHourStr);
            DeleteQueuesData.deleteQueueHour = hour * 100 + min;
        };
        ChooseTimeFragment hourPicker = new ChooseTimeFragment(doOnGetAns);
        hourPicker.show(this);
    }

    private void deleteQueues(View view)
    {
        if (DeleteQueuesData.deleteQueuesFromHour == null)
        {
            Toast.makeText(SharedData.mainActivity,"בחר מאיזה תאריך ושעה",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (DeleteQueuesData.deleteQueuesToHour == null)
        {
            Toast.makeText(SharedData.mainActivity,"בחר עד איזה תאריך ושעה",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(notLess(DeleteQueuesData.deleteQueuesFromDate, DeleteQueuesData.deleteQueuesFromHour,
                DeleteQueuesData.deleteQueuesToDate, DeleteQueuesData.deleteQueuesToHour) == false)
        {
            Toast.makeText(SharedData.mainActivity,"זמן ההתחלה מאוחר מזמן הסיום",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        boolean emptyQueuesCheck = deleteEmptyQueuesCheckBox.isChecked();
        boolean reservedQueuesCheck = deleteReservedQueuesCheckBox.isChecked();
        if (emptyQueuesCheck && reservedQueuesCheck)
            deleteEmptyAndReservedQueues();
        else if (emptyQueuesCheck)  //only emptyQueuesCheck
            deleteEmptyQueues();
        else // (only reservedQueuesCheck)
            deleteReservedQueues();
    }

    private void deleteReservedQueues()
    {
        String alertTitle = "למחוק את התורים?";
        SimpleMethod doIfUserPressOk = () ->
        {
            doBeforeDeleteQueuesRequest();
            String fromDate = DateHelper.getTime(DeleteQueuesData.deleteQueuesFromDate, DeleteQueuesData.deleteQueuesFromHour);
            String toDate = DateHelper.getTime(DeleteQueuesData.deleteQueuesToDate, DeleteQueuesData.deleteQueuesToHour);
            ServerRequest serverRequest = new ServerRequest(DeleteQueues::deleteReservedQueuesAns);
            serverRequest.deleteReservedQueues(fromDate, toDate);
        };
        AlertDialog.showAlertDialog(alertTitle,"",doIfUserPressOk);
    }

    private void deleteEmptyAndReservedQueues()
    {
        String alertTitle = "למחוק את התורים?";
        SimpleMethod doIfUserPressOk = () ->
        {
            doBeforeDeleteQueuesRequest();
            String fromDate = DateHelper.getTime(DeleteQueuesData.deleteQueuesFromDate,DeleteQueuesData.deleteQueuesFromHour);
            String toDate = DateHelper.getTime(DeleteQueuesData.deleteQueuesToDate,DeleteQueuesData.deleteQueuesToHour);
            ServerRequest serverRequest = new ServerRequest(DeleteQueues::deleteEmptyAndReservedQueuesAns);
            serverRequest.deleteEmptyAndReservedQueues(fromDate, toDate);
        };
        AlertDialog.showAlertDialog(alertTitle,"",doIfUserPressOk);
    }

    private void deleteEmptyQueues()
    {
        String alertTitle = "למחוק את התורים?";
        SimpleMethod doIfUserPressOk = () ->
        {
            doBeforeDeleteQueuesRequest();
            String fromDate = DateHelper.getTime(DeleteQueuesData.deleteQueuesFromDate,DeleteQueuesData.deleteQueuesFromHour);
            String toDate = DateHelper.getTime(DeleteQueuesData.deleteQueuesToDate,DeleteQueuesData.deleteQueuesToHour);
            ServerRequest serverRequest = new ServerRequest(DeleteQueues::deleteEmptyQueuesAns);
            serverRequest.deleteEmptyQueues(fromDate, toDate);
        };
        AlertDialog.showAlertDialog(alertTitle,"",doIfUserPressOk);
    }

    public void doBeforeDeleteQueuesRequest()
    {
        deleteQueuesLoadingView.setVisibility(View.VISIBLE);
        deleteQueuesBtn.setEnabled(false);
        DeleteQueuesData.deleteQueuesInRequest = true;
    }

    private void deleteQueue(View view)
    {
        if (DeleteQueuesData.deleteQueueHour == null)
        {
            Toast.makeText(SharedData.mainActivity,"בחר תאריך ושעה",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (DateHelper.checkIfFutureDate(DeleteQueuesData.deleteQueueDate,DeleteQueuesData.deleteQueueHour) == false)
            return;
        boolean emptyQueueCheck = deleteEmptyQueueCheckBox.isChecked();
        boolean reservedQueueCheck = deleteReservedQueueCheckBox.isChecked();
        if (emptyQueueCheck && reservedQueueCheck)
            deleteEmptyOrReservedQueue();
        else if (emptyQueueCheck)  //only emptyQueueCheck
            deleteEmptyQueue();
        else // (only reservedQueueCheck)
            deleteReservedQueue();
    }

    private void deleteReservedQueue()
    {
        String alertTitle = "למחוק את התור?";
        SimpleMethod doIfUserPressOk = () ->
        {
            doBeforeDeleteQueueRequest();
            String date = DateHelper.getTime(DeleteQueuesData.deleteQueueDate,DeleteQueuesData.deleteQueueHour);
            ServerRequest serverRequest = new ServerRequest(DeleteQueues::deleteReservedQueueAns);
            serverRequest.deleteReservedQueueByTime(date);
        };
        AlertDialog.showAlertDialog(alertTitle,"",doIfUserPressOk);
    }

    private void deleteEmptyOrReservedQueue()
    {
        String alertTitle = "למחוק את התור?";
        SimpleMethod doIfUserPressOk = () ->
        {
            doBeforeDeleteQueueRequest();
            String date = DateHelper.getTime(DeleteQueuesData.deleteQueueDate,DeleteQueuesData.deleteQueueHour);
            ServerRequest serverRequest = new ServerRequest(DeleteQueues::deleteEmptyOrReservedQueueAns);
            serverRequest.deleteEmptyOrReservedQueue(date);
        };
        AlertDialog.showAlertDialog(alertTitle,"",doIfUserPressOk);
    }

    private void deleteEmptyQueue()
    {
        String alertTitle = "למחוק את התור?";
        SimpleMethod doIfUserPressOk = () ->
        {
            doBeforeDeleteQueueRequest();
            String date = DateHelper.getTime(DeleteQueuesData.deleteQueueDate,DeleteQueuesData.deleteQueueHour);
            ServerRequest serverRequest = new ServerRequest(DeleteQueues::deleteEmptyQueueAns);
            serverRequest.deleteEmptyQueue(date);
        };
        AlertDialog.showAlertDialog(alertTitle,"",doIfUserPressOk);
    }

    private void doBeforeDeleteQueueRequest()
    {
        deleteQueueLoadingView.setVisibility(View.VISIBLE);
        deleteQueueBtn.setEnabled(false);
        DeleteQueuesData.deleteQueueInRequest = true;
    }


    private boolean notLess(Date firstDate, int firstHour, Date secondDate, int secondHour) //first date and time not early from the second
    {
        int datesCmp = firstDate.compareTo(secondDate);
        if (datesCmp == 1)
            return false;
        else if (datesCmp == -1)
            return true;
        if (firstHour > secondHour)
            return false;
        else
            return true;
    }

    public void deleteQueuesCheckBoxListener(CompoundButton buttonView, boolean isChecked)
    {
        if (buttonView.getId() == R.id.deleteEmptyQueuesCheckBox)
            DeleteQueuesData.cbDeleteEmptyQueues = isChecked;
        else //(buttonView.getId() == R.id.deleteEmptyQueuesCheckBox)
            DeleteQueuesData.cbDeleteReservedQueues = isChecked;
        if ( (deleteEmptyQueuesCheckBox.isChecked() == false) && (deleteReservedQueuesCheckBox.isChecked() == false) )
            deleteQueuesBtn.setEnabled(false);
        else
            deleteQueuesBtn.setEnabled(true);
    }

    public void deleteQueueCheckBoxListener(CompoundButton buttonView, boolean isChecked)
    {
        if (buttonView.getId() == R.id.deleteEmptyQueueCheckBox)
            DeleteQueuesData.cbDeleteEmptyQueue = isChecked;
        else //(buttonView.getId() == R.id.deleteEmptyQueueCheckBox)
            DeleteQueuesData.cbDeleteReservedQueue = isChecked;
        if ( (deleteEmptyQueueCheckBox.isChecked() == false) && (deleteReservedQueueCheckBox.isChecked() == false) )
            deleteQueueBtn.setEnabled(false);
        else
            deleteQueueBtn.setEnabled(true);
    }
}