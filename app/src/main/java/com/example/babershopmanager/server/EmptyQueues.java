package com.example.babershopmanager.server;

import com.example.babershopmanager.sharedDate.SharedData;
import com.example.babershopmanager.sharedDate.QueuesData;

public class EmptyQueues
{

    public static void getEmptyQueuesAns(String response)
    {
        if (ServerRequest.requestAnsHelperWithoutToast(response) == false)
        {
            SharedData.emptyQueuesFragment.goneLoadingView();
            QueuesData.haveInternet = false;
            SharedData.queuesFragment.showNoInternet();
            QueuesData.askForEmptyQueues = true;
            SharedData.emptyQueuesFragment.createEmptyQueuesViewList();
            return;
        }
        QueuesData.askForEmptyQueues = false;
        QueuesData.haveInternet = true;
        if (response.isEmpty())
            QueuesData.emptyQueuesArray = new String[0];
         else
            QueuesData.emptyQueuesArray = response.split("<br>");
        SharedData.emptyQueuesFragment.createEmptyQueuesViewList();
        SharedData.emptyQueuesFragment.goneLoadingView();
        SharedData.queuesFragment.goneNoInternet();
    }

}
