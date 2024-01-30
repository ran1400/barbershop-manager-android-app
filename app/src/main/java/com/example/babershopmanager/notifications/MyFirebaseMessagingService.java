package com.example.babershopmanager.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.babershopmanager.MainActivity;
import com.example.babershopmanager.R;
import com.example.babershopmanager.sharedDate.SharedData;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String URL = "https://fcm.googleapis.com/fcm/send";

    public static void sendNotification(String topic, String title, String body, String channelId) {
        RequestQueue mRequestQue = Volley.newRequestQueue(SharedData.mainActivity);
        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/" + topic);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("android_channel_id", channelId);
            notificationObj.put("title", title);
            notificationObj.put("body", body);
            json.put("notification", notificationObj);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            SharedData.sendMsgFragment.sendPushMsgNotInRequest();
                            Toast.makeText(SharedData.mainActivity, "ההודעה נשלחה", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    SharedData.sendMsgFragment.sendPushMsgNotInRequest();
                    Toast.makeText(SharedData.mainActivity, "השליחה נכשלה", Toast.LENGTH_SHORT).show();
                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", SharedData.mainActivity.getString(R.string.notificationAuthorizationKey));
                    return header;
                }
            };
            mRequestQue.add(request);
        } catch (JSONException e) {
        }
    }

    public static void sendNotificationToAllUsers(String title, String body, boolean quietMsg) {
        String channelId;
        if (quietMsg)
            channelId = "quietManagerMsg";
        else
            channelId = "managerMsg";
        sendNotification("managerMsgs", title, body, channelId);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String channelId = remoteMessage.getNotification().getChannelId();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);
        manager.notify(0, builder.build());
    }
}