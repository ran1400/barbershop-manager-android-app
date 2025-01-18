package com.example.barbershopmanager.fragments.dialogFragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.widget.DatePicker;
import java.io.Serializable;
import java.util.Calendar;



public class ChooseDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{

    private static Calendar calendar = Calendar.getInstance();
    private DoOnGetAns doOnGetAns;


    public interface DoOnGetAns extends Serializable
    {
        void doOnGetAns(int year,int month,int day);
    }

   public ChooseDateFragment()
   {
       doOnGetAns = null; // for change screen size (if we called from the system and not from my code)
   }

   @SuppressLint("ValidFragment")
   public ChooseDateFragment(DoOnGetAns doOnGetAns)
   {
       this.doOnGetAns = doOnGetAns;
   }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        if (doOnGetAns == null)
            this.dismiss(); // if we called from the system and not from my code - so disappear
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        doOnGetAns.doOnGetAns(year,month,day);
    }

    public void show(Fragment fragment)
    {
        super.show(fragment.getParentFragmentManager(), "");
    }
}
