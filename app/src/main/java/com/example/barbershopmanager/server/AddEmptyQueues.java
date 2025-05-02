package com.example.barbershopmanager.server;

import static com.example.barbershopmanager.server.ServerRequest.requestAnsHelper;

import android.widget.Toast;

import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.QueuesData;

import org.json.JSONObject;

public class AddEmptyQueues
{

    public static void addQueueAns(String response)
    {
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        else
        {
            String error;
            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                error = jsonResponse.getString("error");
            }
            catch (Exception e) {error = "yes";}
            if (error.equals("no"))
            {
                String msg= "התור נוסף";
                Toast.makeText(SharedData.mainActivity, msg, Toast.LENGTH_SHORT).show();
                QueuesData.askForEmptyQueues = true;
            }
            else if(error.equals("queue exist"))
                Toast.makeText(SharedData.mainActivity,"הפעולה נכשלה - התור קיים", Toast.LENGTH_SHORT).show();
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        }
        SharedData.addQueuesFragment.addQueueNotInRequest();
    }

    public static void addQueuesAns(String response)
    {
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        else
        {
            String error,queuesInserted = null;
            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                error = jsonResponse.optString("error",null);
                if (error == null)
                    queuesInserted = jsonResponse.getString("queuesInserted");

            }
            catch (Exception e) {error = "yes";}
            if (error == null)
            {
                if (queuesInserted.equals("0"))
                    Toast.makeText(SharedData.mainActivity, "כל התורים כבר קיימים", Toast.LENGTH_SHORT).show();
                else if (queuesInserted.equals("1"))
                    Toast.makeText(SharedData.mainActivity, "נוסף תור אחד", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SharedData.mainActivity, "נוספו " + queuesInserted + " תורים", Toast.LENGTH_SHORT).show();
                QueuesData.askForEmptyQueues = true;
            }
            else if (error.equals("reservedQueueExistInThisDates"))
                Toast.makeText(SharedData.mainActivity, "בקשה סורבה - התנגשות הוספת תור פנוי עם תור מוזמן קיים", Toast.LENGTH_SHORT).show();
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        }
        SharedData.addQueuesFragment.addQueuesNotInRequest();
    }

}
