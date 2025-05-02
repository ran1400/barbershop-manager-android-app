package com.example.barbershopmanager.server;

import android.view.View;
import android.widget.Toast;

import com.example.barbershopmanager.sharedDate.QueuesData;
import com.example.barbershopmanager.utils.dataStructures.ReservedQueue;
import com.example.barbershopmanager.sharedDate.SharedData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ReservedQueues
{

    public static ReservedQueue[] makeReservedQueuesArray(JSONArray queues)
    {
        try
        {
            int queuesAmount = queues.length();
            ReservedQueue[] res = new ReservedQueue[queuesAmount];
            for ( int i = 0 ; i < queuesAmount ; i++)
            {
                JSONObject queue = queues.getJSONObject(i);
                res[i] = new ReservedQueue( queue.getString("Time"),
                                            queue.getString("Name"),
                                            queue.getString("Phone"),
                                            queue.getString("Mail"));
            }
            return res;
        }
        catch (JSONException e) {return null;}
    }

    public static void doIfAskForQueuesFailed()
    {
        QueuesData.askForReservedQueues = true;
        QueuesData.haveInternet = false;
        SharedData.queuesFragment.showNoInternet();
        SharedData.reservedQueuesFragment.createReservedQueuesViewList();
    }


    public static void getReservedQueuesAns(String response)
    {
        SharedData.reservedQueuesFragment.loadingView.setVisibility(View.INVISIBLE);
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
        QueuesData.askForReservedQueues = false;
        QueuesData.haveInternet = true;
        if (SharedData.queuesFragment != null)
            SharedData.queuesFragment.goneNoInternet();
        QueuesData.reservedQueuesArray = makeReservedQueuesArray(queues);
        if (SharedData.queuesFragment != null && SharedData.queuesFragment.reservedQueuesIsChecked()) //if queue view was visible
        {
            SharedData.reservedQueuesFragment.createReservedQueuesViewList();
            SharedData.reservedQueuesFragment.makeVisible();
        }
    }

    public static void changeReservedQueueAns(String response)
    {
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SharedData.mainActivity,"התור שונה", Toast.LENGTH_SHORT).show();
                SharedData.queuesFragment.refreshQueues();
            }
            else if(error.equals("queue exist"))
                Toast.makeText(SharedData.mainActivity,"הפעולה נכשלה - התור תפוס על ידי מישהו אחר", Toast.LENGTH_SHORT).show();
            else
            {   // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "הפעולה נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
                if (response.startsWith("cmd failed"))
                    SharedData.queuesFragment.refreshQueues();
            }
        }
        SharedData.reservedQueuesFragment.doWhenPopUpServerAns();
    }

    public static void cleanReservedQueueAns(String response)
    {
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SharedData.mainActivity,"התור התווסף לתורים הריקים", Toast.LENGTH_SHORT).show();
                SharedData.queuesFragment.refreshQueues();
            }
            else
            {   // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "הפעולה נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
                if (response.startsWith("cmd failed"))
                    SharedData.queuesFragment.refreshQueues();
            }
        }
        SharedData.reservedQueuesFragment.doWhenPopUpServerAns();
    }

    public static void deleteReservedQueueAns(String response)
    {
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
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
                SharedData.queuesFragment.refreshQueues();
            }
            else
            {   // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "הפעולה נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
                if (response.startsWith("cmd failed"))
                    SharedData.queuesFragment.refreshQueues();
            }
        }
        SharedData.reservedQueuesFragment.doWhenPopUpServerAns();
    }
}
