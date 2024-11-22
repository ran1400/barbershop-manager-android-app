package com.example.barbershopmanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

import com.example.barbershopmanager.R;
import com.example.barbershopmanager.server.EmptyQueues;
import com.example.barbershopmanager.server.ReservedQueues;
import com.example.barbershopmanager.server.ServerRequest;
import com.example.barbershopmanager.fragments.QueuesFragments.EmptyQueuesFragment;
import com.example.barbershopmanager.fragments.QueuesFragments.PastQueuesFragment;
import com.example.barbershopmanager.fragments.QueuesFragments.ReservedQueuesFragment;
import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.QueuesData;

public class QueuesFragment extends Fragment
{
    private RadioGroup chooseQueueList;
    private RadioButton emptyQueuesRadioBtn,pastReservedQueuesRadioBtn;
    private RadioButton reservedQueuesRadioBtn;
    private ImageButton refreshQueuesBtn;
    private TextView noInternetText;


    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState)
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
        getChildFragmentManager().beginTransaction().replace(R.id.queuesListFrame,SharedData.reservedQueuesFragment).commit();
    }


    private void showPastReservedQueues()
    {
        SharedData.mainActivity.changeActivityTitle("תורים קודמים :");
        if (SharedData.pastQueuesFragment == null)
            SharedData.pastQueuesFragment = new PastQueuesFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.queuesListFrame,SharedData.pastQueuesFragment).commit();
    }

    private void showEmptyQueues()
    {
        SharedData.mainActivity.changeActivityTitle("תורים פנויים :");
        if (SharedData.emptyQueuesFragment == null)
            SharedData.emptyQueuesFragment = new EmptyQueuesFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.queuesListFrame,SharedData.emptyQueuesFragment).commit();
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