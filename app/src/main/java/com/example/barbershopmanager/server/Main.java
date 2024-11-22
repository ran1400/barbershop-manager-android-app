package com.example.barbershopmanager.server;

import android.widget.Toast;

import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.SettingData;

public class Main
{
    public static void checkIfUserCmdEnabledAns(String response)
    {
        response = response.trim();
        if(response.equals("true"))
            SettingData.userCmdIsLock = false;
        else if (response.equals("false"))
        {
            Toast.makeText(SharedData.mainActivity, "שים לב - המערכת נעולה לקביעת/שינוי תורים", Toast.LENGTH_SHORT).show();
            SettingData.userCmdIsLock = true;
        }
        else if(response.equals("permission problem"))
            Toast.makeText(SharedData.mainActivity, "בעיית הרשאות", Toast.LENGTH_SHORT).show();
        else if (response.equals(ServerRequest.REQUEST_ERROR) || response.equals("cmd failed"))
        {
            SharedData.mainActivity.showLoadingWindowContent();
            return;
        }
        SharedData.mainActivity.thereIsInternet();
    }
}
