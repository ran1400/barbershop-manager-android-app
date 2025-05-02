package com.example.barbershopmanager.fragments.QueuesFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.barbershopmanager.R;
import com.example.barbershopmanager.server.EmptyQueues;
import com.example.barbershopmanager.server.ServerRequest;
import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.QueuesData;
import com.example.barbershopmanager.utils.DateHelper;

import org.json.JSONArray;
import org.json.JSONException;


public class EmptyQueuesFragment extends Fragment
{
    private ProgressBar loadingView;
    private TextView emptyQueueTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_empty_queues, container, false);
        SharedData.emptyQueuesFragment = this;
        emptyQueueTextView = view.findViewById(R.id.emptyQueuesTextView);
        loadingView = view.findViewById(R.id.emptyQueueFragmentLoadingView);
        if (QueuesData.askForEmptyQueues)
        {
            loadingView.setVisibility(View.VISIBLE);
            ServerRequest serverRequest = new ServerRequest((String response) -> EmptyQueues.getEmptyQueuesAns(response));
            serverRequest.getEmptyQueues();
        }
        else
            createEmptyQueuesViewList();
        return view;
    }

    public void goneLoadingView()
    {
        loadingView.setVisibility(View.GONE);
    }

    public void showLoadingView()
    {
        loadingView.setVisibility(View.VISIBLE);
    }

    public void createEmptyQueuesViewList()
    {
        loadingView.setVisibility(View.INVISIBLE);
        if (QueuesData.emptyQueuesArray == null)
        {
            emptyQueueTextView.setText("תקלה בהצגת התורים");
            return;
        }
        JSONArray queues = QueuesData.emptyQueuesArray;
        int queuesAmount = queues.length();
        if (queuesAmount == 0)
        {
            emptyQueueTextView.setText("אין תורים פנויים");
            return;
        }
        try
        {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            String prevQueueDate = queues.getString(0).substring(0,10);
            String date = DateHelper.flipDateString(prevQueueDate) + "  " + DateHelper.getDayOfWeek(prevQueueDate) + "\n";
            spannableStringBuilder.append(date);
            int startIndex = 0;
            int endIndex = date.length();
            spannableStringBuilder.setSpan(new AbsoluteSizeSpan(22, true),startIndex,endIndex,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            startIndex = endIndex;
            for (int i = 0; i < queuesAmount ; i++)
            {
                String crntQueueDate = queues.getString(i).substring(0,10);
                if (crntQueueDate.equals(prevQueueDate) == false)
                {
                    date = "\n\n" + DateHelper.flipDateString(crntQueueDate.substring(0,10)) + "  " + DateHelper.getDayOfWeek(crntQueueDate) + "\n";
                    spannableStringBuilder.append(date);
                    endIndex = startIndex + date.length();
                    spannableStringBuilder.setSpan(new AbsoluteSizeSpan(22, true), startIndex,endIndex,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    startIndex = endIndex;
                }
                String queueHour = "\n" + queues.getString(i).substring(11,16);
                spannableStringBuilder.append(queueHour);
                endIndex = startIndex + queueHour.length();
                spannableStringBuilder.setSpan(new AbsoluteSizeSpan(18, true), startIndex,endIndex,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                startIndex = endIndex;
                prevQueueDate = crntQueueDate;
            }
            emptyQueueTextView.setText(spannableStringBuilder);
        }
        catch (JSONException e) {emptyQueueTextView.setText("תקלה בהצגת התורים");}
    }

}