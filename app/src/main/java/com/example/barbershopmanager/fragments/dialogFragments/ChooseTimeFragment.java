package com.example.barbershopmanager.fragments.dialogFragments;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.widget.TimePicker;


public class ChooseTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{
    public interface DoOnGetAns
    {
        void doOnGetAns(int hour,int min);
    }

    private DoOnGetAns doOnGetAns;


    public ChooseTimeFragment()
    {
        doOnGetAns = null; // for change screen size (if we called from the system and not from my fragments)
    }

    public ChooseTimeFragment(DoOnGetAns doOnGetAns)
    {
        this.doOnGetAns = doOnGetAns;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        if (doOnGetAns == null)
            this.dismiss();
        return new TimePickerDialog(getActivity(),this,10,0,true);
    }

    public static String makeHourStr(int hour, int min)
    {
        String minStr,hourStr;
        if (min < 10)
            minStr = "0" + min ;
        else
            minStr = String.valueOf(min);
        if (hour < 10)
            hourStr = "0" + hour ;
        else
            hourStr = String.valueOf(hour);
        return " " + minStr + " : " + hourStr + " ";
    }

    public static String makeHourStr(int hour)
    {
        if (hour < 10)
            return "0" + hour ;
        else
            return String.valueOf(hour);
    }

    public static String makeMinStr(int min)
    {
        if (min < 10)
            return "0" + min ;
        else
            return String.valueOf(min);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min)
    {
        doOnGetAns.doOnGetAns(hour,min);
    }

    public void show(Fragment fragment)
    {
        super.show(fragment.getParentFragmentManager(), "");
    }
}