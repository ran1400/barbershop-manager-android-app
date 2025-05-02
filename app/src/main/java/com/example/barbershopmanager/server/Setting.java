package com.example.barbershopmanager.server;

import android.util.Log;
import android.widget.Toast;

import com.example.barbershopmanager.fragments.SettingFragments.NotificationsFragment;
import com.example.barbershopmanager.fragments.SettingFragments.ShowUsersFragment;
import com.example.barbershopmanager.sharedDate.QueuesData;
import com.example.barbershopmanager.utils.dataStructures.User;
import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.SettingData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;


public class Setting
{
    static public void getNotificationsSettingAns(String response)
    {
        SettingData.getSettingFragmentInRequest = false;
        if (response == ServerRequest.REQUEST_ERROR)
        {
            Toast.makeText(SharedData.mainActivity, "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
            SharedData.settingFragment.showNoneBtnIsClicked();
        }
        else
        {
            String error;
            String secondsAmountToGetNotification = null;
            String sendUserBlockNotifications = null;
            String sendUserUnblockNotifications = null;
            String sendUserRemoveNotifications = null;
            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                error = jsonResponse.optString("error",null);
                if (error == null)
                {
                    secondsAmountToGetNotification = jsonResponse.getString("secondsToSendQueueNotification");
                    sendUserBlockNotifications = jsonResponse.getString("sendUserBlockNotification");
                    sendUserUnblockNotifications = jsonResponse.getString("sendUserUnblockNotification");
                    sendUserRemoveNotifications = jsonResponse.getString("sendUserRemoveNotification");
                }

            }
            catch (Exception e){error = "yes";}
            if (error == null)
            {
                SettingData.secondsAmountToGetNotification = Integer.parseInt(secondsAmountToGetNotification);
                if (SettingData.secondsAmountToGetNotification == 2000000) // 2000000 seconds define in server as EVER
                    SettingData.secondsAmountToGetNotification = NotificationsFragment.EVER;
                SettingData.sendUserBlockNotifications =  sendUserBlockNotifications.equals("1");
                SettingData.sendUserUnblockNotifications =  sendUserUnblockNotifications.equals("1");
                SettingData.sendUserRemoveNotifications =  sendUserRemoveNotifications.equals("1");
                if(SharedData.isSettingCrntWindows())
                    SharedData.settingFragment.showNotificationFragment();
            }
            else
            {   // error is : yes || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
                SharedData.settingFragment.showNoneBtnIsClicked();
            }
        }
        SharedData.settingFragment.checkIfRemoveLoadingView();
    }

    public static void setSendUserBlockNotificationsAns(String response)
    {
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        else
        {
            String error;
            String sendUserBlockNotification = null;
            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                error = jsonResponse.optString("error",null);
                if (error == null)
                    sendUserBlockNotification = jsonResponse.getString("sendUserBlockNotification");
            }
            catch (Exception e){error = "yes";}
            if (error == null)
            {
                SettingData.sendUserBlockNotifications =  sendUserBlockNotification.equals("1");
                SharedData.notificationsFragment.sendUserBlockNotificationsRefreshSwitch();
            }
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        }
        SettingData.sendUserBlockNotificationsInRequest = false;
        SharedData.notificationsFragment.enableSendNotificationsBlockUser();
        SharedData.notificationsFragment.checkIfGoneLoadingView();
    }

    public static void setSendUserRemoveNotificationsAns(String response)
    {
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        else
        {
            String error ;
            String sendUserRemoveNotification = null;
            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                error = jsonResponse.optString("error",null);
                if (error == null)
                    sendUserRemoveNotification = jsonResponse.getString("sendUserRemoveNotification");
            }
            catch (Exception e){error = "yes";}
            if (error == null)
            {
                SettingData.sendUserRemoveNotifications = sendUserRemoveNotification.equals("1");
                SharedData.notificationsFragment.sendUserRemoveNotificationsRefreshSwitch();
            }
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        }
        SettingData.sendUserRemoveNotificationsInRequest = false;
        SharedData.notificationsFragment.enableSendNotificationsRemoveUser();
        SharedData.notificationsFragment.checkIfGoneLoadingView();
    }

    public static void setSendUserUnblockNotificationsAns(String response)
    {
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        else
        {
            String error;
            String sendUserUnblockNotification = null;
            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                error = jsonResponse.optString("error",null);
                if (error == null)
                    sendUserUnblockNotification = jsonResponse.getString("sendUserUnblockNotification");
            }
            catch (Exception e){error = "yes";}
            if (error == null)
            {
                SettingData.sendUserUnblockNotifications =  sendUserUnblockNotification.equals("1");
                SharedData.notificationsFragment.sendUserUnblockNotificationsRefreshSwitch();
            }
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        }
        SettingData.sendUserUnblockNotificationsInRequest = false;
        SharedData.notificationsFragment.enableSendNotificationsUnblockUser();
        SharedData.notificationsFragment.checkIfGoneLoadingView();
    }



    public static void setSecondsAmountToSendNotificationAns(String response)
    {
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        else
        {
            String error,seconds = null;
            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                error = jsonResponse.optString("error",null);
                if (error == null)
                    seconds = jsonResponse.getString("seconds");
            }
            catch (Exception e){error = "yes";}
            if (error == null)
            {
                SettingData.secondsAmountToGetNotification = Integer.parseInt(seconds);
                if (SettingData.secondsAmountToGetNotification == 2000000) // 2000000 seconds define in server as EVER
                    SettingData.secondsAmountToGetNotification = NotificationsFragment.EVER;
                Toast.makeText(SharedData.mainActivity, "קבלת התראות עודכנה", Toast.LENGTH_SHORT).show();
                SharedData.notificationsFragment.fillInfoFromServerToPickers();
            }
            else // error is : yes || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        }
        SettingData.setSecondsAmountToSendNotificationInRequest = false;
        SharedData.notificationsFragment.enabledPickersArea();
        SharedData.notificationsFragment.checkIfGoneLoadingView();
    }

    static public void getInAppMsgContentAns(String response)
    {
        SettingData.setInAppMsgInRequest = false;
        if (response == ServerRequest.REQUEST_ERROR)
        {
            Toast.makeText(SharedData.mainActivity, "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
            SharedData.sendMsgFragment.inAppMsgNotInRequest(false);
        }
        else
        {
            String error,msg = null;
            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                error = jsonResponse.optString("error",null);
                if (error == null)
                    msg = jsonResponse.getString("msg");
            }
            catch (Exception e){error = "yes";}
            if (error == null)
            {
                if (msg.equals("NULL"))
                    msg = null;
                SharedData.sendMsgFragment.showInAppMsgWindow(msg);
                SharedData.sendMsgFragment.inAppMsgNotInRequest(true);
            }
            else
            {   // error is : yes || permission problem || start with cmd failed
                if (error.equals("permission problem"))
                    Toast.makeText(SharedData.mainActivity, "בעיית הרשאות", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
                SharedData.sendMsgFragment.inAppMsgNotInRequest(false);
            }
        }
    }

    static public void setInAppMsgAns(String response)
    {
        SettingData.setInAppMsgInRequest = false;
        if (response == ServerRequest.REQUEST_ERROR)
        {
            Toast.makeText(SharedData.mainActivity, "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
            SharedData.sendMsgFragment.inAppMsgNotInRequest(true);
        }
        else
        {
            String error,msg = null;
            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                error = jsonResponse.optString("error",null);
                if (error == null)
                    msg = jsonResponse.getString("msg");
            }
            catch (Exception e){error = "yes";}
            if (error == null)
            {
                Toast.makeText(SharedData.mainActivity, "ההודעה עודכנה", Toast.LENGTH_SHORT).show();
                if (msg.equals("NULL"))
                    msg = null;
                SharedData.sendMsgFragment.showInAppMsgWindow(msg);
                SharedData.sendMsgFragment.inAppMsgNotInRequest(true);
            }
            else
            {   // error is : yes || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
                SharedData.sendMsgFragment.inAppMsgNotInRequest(true);
            }
        }
    }

    static public void sendMailToAllTheUsersAns(String response)
    {
        SettingData.sendMailInRequest = false;
        SharedData.sendMsgFragment.sendMailNotInRequest();
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - התור לא בוטל\nנסה שוב", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SharedData.mainActivity, "המייל נשלח", Toast.LENGTH_SHORT).show();
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "הפעולה נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
        }
    }

    static public void sendNotificationAns(String response)
    {
        SettingData.sendPushMsgInRequest = false;
        SharedData.sendMsgFragment.sendNotificationMsgNotInRequest();

        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - התור לא בוטל\nנסה שוב", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SharedData.mainActivity, "ההודעה נשלחה", Toast.LENGTH_SHORT).show();
            else // error is : yes || sql connection failed || permission problem || cmd failed
                Toast.makeText(SharedData.mainActivity, "הפעולה נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
        }
    }

    public static void deleteQueueAns(String response)
    {
        SettingData.showUserFragmentInRequest = false;
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - התור לא בוטל\nנסה שוב", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SharedData.mainActivity, "התור בוטל", Toast.LENGTH_SHORT).show();
                refreshUsersAndQueuesList();
            }
            else
            {   // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - התור לא בוטל\nנסה שוב", Toast.LENGTH_SHORT).show();
                if (response.startsWith("cmd failed"))
                    refreshUsersAndQueuesList();
            }
        }
        SharedData.userOptionsFragment.doWhenGetResponseFromTheServer();
    }

    public static void cleanQueueAns(String response)
    {
        SettingData.showUserFragmentInRequest = false;
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - התור לא בוטל\nנסה שוב", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SharedData.mainActivity, "התור בוטל והועבר לרשימת התורים הפנויים", Toast.LENGTH_SHORT).show();
                refreshUsersAndQueuesList();
            }
            else
            {   // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - התור לא בוטל\nנסה שוב", Toast.LENGTH_SHORT).show();
                if (response.startsWith("cmd failed"))
                    refreshUsersAndQueuesList();
            }
        }
        SharedData.userOptionsFragment.doWhenGetResponseFromTheServer();
    }

    private static void refreshUsersList()
    {
        if (SharedData.isSettingCrntWindows() &&
                (SettingData.menuBtnClicked == SettingData.MenuBtnClicked.USER || SettingData.menuBtnClicked == SettingData.MenuBtnClicked.GET_USERS))
        {
            SharedData.settingFragment.askForUsersList();
            SharedData.settingFragment.showGetUsersIsClicked();
        }
        else
        {
            SettingData.menuBtnClicked = null;
            SettingData.askForUsersList = true;
        }
    }

    private static void refreshUsersAndQueuesList()
    {
        QueuesData.askForEmptyQueues = true;
        QueuesData.askForReservedQueues = true;
        refreshUsersList();
    }
    public static void addQueueAns(String response)
    {
        SettingData.showUserFragmentInRequest = false;
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - התור לא נקבע\nנסה שוב", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SharedData.mainActivity,"התור נוסף", Toast.LENGTH_SHORT).show();
                refreshUsersAndQueuesList();
            }
            else if(error.equals("queue exist"))
                Toast.makeText(SharedData.mainActivity,"קביעת התור נכשלה - התור תפוס על ידי מישהו אחר", Toast.LENGTH_SHORT).show();
            else
            {   // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "קביעת התור נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
                if (response.startsWith("cmd failed"))
                    refreshUsersAndQueuesList();
            }
        }
        SharedData.userOptionsFragment.doWhenGetResponseFromTheServer();
    }

    public static void changeQueueAns(String response)
    {
        SettingData.showUserFragmentInRequest = false;

        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - התור לא שונה\nנסה שוב", Toast.LENGTH_SHORT).show();
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
                refreshUsersAndQueuesList();
            }
            else if(error.equals("queue exist"))
                Toast.makeText(SharedData.mainActivity,"שינוי התור נכשלה - התור תפוס על ידי מישהו אחר", Toast.LENGTH_SHORT).show();
            else
            {   // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "קביעת התור נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
                if (response.startsWith("cmd failed"))
                    refreshUsersAndQueuesList();
            }
        }
        SharedData.userOptionsFragment.doWhenGetResponseFromTheServer();
    }

    public static void unblockUserAns(String response)
    {
        SettingData.showUserFragmentInRequest = false;
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - ביטול חסימת המשתמש נכשל\nנסה שוב", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SharedData.mainActivity,"חסימת המשתמש בוטלה", Toast.LENGTH_SHORT).show();
                refreshUsersList();
            }
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - ביטול חסימת המשתמש נכשל\nנסה שוב", Toast.LENGTH_SHORT).show();
        }
        SharedData.userOptionsFragment.doWhenGetResponseFromTheServer();
    }

    public static void blockUserAns(String response,boolean haveQueue)
    {
        SettingData.showUserFragmentInRequest = false;
        if (response == ServerRequest.REQUEST_ERROR)
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - המשתמש לא נחסם\nנסה שוב", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SharedData.mainActivity,"המשתמש נחסם", Toast.LENGTH_SHORT).show();
                if(haveQueue)
                    refreshUsersAndQueuesList();
                else
                    refreshUsersList();
            }
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - המשתמש לא נחסם\nנסה שוב", Toast.LENGTH_SHORT).show();
        }
        SharedData.userOptionsFragment.doWhenGetResponseFromTheServer();
    }

    public static void removeUserAns(String response,boolean haveQueue)
    {
        SettingData.showUserFragmentInRequest = false;
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
                Toast.makeText(SharedData.mainActivity,"המשתמש נמחק", Toast.LENGTH_SHORT).show();
                if(haveQueue)
                    refreshUsersAndQueuesList();
                else
                    refreshUsersList();
            }
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה", Toast.LENGTH_SHORT).show();
        }
        SharedData.userOptionsFragment.doWhenGetResponseFromTheServer();
    }

    private static class Users
    {
        User[] users;
        LinkedList<Integer> blockedUsers;
        LinkedList<Integer> haveQueue;
        LinkedList<Integer> notHaveQueue;

        public Users(JSONArray usersJsonArray) throws JSONException
        {
            users = new User[usersJsonArray.length()];
            blockedUsers = new LinkedList<Integer>();
            haveQueue = new LinkedList<Integer>();
            notHaveQueue = new LinkedList<Integer>();
            for (int i = 0 ; i < this.users.length ; i++)
            {
                JSONObject user = usersJsonArray.getJSONObject(i);
                String name = user.getString("Name");
                String phone = user.getString("Phone");
                String mail =  user.getString("Mail");
                String queue = user.getString("Queue");
                if (queue.equals("X"))
                    queue = null;
                Boolean block = user.getString("Block").equals("1");
                users[i] = new User(name,phone,mail,queue,block);
                if (block)
                    blockedUsers.add(i);
                else if (queue == null)
                    notHaveQueue.add(i);
                else
                    haveQueue.add(i);
            }
        }

        public User[] getUsers(LinkedList<Integer> type)
        {
           User[] res = new User[type.size()];
           int i = 0;
           for (int user : type)
               res[i++] = users[user];
           return res;
        }

    }

    private static void getUsersListAnsHelper(JSONArray usersJsonArray) throws JSONException
    {
        Users users = new Users(usersJsonArray);
        SettingData.usersWithQueue = users.getUsers(users.haveQueue);
        SettingData.usersWithoutQueue = users.getUsers(users.notHaveQueue);
        SettingData.blockedUsers = users.getUsers(users.blockedUsers);
    }

    private static void doIfGetUsersFailed()
    {
        Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
        if (SettingData.usersWithoutQueue != null)
        {
            SettingData.askForUsersList = false;
            SharedData.settingFragment.showUsersFragment();
        }
        else
            SharedData.settingFragment.showNoneBtnIsClicked();
    }

    public static void getUsersListAns(String response)
    {
        if (response == ServerRequest.REQUEST_ERROR)
            doIfGetUsersFailed();
        else
        {
            String error;
            JSONObject jsonResponse;
            try
            {
                jsonResponse = new JSONObject(response);
                error = jsonResponse.optString("error", null);
                if (error == null)
                    getUsersListAnsHelper(jsonResponse.getJSONArray("users"));
            }
            catch (Exception e) {error = "yes";}
            if(error != null) // error is : yes || sql connection failed || permission problem || start with cmd failed
                doIfGetUsersFailed();
            else
            {
                SettingData.askForUsersList = false;
                ShowUsersFragment.initScrollViewLocation(); //static method in fragments.settingFragments.ShowUsersFragments
                if (SharedData.isSettingCrntWindows())
                    SharedData.settingFragment.showUsersFragment();
            }
        }
        SettingData.getUsersListInRequest = false;
        SharedData.settingFragment.checkIfRemoveLoadingView();
    }

    private static boolean responseErrorHandle(String response)
    {
        if (response.equals("permission problem"))
        {
            Toast.makeText(SharedData.mainActivity, "פעולה לא בוצעה - בעיית הרשאות", Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if (response.equals(ServerRequest.REQUEST_ERROR))
        {
            Toast.makeText(SharedData.mainActivity, "פעולה לא בוצעה - אין גישה לשרת", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static void lockUserCmdAns(String response)
    {

        if (response == ServerRequest.REQUEST_ERROR)
        {
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
            SharedData.settingFragment.setBlockSystemSwitch(false);
        }
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
                SettingData.userCmdIsLock = true;
                SharedData.settingFragment.setBlockSystemSwitch(true);
                Toast.makeText(SharedData.mainActivity, "המערכת ננעלה לקביעת / שינוי תורים", Toast.LENGTH_SHORT).show();
            }
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
            {
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
                SharedData.settingFragment.setBlockSystemSwitch(false);
            }
        }
        SettingData.userCmdIsLockInRequest = false;
        SharedData.settingFragment.checkIfRemoveLoadingView();
        SharedData.settingFragment.setBlockSystemSwitchEnabled();
    }

    public static void unlockUserCmdAns(String response)
    {
        if (response == ServerRequest.REQUEST_ERROR)
        {
            Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
            SharedData.settingFragment.setBlockSystemSwitch(true);
        }
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
                SettingData.userCmdIsLock = false;
                SharedData.settingFragment.setBlockSystemSwitch(false);
                Toast.makeText(SharedData.mainActivity, "המערכת פעילה", Toast.LENGTH_SHORT).show();
            }
            else // error is : yes || sql connection failed || permission problem || start with cmd failed
            {
                Toast.makeText(SharedData.mainActivity, "פניה לשרת נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
                SharedData.settingFragment.setBlockSystemSwitch(true);
            }
        }
        SettingData.userCmdIsLockInRequest = false;
        SharedData.settingFragment.checkIfRemoveLoadingView();
        SharedData.settingFragment.setBlockSystemSwitchEnabled();
    }
}
