package com.example.barbershopmanager.server;

import android.widget.Toast;
import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.QueuesData;

import org.json.JSONObject;

public class DeleteQueues
{

    public static void deleteReservedQueueAns(String response)
    {
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        else
        {
            String error;
            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                error = jsonResponse.optString("error","yes");
            }
            catch (Exception e){error = "yes";}
            if (error.equals("no"))
            {
                Toast.makeText(SharedData.mainActivity,"התור נמחק", Toast.LENGTH_SHORT).show();
                QueuesData.askForReservedQueues = true;
            }
            else if (error.equals("queue not found"))
                Toast.makeText(SharedData.mainActivity,"התור לא נמצא", Toast.LENGTH_SHORT).show();
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        }

        SharedData.deleteQueuesFragment.deleteQueueNotInRequest();

    }

    public static void deleteEmptyOrReservedQueueAns(String response)
    {
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        else
        {
            String error,queueDelete = null;
            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                error = jsonResponse.optString("error",null);
                if (error == null)
                    queueDelete = jsonResponse.getString("queueDelete");
            }
            catch (Exception e) {error = "yes";}
            if (error == null)
            {
                if (queueDelete.equals("empty queue"))
                {
                    Toast.makeText(SharedData.mainActivity, "נמחק תור פנוי", Toast.LENGTH_SHORT).show();
                    QueuesData.askForEmptyQueues = true;
                }
                else //(queueDelete.equals("reserved queue"))
                {
                    Toast.makeText(SharedData.mainActivity, "נמחק תור תפוס", Toast.LENGTH_SHORT).show();
                    QueuesData.askForReservedQueues = true;
                }

            }
            else if (error.equals("queue not found"))
                Toast.makeText(SharedData.mainActivity, "התור לא נמצא", Toast.LENGTH_SHORT).show();
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        }
        SharedData.deleteQueuesFragment.deleteQueueNotInRequest();
    }

    public static void deleteEmptyQueueAns(String response)
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
                String msg = "התור נמחק";
                Toast.makeText(SharedData.mainActivity, msg, Toast.LENGTH_SHORT).show();
                QueuesData.askForEmptyQueues = true;
            }
            else if(error.equals("not found"))
                Toast.makeText(SharedData.mainActivity, "התור לא נמצא", Toast.LENGTH_SHORT).show();
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        }
        SharedData.deleteQueuesFragment.deleteQueueNotInRequest();
    }

    public static void deleteEmptyQueuesAns(String response)
    {
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        else
        {
            String error,queuesDeleted = null;
            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                error = jsonResponse.optString("error",null);
                if (error == null)
                    queuesDeleted = jsonResponse.getString("queuesDeleted");
            }
            catch (Exception e) {error = "yes";}
            if (error == null)
            {
                String msg;
                if (queuesDeleted.equals("0"))
                    msg = "לא נמצאו תורים";
                else
                    msg = "נמחקו " + queuesDeleted + " תורים פנויים";
                Toast.makeText(SharedData.mainActivity, msg, Toast.LENGTH_SHORT).show();
                QueuesData.askForEmptyQueues = true;
            }
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        }
        SharedData.deleteQueuesFragment.deleteQueuesNotInRequest();
    }

    public static void deleteReservedQueuesAns(String response)
    {
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        else
        {
            String error,queuesDelete = null;
            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                error = jsonResponse.optString("error",null);
                if (error == null)
                    queuesDelete = jsonResponse.getString("queuesDeleted");
            }
            catch (Exception e) {error = "yes";}
            if (error == null)
            {
                String msg;
                if (queuesDelete.equals("0"))
                    msg = "לא נמצאו תורים";
                else
                    msg = "נמחקו " + queuesDelete + " תורים קבועים";
                Toast.makeText(SharedData.mainActivity, msg, Toast.LENGTH_SHORT).show();
                QueuesData.askForReservedQueues = true;
            }
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        }
        SharedData.deleteQueuesFragment.deleteQueuesNotInRequest();
    }


    public static void deleteEmptyAndReservedQueuesAns(String response)
    {

        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        else
        {
            String error,reservedQueueDelete = null,emptyQueueDelete = null;
            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                error = jsonResponse.optString("error",null);
                if (error == null)
                {
                    reservedQueueDelete = jsonResponse.getString("reservedQueuesDeleted");
                    emptyQueueDelete = jsonResponse.getString("emptyQueuesDeleted");
                }
            }
            catch (Exception e) {error = "yes";}
            if (error == null)
            {
                int reservedQueueDeleteCount = Integer.parseInt(reservedQueueDelete);
                int emptyQueueDeleteCount = Integer.parseInt(emptyQueueDelete);
                String msg;
                if (reservedQueueDeleteCount > 0 && emptyQueueDeleteCount == 0 )
                {
                    if (reservedQueueDeleteCount == 1)
                        msg = "נמחק תור תפוס אחד";
                    else
                        msg =  "נמחקו " + reservedQueueDeleteCount + " תורים תפוסים";
                    QueuesData.askForReservedQueues = true;
                }
                else if (reservedQueueDeleteCount == 0 && emptyQueueDeleteCount > 0 )
                {
                    if (reservedQueueDeleteCount == 1)
                        msg = "נמחק תור תפוס אחד";
                    else
                        msg =  "נמחקו " + emptyQueueDeleteCount + " תורים פנויים";
                    QueuesData.askForEmptyQueues = true;
                }
                else if (reservedQueueDeleteCount == 0 && emptyQueueDeleteCount == 0 )
                    msg = "לא נמצאו תורים";
                else //(reservedQueueDeleteCount > 0 && emptyQueueDeleteCount > 0 )
                {
                    if (reservedQueueDeleteCount == 1)
                        msg = "נמחק תור תפוס אחד ו";
                    else
                        msg = "נמחקו " + reservedQueueDeleteCount + " תורים תפוסים ו";
                    if (emptyQueueDeleteCount == 1)
                        msg += "תור פנוי אחד";
                    else
                        msg += emptyQueueDelete + " תורים פנויים";
                    QueuesData.askForReservedQueues = true;
                    QueuesData.askForEmptyQueues = true;
                }
                Toast.makeText(SharedData.mainActivity,msg,Toast.LENGTH_SHORT).show();
            }
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        }
        SharedData.deleteQueuesFragment.deleteQueuesNotInRequest();
    }
}