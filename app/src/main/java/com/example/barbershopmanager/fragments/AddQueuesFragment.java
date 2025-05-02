package com.example.barbershopmanager.fragments;

import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.barbershopmanager.R;
import com.example.barbershopmanager.utils.DateHelper;
import com.example.barbershopmanager.fragments.dialogFragments.SimpleMethod;
import com.example.barbershopmanager.fragments.dialogFragments.AlertDialog;
import com.example.barbershopmanager.fragments.dialogFragments.ChooseDateFragment;
import com.example.barbershopmanager.fragments.dialogFragments.ChooseTimeFragment;
import com.example.barbershopmanager.server.AddEmptyQueues;
import com.example.barbershopmanager.server.ServerRequest;
import com.example.barbershopmanager.sharedDate.AddQueuesData;
import com.example.barbershopmanager.sharedDate.SharedData;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddQueuesFragment extends Fragment
{
    private EditText addQueuesDateStartEditText,addQueuesHourStartEditText;
    private EditText addQueuesDateEndEditText,addQueuesHourEndEditText;
    private EditText addQueueDateEditText,addQueueHourEditText;
    private EditText spaceBetweenQueuesEditText;
    private ToggleButton tbDaysArray[] =  new ToggleButton[7]; //toggle button of days
    private Button addQueuesBtn,addQueueBtn;
    private Button switchToAddQueueWindowsBtn,switchToAddQueuesWindowsBtn;
    private ProgressBar addQueueLoadingView , addQueuesLoadingView;
    private View addQueueWindows,addQueuesWindows;
    private String queueDaysString;

    @SuppressLint("ClickableViewAccessibility") //for the option to setOnClickListener of editText
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_add_queues, container, false);
        SharedData.addQueuesFragment = this;
        tbDaysArray[0] = view.findViewById(R.id.tb1);
        tbDaysArray[1] = view.findViewById(R.id.tb2);
        tbDaysArray[2] = view.findViewById(R.id.tb3);
        tbDaysArray[3] = view.findViewById(R.id.tb4);
        tbDaysArray[4] = view.findViewById(R.id.tb5);
        tbDaysArray[5] = view.findViewById(R.id.tb6);
        tbDaysArray[6] = view.findViewById(R.id.tb7);
        for (int i =0 ; i < 7 ; i++)
            tbDaysArray[i].setOnCheckedChangeListener(this::TogglesButtonsChangedListener);
        addQueuesDateStartEditText = view.findViewById(R.id.addQueuesDateStartEditText);
        addQueuesHourStartEditText = view.findViewById(R.id.addQueuesHourStartEditText);
        addQueuesDateEndEditText = view.findViewById(R.id.addQueuesDateEndEditText);
        addQueuesHourEndEditText = view.findViewById(R.id.addQueuesHourEndEditText);
        addQueueDateEditText = view.findViewById(R.id.addQueueDateEditText);
        addQueueHourEditText = view.findViewById(R.id.addQueueHourEditText);
        spaceBetweenQueuesEditText = view.findViewById(R.id.queueSpaceEditText);
        addQueuesBtn = view.findViewById(R.id.addQueuesBtn);
        addQueueBtn = view.findViewById(R.id.addQueueBtn);
        switchToAddQueuesWindowsBtn = view.findViewById(R.id.switchToAddQueuesWindow);
        switchToAddQueueWindowsBtn = view.findViewById(R.id.switchToAddQueueWindow);
        addQueueLoadingView  = view.findViewById(R.id.addQueueLoadingView);
        addQueuesLoadingView = view.findViewById(R.id.addQueuesLoadingView);
        addQueueWindows = view.findViewById(R.id.addQueueWindow);
        addQueuesWindows = view.findViewById(R.id.addQueuesWindow);
        spaceBetweenQueuesEditText.setOnTouchListener(this::SpaceEditTextTouchListener);
        addQueuesDateStartEditText.setOnClickListener(this::addQueuesDateStartEditTextClicked);
        addQueuesDateEndEditText.setOnClickListener(this::addQueuesDateEndEditTextClicked);
        addQueuesHourStartEditText.setOnClickListener(this::addQueuesHourStartEditTextClicked);
        addQueuesHourEndEditText.setOnClickListener(this::addQueuesHourEndEditTextClicked);
        addQueueHourEditText.setOnClickListener(this::addQueueHourEditTextClicked);
        addQueueDateEditText.setOnClickListener(this::addQueueDateEditTextClicked);
        switchToAddQueuesWindowsBtn.setOnClickListener(this::switchToAddQueuesWindow);
        switchToAddQueueWindowsBtn.setOnClickListener(this::switchToAddQueueWindow);
        addQueueBtn.setOnClickListener(this::addQueue);
        addQueuesBtn.setOnClickListener(this::addQueues);
        addQueueFillEditTexts();
        addQueuesFillEditTexts();
        fillTbDaysArray();
        if (AddQueuesData.addQueuesInRequest)
        {
            addQueuesBtn.setEnabled(false);
            addQueuesLoadingView.setVisibility(View.VISIBLE);
        }
        if (AddQueuesData.addQueueInRequest)
        {
            addQueueBtn.setEnabled(false);
            addQueueLoadingView.setVisibility(View.VISIBLE);
        }
        showCrntWindow(); // add queue or addQueues windows
        return view;
    }

    public void showCrntWindow()
    {
        if (AddQueuesData.addQueueWindowsIsVisible())
            switchToAddQueueWindow();
    }

    public boolean SpaceEditTextTouchListener(View view, MotionEvent motionEvent)
    {
        spaceBetweenQueuesEditText.setText("");
        return false;
    }

    private void fillTbDaysArray()
    {
        for (int i = 0 ; i < 7 ; i++)
            tbDaysArray[i].setChecked(AddQueuesData .daysTbState[i]);
    }

    public void TogglesButtonsChangedListener(CompoundButton compoundButton, boolean bool)
    {
        switch (compoundButton.getId())
        {
            case R.id.tb1 :
                AddQueuesData.daysTbState[0] = bool;
                break;
            case R.id.tb2 :
                AddQueuesData.daysTbState[1] = bool;
                break;
            case R.id.tb3 :
                AddQueuesData.daysTbState[2] = bool;
                break;
            case R.id.tb4 :
                AddQueuesData.daysTbState[3] = bool;
                break;
            case R.id.tb5 :
                AddQueuesData.daysTbState[4] = bool;
                break;
            case R.id.tb6 :
                AddQueuesData.daysTbState[5] = bool;
                break;
            case R.id.tb7 :
                AddQueuesData.daysTbState[6] = bool;
                break;
        }
    }

    private void addQueueFillEditTexts()
    {
        if (AddQueuesData.addQueueDate != null)
            addQueueDateEditText.setText(DateHelper.fromDateToStr(AddQueuesData.addQueueDate));
        else
            addQueueDateEditText.setText("");
        if (AddQueuesData.addQueueHour != null)
            addQueueHourEditText.setText(DateHelper.fromHourToStr(AddQueuesData.addQueueHour));
        else
            addQueueHourEditText.setText("");
    }

    private void addQueuesFillEditTexts()
    {
        if (AddQueuesData.addQueuesDateStart != null)
            addQueuesDateStartEditText.setText(DateHelper.fromDateToStr(AddQueuesData.addQueuesDateStart));
        else
            addQueuesDateStartEditText.setText("");
        if (AddQueuesData.addQueuesHourStart != null)
            addQueuesHourStartEditText.setText(DateHelper.fromHourToStr(AddQueuesData.addQueuesHourStart));
        else
            addQueuesHourStartEditText.setText("");
        if (AddQueuesData.addQueuesDateEnd != null)
            addQueuesDateEndEditText.setText(DateHelper.fromDateToStr(AddQueuesData.addQueuesDateEnd));
        else
            addQueuesDateEndEditText.setText("");
        if (AddQueuesData.addQueuesHourEnd != null)
            addQueuesHourEndEditText.setText(DateHelper.fromHourToStr(AddQueuesData.addQueuesHourEnd));
        else
            addQueuesHourEndEditText.setText("");
    }


    public void switchToAddQueueWindow(View view)
    {
        switchToAddQueueWindow();
    }

    public void switchToAddQueueWindow()
    {
        SharedData.mainActivity.changeActivityTitle("הוספת תור אחד");
        addQueuesWindows.setVisibility(View.GONE);
        addQueueWindows.setVisibility(View.VISIBLE);
        switchToAddQueueWindowsBtn.setVisibility(View.GONE);
        switchToAddQueuesWindowsBtn.setVisibility(View.VISIBLE);
        AddQueuesData.setCrntWindowToAddQueue();
    }

    public void switchToAddQueuesWindow(View view)
    {
        switchToAddQueuesWindow();
    }

    public void switchToAddQueuesWindow()
    {
        SharedData.mainActivity.changeActivityTitle("הוספת תורים");
        addQueueWindows.setVisibility(View.GONE);
        addQueuesWindows.setVisibility(View.VISIBLE);
        switchToAddQueuesWindowsBtn.setVisibility(View.GONE);
        switchToAddQueueWindowsBtn.setVisibility(View.VISIBLE);
        AddQueuesData.setCrntWindowToAddQueues();
    }

    private void addQueuesDateStartEditTextClicked(View view)
    {
        ChooseDateFragment.DoOnGetAns doOnGetAns = (int year,int month,int day) ->
        {
            Date date = new GregorianCalendar(year,month,day).getTime();
            SharedData.addQueuesFragment.addQueuesDateStartEditText.setText(day + "." + (month + 1) + "." + year);
            AddQueuesData.addQueuesDateStart = date;
        };
        ChooseDateFragment datePicker = new ChooseDateFragment(doOnGetAns);
        datePicker.show(this);
    }

    private void addQueuesHourStartEditTextClicked(View view)
    {
        ChooseTimeFragment.DoOnGetAns doOnGetAns = (int hour,int min) ->
        {
            String showHourStr = ChooseTimeFragment.makeHourStr(hour,min);
            SharedData.addQueuesFragment.addQueuesHourStartEditText.setText(showHourStr);
            AddQueuesData.addQueuesHourStart = hour * 100 + min;
        };
        ChooseTimeFragment hourPicker = new ChooseTimeFragment(doOnGetAns);
        hourPicker.show(this);
    }

    private void addQueuesDateEndEditTextClicked(View view)
    {
        ChooseDateFragment.DoOnGetAns doOnGetAns = (int year,int month,int day) ->
        {
            Date date = new GregorianCalendar(year,month,day).getTime();
            SharedData.addQueuesFragment.addQueuesDateEndEditText.setText(day + "." + (month + 1) + "." + year);
            AddQueuesData.addQueuesDateEnd = date;
        };
        ChooseDateFragment datePicker = new ChooseDateFragment(doOnGetAns);
        datePicker.show(this);
    }

    private void addQueuesHourEndEditTextClicked(View view)
    {
        ChooseTimeFragment.DoOnGetAns doOnGetAns = (int hour,int min) ->
        {
            String showHourStr = ChooseTimeFragment.makeHourStr(hour,min);
            SharedData.addQueuesFragment.addQueuesHourEndEditText.setText(showHourStr);
            AddQueuesData.addQueuesHourEnd =  hour * 100 + min;
        };
        ChooseTimeFragment hourPicker = new ChooseTimeFragment(doOnGetAns);
        hourPicker.show(this);
    }

    private void addQueueHourEditTextClicked(View view)
    {
        ChooseTimeFragment.DoOnGetAns doOnGetAns = (int hour,int min) ->
        {
            String showHourStr = ChooseTimeFragment.makeHourStr(hour,min);
            SharedData.addQueuesFragment.addQueueHourEditText.setText(showHourStr);
            AddQueuesData.addQueueHour = hour * 100 + min;
        };
        ChooseTimeFragment hourPicker = new ChooseTimeFragment(doOnGetAns);
        hourPicker.show(this);
    }

    private void addQueueDateEditTextClicked(View view)
    {
        ChooseDateFragment.DoOnGetAns doOnGetAns = (int year,int month,int day) ->
        {
            Date date = new GregorianCalendar(year,month,day).getTime();
            SharedData.addQueuesFragment.addQueueDateEditText.setText(day + "." + (month + 1) + "." + year);
            AddQueuesData.addQueueDate = date;
        };
        ChooseDateFragment datePicker = new ChooseDateFragment(doOnGetAns);
        datePicker.show(this);
    }


    public void addQueue(View view)
    {
        if (AddQueuesData.addQueueDate == null)
        {
            Toast.makeText(SharedData.mainActivity,"בחר מאיזה תאריך ושעה",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (AddQueuesData.addQueueHour == null)
        {
            Toast.makeText(SharedData.mainActivity,"בחר עד איזה תאריך ושעה",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (DateHelper.checkIfFutureDate(AddQueuesData.addQueueDate,AddQueuesData.addQueueHour) == false) //the check do toast
            return ;
        addQueueHelper();
    }

    private void addQueueHelper()
    {
        String alertTitle = "להוסיף את התור?";
        SimpleMethod doIfUserPressOk = () ->
        {
            doBeforeAddQueueRequest();
            String queueToAdd = DateHelper.getTime(AddQueuesData.addQueueDate,AddQueuesData.addQueueHour);
            ServerRequest serverRequest = new ServerRequest((String response) -> AddEmptyQueues.addQueueAns(response));
            serverRequest.addEmptyQueue(queueToAdd);
        };
        AlertDialog.showAlertDialog(alertTitle,"",doIfUserPressOk);
    }

    public void doBeforeAddQueueRequest()
    {
        addQueueLoadingView.setVisibility(View.VISIBLE);
        addQueueBtn.setEnabled(false);
        AddQueuesData.addQueueInRequest = true;
    }

    private boolean checkQueueSpaceEditText()
    {
        if (AddQueuesData.addQueuesSpaceBetweenQueues.isEmpty())
        {
            Toast.makeText(SharedData.mainActivity,"הכנס רווח בין תורים",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            int queueSpaceInt = Integer.parseInt(AddQueuesData.addQueuesSpaceBetweenQueues);
            if (queueSpaceInt == 0)
            {
                Toast.makeText(SharedData.mainActivity, "0 הוא לא מספר חוקי",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public boolean checkIfZeroQueues()
    {
        int nextQueueTime = Integer.parseInt(AddQueuesData.addQueuesSpaceBetweenQueues);
        nextQueueTime += (AddQueuesData.addQueuesHourStart / 100) * 60; //add hours in min
        nextQueueTime += (AddQueuesData.addQueuesHourStart % 100) ; //add min
        int endTimeStamp = (AddQueuesData.addQueuesHourEnd / 100) * 60; //add hours in min
        endTimeStamp += (AddQueuesData.addQueuesHourEnd % 100) ;//add min
        if (nextQueueTime > endTimeStamp)
        {
            Toast.makeText(SharedData.mainActivity, "אין מספיק מרווח זמנים בשביל תור אחד", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
            return false;
    }

    private boolean checkAddQueuesEditTextsNotNull()
    {
        if (AddQueuesData.addQueuesDateStart == null)
        {
            Toast.makeText(SharedData.mainActivity, "הכנס תאריך התחלה",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (AddQueuesData.addQueuesDateEnd == null)
        {
            Toast.makeText(SharedData.mainActivity, "הכנס תאריך סיום",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (AddQueuesData.addQueuesHourStart == null)
        {
            Toast.makeText(SharedData.mainActivity, "הכנס שעת התחלה",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (AddQueuesData.addQueuesHourEnd == null)
        {
            Toast.makeText(SharedData.mainActivity, "הכנס שעת סיום",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void addQueues(View view)
    {
        if (checkAddQueuesEditTextsNotNull() == false) //send toast msgs
            return;
        if (AddQueuesData.addQueuesDateStart.compareTo(AddQueuesData.addQueuesDateEnd) == 1)
        {
            Toast.makeText(SharedData.mainActivity,"תאריך סיום לא יכול להיות קודם לתאריך תחלה",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (AddQueuesData.addQueuesHourStart >= AddQueuesData.addQueuesHourEnd)
        {
            Toast.makeText(SharedData.mainActivity,"שעת התחלה צריכה להיות קודמת לשעת סיום",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        AddQueuesData.addQueuesSpaceBetweenQueues = spaceBetweenQueuesEditText.getText().toString();
        if (checkQueueSpaceEditText() == false) //send toast msgs
            return;
        if (DateHelper.checkIfFutureDate(AddQueuesData.addQueuesDateStart,AddQueuesData.addQueuesHourStart) == false) //send msgs
            return;
        if (checkIfZeroQueues()) //send toast msgs
            return;
        queueDaysString = makeQueueDaysList();
        if (queueDaysString.isEmpty())
        {
            Toast.makeText(SharedData.mainActivity, "רשימת התורים להוספה ריקה", Toast.LENGTH_SHORT).show();
            return;
        }
        addQueuesHelper();
    }

    private void addQueuesHelper()
    {
        String alertTitle = "להוסיף את התורים?";
        SimpleMethod doIfUserPressOk = () ->
        {
            doBeforeAddQueuesRequest();
            String startHour = DateHelper.makeHourStr(AddQueuesData.addQueuesHourStart);
            String endHour = DateHelper.makeHourStr(AddQueuesData.addQueuesHourEnd);
            ServerRequest serverRequest = new ServerRequest((String response) -> AddEmptyQueues.addQueuesAns(response));
            serverRequest.addEmptyQueues(queueDaysString, startHour, endHour);
        };
        AlertDialog.showAlertDialog(alertTitle,"",doIfUserPressOk);
    }

    public void doBeforeAddQueuesRequest()
    {
        addQueuesLoadingView.setVisibility(View.VISIBLE);
        addQueuesBtn.setEnabled(false);
        AddQueuesData.addQueuesInRequest = true;
    }


    public void addQueuesNotInRequest()
    {
        addQueuesLoadingView.setVisibility(View.GONE);
        addQueuesBtn.setEnabled(true);
        AddQueuesData.addQueuesInRequest = false;
    }

    public void addQueueNotInRequest()
    {
        addQueueLoadingView.setVisibility(View.GONE);
        addQueueBtn.setEnabled(true);
        AddQueuesData.addQueueInRequest = false;
    }

    String makeQueueDaysList()
    {
        String res = "";
        Date crntDate = (Date) AddQueuesData.addQueuesDateStart.clone();
        Calendar calendar = Calendar.getInstance();
        do
        {
            calendar.setTime(crntDate);
            int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) -1;
            if (AddQueuesData.daysTbState[day_of_week])
            {
                String monthStr,dayStr;
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH) ;
                if (month < 10)
                    monthStr = "0" + month;
                else
                    monthStr = "" + month;
                if (day < 10)
                    dayStr = "0" + day;
                else
                    dayStr = "" + day;
                res += calendar.get(Calendar.YEAR) + monthStr + dayStr;
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            crntDate = calendar.getTime();
        }
        while (crntDate.compareTo(AddQueuesData.addQueuesDateEnd) != 1);
        return res;
    }
}