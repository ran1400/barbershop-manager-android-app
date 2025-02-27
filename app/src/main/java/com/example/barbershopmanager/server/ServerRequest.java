package com.example.barbershopmanager.server;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.barbershopmanager.MainActivity;
import com.example.barbershopmanager.R;
import com.example.barbershopmanager.sharedDate.AddQueuesData;
import com.example.barbershopmanager.sharedDate.SharedData;
import java.util.HashMap;
import java.util.Map;

public class ServerRequest
{
    public static final String REQUEST_ERROR = "requestError";
    private Map map;
    private String url = "https://ran-y.me/barbershop/commands/";
    private ServerResponseHandle serverResponseHandle;
    public ServerRequest(ServerResponseHandle serverResponseHandle)
    {
        this.serverResponseHandle = serverResponseHandle;
        map  = new HashMap<String,String>();
        map.put("secretKey",SharedData.mainActivity.getString(R.string.serverManagerSecretKey));
    }

    public void sendNotificationToAllUsers(String title, String body)
    {
        url += "manager/send_notification_to_all_the_users.php";
        map.put("title",title);
        map.put("body",body);
        sendRequest();
    }

    public void sendNotificationToYourself(String title, String body)
    {
        url += "manager/send_notification_to_yourself.php";
        map.put("title",title);
        map.put("body",body);
        sendRequest();
    }


    public void changeReservedQueue(String userMail ,String prevQueue,String newQueue,boolean addToEmptyQueue)
    {
        url += "manager/change_reserved_queue.php";
        map.put("newQueue",newQueue);
        map.put("mail",userMail);
        map.put("prevQueue",prevQueue);
        if (addToEmptyQueue)
            map.put("addToEmptyQueue","yes");
        else
            map.put("addToEmptyQueue","no");
        sendRequest();
    }


    public void checkIfUserCmdEnabled()
    {
        url += "manager/check_if_user_cmd_enabled.php";
        sendRequest();
    }

    public void setInAppMsg(String msg)
    {
        url += "manager/write_msg.php";
        map.put("msg",msg);
        sendRequest();
    }

    public void sendMailToAllTheUsers(String mailTitle,String mailBody)
    {
        url += "manager/send_email_to_all_users.php";
        map.put("mailTitle",mailTitle);
        map.put("mailBody",mailBody.replace("\n","<br>"));
        sendRequest();
    }
    public void neverSendQueueUpdates()
    {
        setSecondsAmountToSendNotification(0);
    }

    public void everSendQueueUpdates()
    {
        setSecondsAmountToSendNotification(1);
    }

    // if have change in queue x seconds from now - notification will send to manager
    public void setSecondsAmountToSendNotification(int seconds)
    {
        url += "manager/set_seconds_amount_to_send_notification.php";
        map.put("seconds",String.valueOf(seconds));
        sendRequest();
    }

    public void setSendUserQueueNotifications(boolean bool) // if true - send notifications to user on change on his queues
    {
        url += "manager/set_send_user_queue_notifications.php";
        if (bool)
            map.put("sendNotifications","true");
        else
            map.put("sendNotifications","false");
        sendRequest();
    }

    public void setSendUserRemoveNotifications(boolean bool) // if true - send notifications to user if he removed
    {
        url += "manager/set_send_user_remove_notifications.php";
        if (bool)
            map.put("sendNotifications","true");
        else
            map.put("sendNotifications","false");
        sendRequest();
    }

    public void setSendUserBlockNotifications(boolean bool) // if true - send notifications to user if he blocked
    {
        url += "manager/set_send_user_block_notifications.php";
        if (bool)
            map.put("sendNotifications","true");
        else
            map.put("sendNotifications","false");
        sendRequest();
    }

    public void setSendUserUnblockNotifications(boolean bool) // if true - send notifications to user if he unblocked
    {
        url += "manager/set_send_user_unblock_notifications.php";
        if (bool)
            map.put("sendNotifications","true");
        else
            map.put("sendNotifications","false");
        sendRequest();
    }

    public void getNotificationsSetting()
    {
        url += "manager/get_notifications_setting.php";
        sendRequest();
    }

    public void addReservedQueue(String mail,String queue)
    {
        url += "manager/add_reserved_queue.php";
        map.put("mail",mail);
        map.put("newDate",queue);
        sendRequest();
    }

    public void unlockUserCmd()
    {
        url += "manager/unlock_user_cmd.php";
        sendRequest();
    }

    public void lockUserCmd()
    {
        url += "manager/lock_user_cmd.php";
        sendRequest();
    }

    public void getInAppMsg()
    {
        url += "manager/get_msg.php";
        sendRequest();
    }

    public void getUsersList()
    {
        url += "manager/get_all_users.php";
        sendRequest();
    }

    public void deleteReservedQueueByMail(String queue, String mail)
    {
        url += "manager/remove_reserved_queue_by_mail.php";
        map.put("date",queue);
        map.put("mail",mail);
        sendRequest();
    }

    public void cleanReservedQueue(String queue, String mail)
    {
        url += "manager/remove_reserved_queue_and_add_empty_queue.php";
        map.put("date",queue);
        map.put("mail",mail);
        sendRequest();
    }

    public void deleteEmptyQueue(String date)
    {
        url += "manager/remove_empty_queue.php";
        map.put("date", date);
        sendRequest();
    }

    public void deleteEmptyOrReservedQueue(String date)
    {
        url += "manager/remove_empty_or_reserved_queue.php";
        map.put("date", date);
        sendRequest();
    }

    public void deleteReservedQueueByTime(String date)
    {
        url += "manager/remove_reserved_queue_by_time.php";
        map.put("date", date);
        sendRequest();
    }


    public void getPastReservedQueues(String startDate,String endDate)
    {
        url += "manager/get_past_reserved_queues_between_dates.php";
        map.put("startTime",startDate);
        map.put("endTime",endDate);
        sendRequest();
    }


    public void getReservedQueues()
    {
        url += "manager/ask_for_future_reserved_queues.php";
        sendRequest();
    }

    public void getEmptyQueues()
    {
        url += "manager/ask_for_future_empty_queues.php";
        sendRequest();
    }

    public void deleteEmptyQueues(String firstDate,String secondDate)
    {
        map.put("firstDate", firstDate);
        map.put("secondDate", secondDate);
        url += "manager/remove_empty_queues_between_dates.php";
        sendRequest();
    }

    public void deleteEmptyAndReservedQueues(String firstDate,String secondDate)
    {
        map.put("firstDate", firstDate);
        map.put("secondDate", secondDate);
        url += "manager/remove_queues_between_dates.php";
        sendRequest();
    }

    public void deleteReservedQueues(String firstDate,String secondDate)
    {
        map.put("firstDate", firstDate);
        map.put("secondDate", secondDate);
        url += "manager/remove_reserved_queues_between_dates.php";
        sendRequest();
    }

    public void addEmptyQueues(String queueDays, String startHour, String endHour)
    {
        url += "manager/add_empty_queues.php";
        map.put("datesList",queueDays);
        map.put("startHour",startHour);
        map.put("endHour",endHour);
        map.put("timeBetweenQueues", AddQueuesData.addQueuesSpaceBetweenQueues);
        sendRequest();
    }

    public void addEmptyQueue(String time)
    {
        url += "manager/add_empty_queue.php";
        map.put("time",time);
        sendRequest();
    }

    public void removeUser(String userMail,String userName)
    {
        url += "manager/remove_user.php";
        map.put("mail",userMail);
        map.put("name",userName);
        sendRequest();
    }

    public void blockUser(String mail)
    {
        url += "manager/block_user.php";
        map.put("mail",mail);
        sendRequest();
    }

    public void unblockUser(String mail)
    {
        url += "manager/unblock_user.php";
        map.put("mail",mail);
        sendRequest();
    }


    private void sendRequest()
    {
        Log.d("check288",url);
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d("check288",response);
                        serverResponseHandle.doWhenGetResponseFromTheServer(response);
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.d("check288",error.toString());
                serverResponseHandle.doWhenGetResponseFromTheServer(REQUEST_ERROR);
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                return map;
            }
        };
        Log.d("check288checkMap",""+map);
        RequestQueue rq = Volley.newRequestQueue(SharedData.mainActivity);
        rq.add(sr);
    }

    public static boolean requestAnsHelper(String response)
    {
        MainActivity mainActivity = SharedData.mainActivity;
        if (response.equals(ServerRequest.REQUEST_ERROR))
        {
            Toast.makeText(mainActivity, "פניה לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (response.equals("connection failed"))
        {
            Toast.makeText(mainActivity, "התחברות לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (response.equals("cmd failed"))
        {
            Toast.makeText(mainActivity, "בקשה לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (response.equals("permission problem"))
        {
            Toast.makeText(mainActivity, "בעיית הרשאות", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean requestAnsHelperWithoutToast(String response)
    {
        if (response.equals(ServerRequest.REQUEST_ERROR))
            return false;
        if (response.equals("connection failed"))
            return false;
        if (response.equals("cmd failed"))
            return false;
        if (response.equals("permission problem"))
            return false;
        return true;
    }
}