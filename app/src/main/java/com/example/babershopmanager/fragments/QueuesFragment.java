package com.example.babershopmanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.example.babershopmanager.R;
import com.example.babershopmanager.server.EmptyQueues;
import com.example.babershopmanager.server.ReservedQueues;
import com.example.babershopmanager.server.ServerRequest;
import com.example.babershopmanager.fragments.QueuesFragments.EmptyQueuesFragment;
import com.example.babershopmanager.fragments.QueuesFragments.PastQueuesFragment;
import com.example.babershopmanager.fragments.QueuesFragments.ReservedQueuesFragment;
import com.example.babershopmanager.sharedDate.SharedData;
import com.example.babershopmanager.sharedDate.QueuesData;

public class QueuesFragment extends Fragment
{
    private RadioGroup chooseQueueList;
    private RadioButton emptyQueuesRadioBtn,pastReservedQueuesRadioBtn;
    private RadioButton reservedQueuesRadioBtn;
    private ImageButton refreshQueuesBtn;
    private TextView noInternetText;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_queues, container, false);
        SharedData.queuesFragment = this;
        chooseQueueList = view.findViewById(R.id.showQueuesRadioGroup);
        reservedQueuesRadioBtn = view.findViewById(R.id.reservedQueues);
        emptyQueuesRadioBtn = view.findViewById(R.id.emptyQueues);
        pastReservedQueuesRadioBtn = view.findViewById(R.id.pastReservedQueues);
        refreshQueuesBtn = view.findViewById(R.id.refreshQueuesBtn);
        noInternetText = view.findViewById(R.id.noInternetText);
        refreshQueuesBtn.setOnClickListener(this::refreshQueues);
        chooseQueueList.setOnCheckedChangeListener(this::chooseQueuesRadioGroupOnClickListener);
        pastReservedQueuesRadioBtn.setOnCheckedChangeListener(this::pastReservedQueuesListener);
        if (QueuesData.haveInternet == false)
            showNoInternet();
        if (reservedQueuesRadioBtn.isChecked())
            showReservedQueues();
        return view;
    }


    public void showNoInternet()
    {
        noInternetText.setVisibility(View.VISIBLE);
    }

    public void goneNoInternet()
    {
        if (noInternetText != null)
            noInternetText.setVisibility(View.GONE);
    }

    public boolean reservedQueuesIsChecked()
    {
        if (reservedQueuesRadioBtn == null)
            return false;
        return reservedQueuesRadioBtn.isChecked();
    }

    public void setReservedQueuesRadioBtnChecked()
    {
        reservedQueuesRadioBtn.setChecked(true);
    }


    public void pastReservedQueuesListener(CompoundButton compoundButton, boolean checked)
    {
        if (checked)
        {
            chooseQueueList.clearCheck();
            showPastReservedQueues();
            refreshQueuesBtn.setVisibility(View.INVISIBLE);
        }
    }

    public void refreshQueues(View view) //fun for the btn
    {
        refreshQueues();
    }

    public void refreshQueues()
    {
        QueuesData.refreshQueues();
        if (reservedQueuesRadioBtn.isChecked())
        {
            SharedData.reservedQueuesFragment.loadingView.bringToFront();
            SharedData.reservedQueuesFragment.loadingView.setVisibility(View.VISIBLE);
            ServerRequest serverRequest = new ServerRequest((String response) -> ReservedQueues.getReservedQueuesAns(response));
            serverRequest.getReservedQueues();
        }
        else if (emptyQueuesRadioBtn.isChecked())
        {
            SharedData.emptyQueuesFragment.showLoadingView();
            ServerRequest serverRequest = new ServerRequest((String response) -> EmptyQueues.getEmptyQueuesAns(response));
            serverRequest.getEmptyQueues();
        }
    }

    public void showReservedQueues()
    {
        SharedData.mainActivity.changeActivityTitle("תורים קבועים :");
        if (SharedData.reservedQueuesFragment == null)
            SharedData.reservedQueuesFragment = new ReservedQueuesFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.queuesListFrame, SharedData.reservedQueuesFragment);
        fragmentTransaction.commit(); // save the changes
    }

    private void showPastReservedQueues()
    {
        SharedData.mainActivity.changeActivityTitle("תורים קודמים :");
        if (SharedData.pastQueuesFragment == null)
            SharedData.pastQueuesFragment = new PastQueuesFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.queuesListFrame, SharedData.pastQueuesFragment);
        fragmentTransaction.commit(); // save the changes
    }

    private void showEmptyQueues()
    {
        SharedData.mainActivity.changeActivityTitle("תורים פנויים :");
        if (SharedData.emptyQueuesFragment == null)
            SharedData.emptyQueuesFragment = new EmptyQueuesFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.queuesListFrame, SharedData.emptyQueuesFragment);
        fragmentTransaction.commit(); // save the changes
    }


    public void chooseQueuesRadioGroupOnClickListener(RadioGroup radioGroup, int checkedId)
    {
        if (reservedQueuesRadioBtn.isChecked())
        {
            pastReservedQueuesRadioBtn.setChecked(false);
            showReservedQueues();
            refreshQueuesBtn.setVisibility(View.VISIBLE);
        }
        else if (emptyQueuesRadioBtn.isChecked())
        {
            pastReservedQueuesRadioBtn.setChecked(false);
            showEmptyQueues();
            refreshQueuesBtn.setVisibility(View.VISIBLE);
        }
    }

}