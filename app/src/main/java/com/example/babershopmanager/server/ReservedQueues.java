package com.example.babershopmanager.server;

import android.view.View;
import android.widget.Toast;

import com.example.babershopmanager.sharedDate.QueuesData;
import com.example.babershopmanager.utils.dataStructures.ReservedQueue;
import com.example.babershopmanager.sharedDate.SharedData;


public class ReservedQueues
{

    public static ReservedQueue[] makeReservedQueuesArray(String response)
    {
        String tokens[] = response.split("<br>");
        int tokensArrayLength = tokens.length;
        int resArrayLength = tokensArrayLength / 4; //time,name,phone,id
        ReservedQueue[] res = new ReservedQueue[resArrayLength];
        ReservedQueue crntQueue;
        int resIndex = 0;
        for ( int i = 0 ; i < tokensArrayLength ; i+=4)
        {
            crntQueue = new ReservedQueue(tokens[i],tokens[i+1],tokens[i+2],tokens[i+3]);
            res[resIndex++] = crntQueue;
        }
        return res;
    }

    public static void getReservedQueuesAns(String response)
    {
        if (ServerRequest.requestAnsHelperWithoutToast(response) == false)
        {
            SharedData.reservedQueuesFragment.loadingView.setVisibility(View.INVISIBLE);
            QueuesData.askForReservedQueues = true;
            QueuesData.haveInternet = false;
            SharedData.queuesFragment.showNoInternet();
            SharedData.reservedQueuesFragment.createReservedQueuesViewList();
            return;
        }
        QueuesData.askForReservedQueues = false;
        QueuesData.haveInternet = true;
        if (SharedData.queuesFragment != null)
            SharedData.queuesFragment.goneNoInternet();
        if (response.isEmpty())
            QueuesData.reservedQueuesArray =  new ReservedQueue[0];
        else
            QueuesData.reservedQueuesArray = makeReservedQueuesArray(response);
        SharedData.reservedQueuesFragment.createReservedQueuesViewList();
        if (SharedData.queuesFragment != null && SharedData.queuesFragment.reservedQueuesIsChecked()) //if queue view was visible
            SharedData.reservedQueuesFragment.makeVisible();
    }

    public static void changeReservedQueueAns(String response)
    {
        if (response.equals("V"))
        {
            Toast.makeText(SharedData.mainActivity, "התור שונה", Toast.LENGTH_SHORT).show();
            SharedData.queuesFragment.refreshQueues();
        }
        else if (response.equals("cmd failed"))
        {
            Toast.makeText(SharedData.mainActivity, "הפעולה נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
            SharedData.queuesFragment.refreshQueues();
        }
        else if (response.equals("queue exist"))
            Toast.makeText(SharedData.mainActivity, "הפעולה נכשלה - התור תפוס על ידי מישהו אחר", Toast.LENGTH_SHORT).show();
        else if (response.equals(ServerRequest.REQUEST_ERROR))
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
        else if (response.equals("connection failed"))
            Toast.makeText(SharedData.mainActivity, "התחברות לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
        else if (response.equals("permission problem"))
            Toast.makeText(SharedData.mainActivity, "בעיית הרשאות", Toast.LENGTH_SHORT).show();
        SharedData.reservedQueuesFragment.doWhenPopUpServerAns();
    }

    public static void cleanReservedQueueAns(String response)
    {
        if (response.equals("V"))
        {
            Toast.makeText(SharedData.mainActivity,"התור התווסף לתורים הריקים", Toast.LENGTH_SHORT).show();
            SharedData.queuesFragment.refreshQueues();
        }
        else if (response.equals("cmd failed"))
        {
            Toast.makeText(SharedData.mainActivity, "הפעולה נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
            SharedData.queuesFragment.refreshQueues();
        }
        else if (response.equals(ServerRequest.REQUEST_ERROR))
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
        else if (response.equals("connection failed"))
            Toast.makeText(SharedData.mainActivity, "התחברות לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
        else if (response.equals("permission problem"))
            Toast.makeText(SharedData.mainActivity, "בעיית הרשאות", Toast.LENGTH_SHORT).show();
        SharedData.reservedQueuesFragment.doWhenPopUpServerAns();

    }

    public static void deleteReservedQueueAns(String response)
    {
        if (response.equals("V"))
        {
            Toast.makeText(SharedData.mainActivity,"התור נמחק", Toast.LENGTH_SHORT).show();
            SharedData.queuesFragment.refreshQueues();
        }
        else if (response.equals("cmd failed"))
        {
            Toast.makeText(SharedData.mainActivity, "הפעולה נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
            SharedData.queuesFragment.refreshQueues();
        }
        else if (response.equals(ServerRequest.REQUEST_ERROR))
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
        else if (response.equals("connection failed"))
            Toast.makeText(SharedData.mainActivity, "התחברות לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
        else if (response.equals("permission problem"))
            Toast.makeText(SharedData.mainActivity, "בעיית הרשאות", Toast.LENGTH_SHORT).show();
        SharedData.reservedQueuesFragment.doWhenPopUpServerAns();
    }
}
