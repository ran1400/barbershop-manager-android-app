package com.example.barbershopmanager.fragments.QueuesFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.barbershopmanager.utils.ExcelHandle;
import com.example.barbershopmanager.R;
import com.example.barbershopmanager.utils.DateHelper;
import com.example.barbershopmanager.server.PastQueues;
import com.example.barbershopmanager.server.ServerRequest;
import com.example.barbershopmanager.fragments.dialogFragments.ChooseDateFragment;
import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.QueuesData;

import java.util.Date;
import java.util.GregorianCalendar;


public class PastQueuesFragment extends Fragment
{
    private TextView pastQueuesEditText;
    private View loadingView;
    private EditText dateStartEditText,dateEndEditText;
    private Button getPastQueuesBtn,getPestQueuesFileBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_past_queues, container, false);
        SharedData.pastQueuesFragment = this;
        getPestQueuesFileBtn = view.findViewById(R.id.getPestQueueFileBtn);
        loadingView = view.findViewById(R.id.pastQueuesFragmentLoadingView);
        dateStartEditText = view.findViewById(R.id.pastQueuesDateStart);
        dateEndEditText = view.findViewById(R.id.pastQueuesDateEnd);
        getPastQueuesBtn = view.findViewById(R.id.getPastQueueBtn);
        pastQueuesEditText = view.findViewById(R.id.pastQueuesTextView);
        getPestQueuesFileBtn.setOnClickListener(this::getPestQueuesFileBtn);
        getPastQueuesBtn.setOnClickListener(this::getPastQueuesBtn);
        dateStartEditText.setOnClickListener(this::startDateClicked);
        dateEndEditText.setOnClickListener(this::endDateClicked);
        fillEditTexts();
        SharedData.pastQueuesFragment.updateLoadingViews();
        createQueuesViewList();
        return view;
    }

    public void onStart()
    {
        super.onStart();
        updateLoadingViews();
    }

    private void fillEditTexts()
    {
        if (QueuesData.pastQueuesDateStart != null)
            dateStartEditText.setText(DateHelper.fromDateToStr(QueuesData.pastQueuesDateStart));
        else
            dateStartEditText.setText("");
        if (QueuesData.pastQueuesDateEnd != null)
            dateEndEditText.setText(DateHelper.fromDateToStr(QueuesData.pastQueuesDateEnd));
        else
            dateEndEditText.setText("");
    }

    public void enableOkBtnAndGoneLoadingView()
    {
        loadingView.setVisibility(View.INVISIBLE);
        getPastQueuesBtn.setEnabled(true);
    }

    public void updateLoadingViews()
    {
        if (QueuesData.pastQueuesInRequest)
        {
            loadingView.setVisibility(View.VISIBLE);
            getPastQueuesBtn.setEnabled(false);
        }
        else
        {
            loadingView.setVisibility(View.INVISIBLE);
            getPastQueuesBtn.setEnabled(true);
        }
    }

    private void getPestQueuesFileBtn(View view)
    {
        ExcelHandle excelHandle = new ExcelHandle();
        boolean fileIsWritten = excelHandle.makePestQueuesListFile();
        if (fileIsWritten)
            Toast.makeText(SharedData.mainActivity,"הקובץ נשמר בתיקיית הורדות", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(SharedData.mainActivity,"שמירת הקובץ נכשלה", Toast.LENGTH_SHORT).show();
    }

    private void getPastQueuesBtn(View view)
    {
        if (QueuesData.pastQueuesDateStart== null)
        {
            Toast.makeText(getActivity(),"הכנס תאריך התחלה",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (QueuesData.pastQueuesDateEnd == null)
        {
            Toast.makeText(getActivity(),"הכנס תאריך סיום",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (QueuesData.pastQueuesDateStart.compareTo(QueuesData.pastQueuesDateEnd) == 1)
        {
            Toast.makeText(getActivity(),"תאריך סיום לא יכול להיות קודם לתאריך תחלה",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        getPastQueueHelper();
    }

    private void getPastQueueHelper()
    {
        String startDate = DateHelper.getTime(QueuesData.pastQueuesDateStart);
        String endDate = DateHelper.getTime(QueuesData.pastQueuesDateEnd);
        loadingView.setVisibility(View.VISIBLE);
        QueuesData.pastQueuesInRequest = true;
        ServerRequest serverRequest = new ServerRequest((String response) -> PastQueues.getQueueAns(response,startDate,endDate));
        serverRequest.getPastReservedQueues(startDate,endDate);
    }

    private void startDateClicked(View view)
    {
        ChooseDateFragment.DoOnGetAns doOnGetAns = (int year , int month,int day) ->
        {
            Date date = new GregorianCalendar(year,month,day).getTime();
            SharedData.pastQueuesFragment.dateStartEditText.setText(day + "." + (month+1) + "." + year);
            QueuesData.pastQueuesDateStart = date;
        };
        ChooseDateFragment datePicker = new ChooseDateFragment(doOnGetAns);
        datePicker.show(getFragmentManager(), "datePicker");
    }

    private void endDateClicked(View view)
    {
        ChooseDateFragment.DoOnGetAns doOnGetAns = (int year , int month,int day) ->
        {
            Date date = new GregorianCalendar(year,month,day).getTime();
            SharedData.pastQueuesFragment.dateEndEditText.setText(day + "." + (month + 1) + "." + year);
            QueuesData.pastQueuesDateEnd = date;
        };
        ChooseDateFragment datePicker = new ChooseDateFragment(doOnGetAns);
        datePicker.show(getFragmentManager(), "datePicker");
    }

    public void createQueuesViewList()
    {

        loadingView.setVisibility(View.GONE);
        if (QueuesData.pastQueuesArray == null)
        {
            getPestQueuesFileBtn.setVisibility(View.GONE);
            return;
        }
        getPestQueuesFileBtn.setVisibility(View.VISIBLE);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        String header = QueuesData.pastQueueStringStart + " - " + QueuesData.pastQueueStringEnd + " :\n";
        header += "----------------------------------------\n\n";
        spannableStringBuilder.append(header);
        int startIndex,endIndex = header.length();
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(22, true), 0,endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        startIndex = endIndex;
        if (QueuesData.pastQueuesArray.length == 0)
        {
            getPestQueuesFileBtn.setVisibility(View.GONE);
            String noQueueText = "אין תורים";
            spannableStringBuilder.append(noQueueText);
            endIndex = startIndex + noQueueText.length();
            spannableStringBuilder.setSpan(new AbsoluteSizeSpan(20, true), startIndex,endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            pastQueuesEditText.setText(spannableStringBuilder);
            return;
        }
        int queuesArrayLength = QueuesData.pastQueuesArray.length;
        String prevQueueDate = QueuesData.pastQueuesArray[0].substring(0,10);
        String date = DateHelper.flipDateString(prevQueueDate) + "  " + DateHelper.getDayOfWeek(prevQueueDate) + "\n";
        spannableStringBuilder.append(date);
        endIndex = startIndex + date.length();
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(20, true), startIndex,endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        startIndex = endIndex;
        for (int i = 0; i < queuesArrayLength ; i++)
        {
            String crntQueueDate = QueuesData.pastQueuesArray[i].substring(0,10);
            if (crntQueueDate.equals(prevQueueDate) == false)
            {
                date = "\n" + DateHelper.flipDateString(crntQueueDate.substring(0,10)) + "  " + DateHelper.getDayOfWeek(crntQueueDate) + "\n";
                spannableStringBuilder.append(date);
                endIndex = startIndex + date.length();
                spannableStringBuilder.setSpan(new AbsoluteSizeSpan(20, true), startIndex,endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                startIndex = endIndex;
            }
            String hour = QueuesData.pastQueuesArray[i].substring(11,13);
            String min = QueuesData.pastQueuesArray[i].substring(14,16);
            String name = QueuesData.pastQueuesArray[i].substring(19);
            String queue = "\n" + hour+ ":" + min + name + "\n";
            spannableStringBuilder.append(queue);
            endIndex = startIndex + queue.length();
            spannableStringBuilder.setSpan(new AbsoluteSizeSpan(18, true), startIndex,endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            startIndex = endIndex;
            prevQueueDate = crntQueueDate;
        }
        pastQueuesEditText.setText(spannableStringBuilder);
    }
}