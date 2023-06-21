package com.example.babershopmanager.fragments.QueuesFragments;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.babershopmanager.R;
import com.example.babershopmanager.server.EmptyQueues;
import com.example.babershopmanager.server.ServerRequest;
import com.example.babershopmanager.fragments.QueuesFragment;
import com.example.babershopmanager.sharedDate.SharedData;
import com.example.babershopmanager.sharedDate.QueuesData;
import com.example.babershopmanager.utils.DateHelper;


public class EmptyQueuesFragment extends Fragment
{

    private LinearLayout emptyQueuesLayout;
    private ProgressBar loadingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_empty_queues, container, false);
        SharedData.emptyQueuesFragment = this;
        loadingView = view.findViewById(R.id.emptyQueueFragmentLoadingView);
        emptyQueuesLayout = view.findViewById(R.id.emptyQueuesScrollViewLayout);
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
        emptyQueuesLayout.removeAllViewsInLayout();
        if (QueuesData.emptyQueuesArray.length == 0)
        {
            QueuesFragment.addTextToLayout(emptyQueuesLayout,"אין תורים פנויים",20);
            return;
        }
        int queuesArrayLength = QueuesData.emptyQueuesArray.length;
        String prevQueueDate = QueuesData.emptyQueuesArray[0].substring(0,10);
        String date = DateHelper.flipDateString(prevQueueDate) + "  " + DateHelper.getDayOfWeek(prevQueueDate);
        QueuesFragment.addTextToLayout(emptyQueuesLayout,date,21);
        QueuesFragment.addTextToLayout(emptyQueuesLayout,"",21);
        for (int i = 0; i < queuesArrayLength ; i+= 1)
        {
            String crntQueueDate = QueuesData.emptyQueuesArray[i].substring(0,10);
            if (crntQueueDate.equals(prevQueueDate) == false)
            {
                QueuesFragment.addTextToLayout(emptyQueuesLayout, "",21);
                date = DateHelper.flipDateString(crntQueueDate.substring(0,10)) + "  " + DateHelper.getDayOfWeek(crntQueueDate);
                QueuesFragment.addTextToLayout(emptyQueuesLayout, date,21);
                QueuesFragment.addTextToLayout(emptyQueuesLayout, "",21);
            }
            QueuesFragment.addTextToLayout(emptyQueuesLayout,QueuesData.emptyQueuesArray[i].substring(11,16),18);
            prevQueueDate = crntQueueDate;
        }
    }

}