package com.example.barbershopmanager.server;

import android.util.Log;

import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.QueuesData;
import com.example.barbershopmanager.utils.DateHelper;

public class PastQueues
{
    public static void getQueueAns(String response,String startTime,String endTime)
    {
        Log.d("requestAns",response);
        QueuesData.pastQueuesInRequest = false;
        if (ServerRequest.requestAnsHelperWithoutToast(response) == false)
        {
            QueuesData.haveInternet = false;
            SharedData.queuesFragment.showNoInternet();
        }
        else
        {
            QueuesData.haveInternet = true;
            SharedData.queuesFragment.goneNoInternet();
            QueuesData.pastQueuesArray = makeQueuesArray(response);
            QueuesData.pastQueueStringStart = DateHelper.flipYYYYMMDD(startTime);
            QueuesData.pastQueueStringEnd = DateHelper.flipYYYYMMDD(endTime);
        }
        SharedData.pastQueuesFragment.enableOkBtnAndGoneLoadingView();
        SharedData.pastQueuesFragment.createQueuesViewList();
    }

    private static String[] makeQueuesArray(String response)
    {
        if (response.isEmpty())
            return new String[0];
        String tokens[] = response.split("<br>");
        int tokensArrayLength = tokens.length;
        int resArrayLength = tokensArrayLength / 2; //time,name,phone,id
        String res[] = new String[resArrayLength];
        String crntQueue;
        int resIndex = 0;
        for ( int i = 0 ; i < tokensArrayLength ; i+=2)
        {
            crntQueue = tokens[i] + "\n" + tokens[i+1];
            res[resIndex++] = crntQueue;
        }
        return res;
    }

}
