package com.example.babershopmanager.server;

import static com.example.babershopmanager.server.ServerRequest.requestAnsHelper;

import android.widget.Toast;

import com.example.babershopmanager.sharedDate.SharedData;
import com.example.babershopmanager.sharedDate.QueuesData;

public class AddQueues
{

    public static void addQueueAns(String response)
    {
        SharedData.addQueuesFragment.addQueueAns();
        if (response.equals("V"))
        {
            String msg= "התור נוסף";
            Toast.makeText(SharedData.mainActivity, msg, Toast.LENGTH_SHORT).show();
            QueuesData.askForEmptyQueues = true;
        }
        else if (response.equals("X"))
            Toast.makeText(SharedData.mainActivity, "בקשה סורבה\nניסיון הוספת תור קיים", Toast.LENGTH_SHORT).show();
       else
            ServerRequest.requestAnsHelper(response); //make toast
    }

    public static void addQueuesAns(String response)
    {
        SharedData.addQueuesFragment.addQueuesAns();
        if (response.equals("reservedQueueExistInThisDates"))
        {
            Toast.makeText(SharedData.mainActivity, "בקשה סורבה - התנגשות הוספת תור פנוי עם תור מוזמן קיים", Toast.LENGTH_SHORT).show();
        }
        else if (response.equals("emptyQueueExistInThisDates"))
        {
            Toast.makeText(SharedData.mainActivity, "בקשה סורבה - ניסיון הוספת תור פנוי קיים", Toast.LENGTH_SHORT).show();
        }
        else if ( requestAnsHelper(response)) // send toasts if false
        {
            if (response.equals("1"))
                Toast.makeText(SharedData.mainActivity, "נוסף תור אחד", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(SharedData.mainActivity, "נוספו " + response + " תורים", Toast.LENGTH_SHORT).show();
            QueuesData.askForEmptyQueues = true;
        }
    }

}
