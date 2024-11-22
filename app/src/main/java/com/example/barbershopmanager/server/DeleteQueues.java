package com.example.barbershopmanager.server;

import android.widget.Toast;
import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.QueuesData;

public class DeleteQueues
{

    public static void deleteReservedQueueAns(String response)
    {
        SharedData.deleteQueuesFragment.deleteQueueAns();
        if (response.equals("V"))
        {
            QueuesData.askForReservedQueues = true;
            String msg = "התור נמחק";
            Toast.makeText(SharedData.mainActivity, msg, Toast.LENGTH_SHORT).show();
        }
        else if ( response.equals("not found"))
        {
            String msg = "התור לא נמצא";
            Toast.makeText(SharedData.mainActivity, msg, Toast.LENGTH_SHORT).show();
            return;
        }
        else
            ServerRequest.requestAnsHelper(response); // make toasts

    }

    public static void deleteEmptyOrReservedQueueAns(String response)
    {
        SharedData.deleteQueuesFragment.deleteQueueAns();
        if (response.equals("emptyQueue")) //empty queue deleted
        {
            String msg = "נמחק תור פנוי";
            Toast.makeText(SharedData.mainActivity, msg, Toast.LENGTH_SHORT).show();
            QueuesData.askForEmptyQueues = true;
        }
        else if (response.equals("reservedQueue"))
        {
            String msg = "נמחק תור תפוס";
            QueuesData.askForReservedQueues = true;
            Toast.makeText(SharedData.mainActivity, msg, Toast.LENGTH_SHORT).show();
        }
        else if ( response.equals("not found"))
        {
            String msg = "התור לא נמצא";
            Toast.makeText(SharedData.mainActivity, msg, Toast.LENGTH_SHORT).show();
            return;
        }
        else
            ServerRequest.requestAnsHelper(response); //make toast
    }

    public static void deleteEmptyQueueAns(String response)
    {
        SharedData.deleteQueuesFragment.deleteQueueAns();
        if (response.equals("V"))
        {
            String msg = "התור נמחק";
            Toast.makeText(SharedData.mainActivity, msg, Toast.LENGTH_SHORT).show();
            QueuesData.askForEmptyQueues = true;

        }
        else if (response.equals("not found"))
            Toast.makeText(SharedData.mainActivity, "התור לא נמצא", Toast.LENGTH_SHORT).show();
        else
            ServerRequest.requestAnsHelper(response); //make toast
    }

    public static void deleteEmptyQueuesAns(String response)
    {
        SharedData.deleteQueuesFragment.deleteQueuesAns();
        if (ServerRequest.requestAnsHelper(response))  //requestAnsHelper make toast if false
        {
            String msg;
            if (response.equals("0"))
                msg = "לא נמצאו תורים";
            else
                msg = "נמחקו " + response + " תורים פנויים";
            Toast.makeText(SharedData.mainActivity, msg, Toast.LENGTH_SHORT).show();
            QueuesData.askForEmptyQueues = true;
        }
    }

    public static void deleteReservedQueuesAns(String response)
    {
        SharedData.deleteQueuesFragment.deleteQueuesAns();
        if (ServerRequest.requestAnsHelper(response)) //requestAnsHelper make toast if false
        {
            String msg;
            if (response.equals("0"))
                msg = "לא נמצאו תורים";
            else
                msg = "נמחקו " + response + " תורים קבועים";
            QueuesData.askForReservedQueues = true;
            Toast.makeText(SharedData.mainActivity, msg, Toast.LENGTH_SHORT).show();
        }
    }


    public static void deleteEmptyAndReservedQueuesAns(String response)
    {
        SharedData.deleteQueuesFragment.deleteQueuesAns();
        if (ServerRequest.requestAnsHelper(response)) //requestAnsHelper make toast if false
        {
            String res[] = response.split("/");
            int reservedQueueDeleteCount = Integer.parseInt(res[1]);
            int emptyQueueDeleteCount = Integer.parseInt(res[0]);
            if (emptyQueueDeleteCount > 0 )
                QueuesData.askForEmptyQueues = true;
            if (reservedQueueDeleteCount > 0)
                QueuesData.askForReservedQueues = true;
            String msg;
            if (reservedQueueDeleteCount > 0 && emptyQueueDeleteCount == 0 )
                msg =  "נמחקו " + reservedQueueDeleteCount + " תורים תפוסים";
            else if (reservedQueueDeleteCount == 0 && emptyQueueDeleteCount > 0 )
                msg =  "נמחקו " + emptyQueueDeleteCount + " תורים פנויים";
            else if (reservedQueueDeleteCount == 0 && emptyQueueDeleteCount == 0 )
                msg = "לא נמצאו תורים";
            else //(reservedQueueDeleteCount > 0 && emptyQueueDeleteCount > 0 )
                msg= "נמחקו " + emptyQueueDeleteCount + " תורים פנויים ו " + reservedQueueDeleteCount + " תורים תפוסים";
            Toast.makeText(SharedData.mainActivity, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
