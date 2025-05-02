package com.example.barbershopmanager.server;

import android.widget.Toast;

import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.SettingData;

import org.json.JSONObject;

public class Main
{
    public static void checkIfUserCmdEnabledAns(String response)
    {
        if (response == ServerRequest.REQUEST_ERROR)
            SharedData.mainActivity.showLoadingWindowContent();
        else
        {
            String error;
            Boolean userCmdLock = null;
            JSONObject jsonResponse;
            try
            {
                jsonResponse = new JSONObject(response);
                error = jsonResponse.optString("error", null);
                if (error == null)
                    userCmdLock = jsonResponse.getBoolean("userCmdLock");
            }
            catch (Exception e) {error = "yes";}
            if (error != null) // error is : yes || sql connection failed || permission problem || start with cmd failed
                SharedData.mainActivity.showLoadingWindowContent();
            else
            {
                if (userCmdLock)
                {
                    Toast.makeText(SharedData.mainActivity, "שים לב - המערכת נעולה לקביעת/שינוי תורים", Toast.LENGTH_SHORT).show();
                    SettingData.userCmdIsLock = true;
                }
                else
                    SettingData.userCmdIsLock = false;
                SharedData.mainActivity.thereIsInternet();
            }
        }
    }
}
