package com.example.babershopmanager.fragments.QueuesFragments;

import android.os.Bundle;

import android.app.Fragment;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.babershopmanager.R;
import com.example.babershopmanager.server.EmptyQueues;
import com.example.babershopmanager.server.ServerRequest;
import com.example.babershopmanager.fragments.QueuesFragment;
import com.example.babershopmanager.sharedDate.SharedData;
import com.example.babershopmanager.sharedDate.QueuesData;
import com.example.babershopmanager.utils.DateHelper;


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
            return;
        if (QueuesData.emptyQueuesArray.length == 0)
        {
            emptyQueueTextView.setText("אין תורים פנויים");
            return;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        int queuesArrayLength = QueuesData.emptyQueuesArray.length;
        String prevQueueDate = QueuesData.emptyQueuesArray[0].substring(0,10);
        String date = DateHelper.flipDateString(prevQueueDate) + "  " + DateHelper.getDayOfWeek(prevQueueDate) + "\n";
        spannableStringBuilder.append(date);
        int startIndex;
        int endIndex = date.length();
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(22, true),0,endIndex,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        startIndex = endIndex;
        for (int i = 0; i < queuesArrayLength ; i+= 1)
        {
            String crntQueueDate = QueuesData.emptyQueuesArray[i].substring(0,10);
            if (crntQueueDate.equals(prevQueueDate) == false)
            {
                date = "\n\n" + DateHelper.flipDateString(crntQueueDate.substring(0,10)) + "  " + DateHelper.getDayOfWeek(crntQueueDate) + "\n";
                spannableStringBuilder.append(date);
                endIndex = startIndex + date.length();
                spannableStringBuilder.setSpan(new AbsoluteSizeSpan(22, true), startIndex,endIndex,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                startIndex = endIndex;
            }
            String queueHour = "\n" + QueuesData.emptyQueuesArray[i].substring(11,16);
            spannableStringBuilder.append(queueHour);
            endIndex = startIndex + queueHour.length();
            spannableStringBuilder.setSpan(new AbsoluteSizeSpan(18, true), startIndex,endIndex,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            startIndex = endIndex;
            prevQueueDate = crntQueueDate;
        }
        emptyQueueTextView.setText(spannableStringBuilder);
    }

}