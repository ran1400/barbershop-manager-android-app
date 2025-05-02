package com.example.barbershopmanager.server;

import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.QueuesData;

import org.json.JSONArray;
import org.json.JSONObject;

public class EmptyQueues
{
    public static void doIfAskForQueuesFailed()
    {
        QueuesData.haveInternet = false;
        SharedData.queuesFragment.showNoInternet();
        QueuesData.askForEmptyQueues = true;
        SharedData.emptyQueuesFragment.createEmptyQueuesViewList();
    }
    public static void getEmptyQueuesAns(String response)
    {
        SharedData.emptyQueuesFragment.goneLoadingView();
        if (response == ServerRequest.REQUEST_ERROR)
        {
            doIfAskForQueuesFailed();
            return;
        }
        String error;
        JSONObject serverResponse;
        JSONArray queues = null;
        try
        {
            serverResponse = new JSONObject(response);
            error = serverResponse.optString("error", null);
            if (error == null)
                queues = serverResponse.getJSONArray("queues");
        }
        catch (Exception e) {error = "yes";}
        if(error != null) // error is : yes || sql connection failed || permission problem || start with cmd failed
        {
            doIfAskForQueuesFailed();
            return;
        }
        QueuesData.askForEmptyQueues = false;
        QueuesData.haveInternet = true;
        QueuesData.emptyQueuesArray = queues;
        if (SharedData.emptyQueuesFragment != null)
            SharedData.emptyQueuesFragment.createEmptyQueuesViewList();
        if (SharedData.queuesFragment != null)
            SharedData.queuesFragment.goneNoInternet();
    }

}
