package com.example.barbershopmanager.server;

import android.util.Log;

import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.QueuesData;
import com.example.barbershopmanager.utils.DateHelper;
import com.example.barbershopmanager.utils.dataStructures.ReservedQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PastQueues
{
    public static void getQueueAns(String response,String startTime,String endTime)
    {
        QueuesData.pastQueuesInRequest = false;
        if (response == ServerRequest.REQUEST_ERROR)
        {
            QueuesData.haveInternet = false;
            SharedData.queuesFragment.showNoInternet();
        }
        else
        {
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
                QueuesData.haveInternet = false;
                SharedData.queuesFragment.showNoInternet();
            }
            else
            {
                QueuesData.haveInternet = true;
                SharedData.queuesFragment.goneNoInternet();
                QueuesData.pastQueuesArray = makeQueuesArray(queues);
                QueuesData.pastQueueStringStart = DateHelper.flipYYYYMMDD(startTime);
                QueuesData.pastQueueStringEnd = DateHelper.flipYYYYMMDD(endTime);
            }
        }
        SharedData.pastQueuesFragment.enableOkBtnAndGoneLoadingView();
        SharedData.pastQueuesFragment.createQueuesViewList();
    }

    private static String[] makeQueuesArray(JSONArray queues)
    {
        try
        {
            int queuesAmount = queues.length();
            String[] res = new String[queuesAmount];
            for ( int i = 0 ; i < queuesAmount ; i++)
            {
                JSONObject queue = queues.getJSONObject(i);
                res[i] = queue.getString("Time") + "\n" + queue.getString("Name") ;
            }
            return res;
        }
        catch (JSONException e) {return null;}
    }

}
