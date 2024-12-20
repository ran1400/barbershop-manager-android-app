package com.example.barbershopmanager.server;

import static com.example.barbershopmanager.sharedDate.SharedData.mainActivity;

import android.widget.Toast;

import com.example.barbershopmanager.fragments.SettingFragments.ShowUsersFragment;
import com.example.barbershopmanager.sharedDate.QueuesData;
import com.example.barbershopmanager.utils.dataStructures.User;
import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.SettingData;


public class Setting
{


    static public void getNotificationsSettingAns(String response)
    {
        SettingData.getSettingFragmentInRequest = false;
        if (response.equals(ServerRequest.REQUEST_ERROR))
        {
            Toast.makeText(mainActivity, "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
            SettingData.menuBtnClicked = null;
            SharedData.settingFragment.showNoneBtnIsClicked();
        }
        else if (response.equals("permission problem"))
        {
            Toast.makeText(mainActivity, "בעיית הרשאות", Toast.LENGTH_SHORT).show();
            SettingData.menuBtnClicked = null;
            SharedData.settingFragment.showNoneBtnIsClicked();
        }
        else
        {
            String[] splitResponse = response.split(">");
            //splitResponse[0] is ever(-1) never(0) or num of seconds
            SettingData.secondsAmountToGetNotification = Integer.parseInt(splitResponse[0]);
            SettingData.sendUserQueueNotifications = splitResponse[1].equals("t");
            SettingData.sendUserBlockNotifications =  splitResponse[2].equals("t");
            SettingData.sendUserUnblockNotifications =  splitResponse[3].equals("t");
            SettingData.sendUserRemoveNotifications =  splitResponse[4].equals("t");
            if(SharedData.isSettingCrntWindows())
                SharedData.settingFragment.showNotificationFragment();
        }
        SharedData.settingFragment.checkIfRemoveLoadingView();
    }

    public static void setSendUserBlockNotificationsAns(String response)
    {
        if (response.equals("true"))
        {
            SettingData.sendUserBlockNotifications = true;
            SharedData.notificationsFragment.sendUserBlockNotificationsRefreshSwitch();
        }
        else if (response.equals("false"))
        {
            SettingData.sendUserBlockNotifications = false;
            SharedData.notificationsFragment.sendUserBlockNotificationsRefreshSwitch();
        }
        else if (response.equals(ServerRequest.REQUEST_ERROR))
            Toast.makeText(mainActivity, "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
        else if (response.equals("permission problem"))
            Toast.makeText(mainActivity, "בעיית הרשאות", Toast.LENGTH_SHORT).show();
        SettingData.sendUserBlockNotificationsInRequest = false;
        SharedData.notificationsFragment.enableSendNotificationsBlockUser();
        SharedData.notificationsFragment.checkIfGoneLoadingView();
    }

    public static void setSendUserRemoveNotificationsAns(String response)
    {
        if(response.equals("true"))
        {
            SettingData.sendUserRemoveNotifications = true;
            SharedData.notificationsFragment.sendUserRemoveNotificationsRefreshSwitch();
        }
        else if(response.equals("false"))
        {
            SettingData.sendUserRemoveNotifications = false;
            SharedData.notificationsFragment.sendUserRemoveNotificationsRefreshSwitch();
        }
        else if (response.equals(ServerRequest.REQUEST_ERROR))
            Toast.makeText(mainActivity, "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
        else if (response.equals("permission problem"))
            Toast.makeText(mainActivity, "בעיית הרשאות", Toast.LENGTH_SHORT).show();
        SettingData.sendUserRemoveNotificationsInRequest = false;
        SharedData.notificationsFragment.enableSendNotificationsRemoveUser();
        SharedData.notificationsFragment.checkIfGoneLoadingView();
    }

    public static void setSendUserUnblockNotificationsAns(String response)
    {
        if (response.equals("true"))
        {
            SettingData.sendUserUnblockNotifications = true;
            SharedData.notificationsFragment.sendUserUnblockNotificationsRefreshSwitch();
        }
        else if (response.equals("false"))
        {
            SettingData.sendUserUnblockNotifications = false;
            SharedData.notificationsFragment.sendUserUnblockNotificationsRefreshSwitch();
        }
        if (response.equals(ServerRequest.REQUEST_ERROR))
            Toast.makeText(mainActivity, "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
        else if (response.equals("permission problem"))
            Toast.makeText(mainActivity, "בעיית הרשאות", Toast.LENGTH_SHORT).show();
        SettingData.sendUserUnblockNotificationsInRequest = false;
        SharedData.notificationsFragment.enableSendNotificationsUnblockUser();
        SharedData.notificationsFragment.checkIfGoneLoadingView();
    }

    public static void setSendUserQueueNotificationsAns(String response)
    {
        if (response.equals("true"))
        {
            SettingData.sendUserQueueNotifications = true;
            SharedData.notificationsFragment.sendUserQueueNotificationsRefreshSwitch();
        }
        else if (response.equals("false"))
        {
            SettingData.sendUserQueueNotifications = false;
            SharedData.notificationsFragment.sendUserQueueNotificationsRefreshSwitch();
        }
        else if (response.equals(ServerRequest.REQUEST_ERROR))
            Toast.makeText(mainActivity, "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
        else if (response.equals("permission problem"))
            Toast.makeText(mainActivity, "בעיית הרשאות", Toast.LENGTH_SHORT).show();
        SettingData.sendUserQueueNotificationsInRequest = false;
        SharedData.notificationsFragment.enableSendNotificationsQueuesUpdates();
        SharedData.notificationsFragment.checkIfGoneLoadingView();
    }



    public static void setSecondsAmountToSendNotificationAns(String response)
    {
        if (response.equals(ServerRequest.REQUEST_ERROR))
            Toast.makeText(mainActivity, "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
        else if (response.equals("permission problem"))
            Toast.makeText(mainActivity, "בעיית הרשאות", Toast.LENGTH_SHORT).show();
        else
        {
            SettingData.secondsAmountToGetNotification = Integer.parseInt(response);
            Toast.makeText(mainActivity, "קבלת התראות עודכנה", Toast.LENGTH_SHORT).show();
            SharedData.notificationsFragment.fillInfoFromServerToPickers();
        }
        SettingData.setSecondsAmountToSendNotificationInRequest = false;
        SharedData.notificationsFragment.enabledPickersArea();
        SharedData.notificationsFragment.checkIfGoneLoadingView();
    }

    static public void getInAppMsgContentAns(String response)
    {
        SettingData.setInAppMsgInRequest = false;
        SharedData.sendMsgFragment.inAppMsgNotInRequest();
        if (response.equals(ServerRequest.REQUEST_ERROR))
            Toast.makeText(mainActivity, "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
        else if (response.equals("permission problem"))
            Toast.makeText(mainActivity, "בעיית הרשאות", Toast.LENGTH_SHORT).show();
        else
            SharedData.sendMsgFragment.showInAppMsgWindow(response);
    }

    static public void setInAppMsgAns(String response)
    {
        SettingData.setInAppMsgInRequest = false;
        SharedData.sendMsgFragment.inAppMsgNotInRequest();
        if (response.equals(ServerRequest.REQUEST_ERROR))
            Toast.makeText(mainActivity, "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
        else if (response.equals("permission problem"))
            Toast.makeText(mainActivity, "בעיית הרשאות", Toast.LENGTH_SHORT).show();
        else
        {
            Toast.makeText(mainActivity, "ההודעה עודכנה", Toast.LENGTH_SHORT).show();
            SharedData.sendMsgFragment.showInAppMsgWindow(response);
        }
    }

    static public void sendMailToAllTheUsersAns(String response)
    {
        SettingData.sendMailInRequest = false;
        SharedData.sendMsgFragment.sendMailNotInRequest();
        if (response.equals("V"))
            Toast.makeText(mainActivity, "המייל נשלח", Toast.LENGTH_SHORT).show();
        else if (response.equals(ServerRequest.REQUEST_ERROR))
            Toast.makeText(mainActivity, "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
        else if (response.equals("connection failed") || response.equals("cmd failed"))
            Toast.makeText(mainActivity, "הפעולה נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
        else // response.equals("permission problem")
            Toast.makeText(mainActivity, "בעיית הרשאות", Toast.LENGTH_SHORT).show();
    }

    static public void sendNotificationAns(String response)
    {
        SettingData.sendPushMsgInRequest = false;
        SharedData.sendMsgFragment.sendNotificationMsgNotInRequest();
        if (response.equals("V"))
            Toast.makeText(SharedData.mainActivity, "ההודעה נשלחה", Toast.LENGTH_SHORT).show();
        else if (response.equals(ServerRequest.REQUEST_ERROR))
            Toast.makeText(mainActivity, "אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
        else if (response.equals("cmd failed"))
            Toast.makeText(mainActivity, "הפעולה נכשלה - נסה שוב", Toast.LENGTH_SHORT).show();
        else // response.equals("permission problem")
            Toast.makeText(mainActivity, "בעיית הרשאות", Toast.LENGTH_SHORT).show();
    }

    public static void deleteQueueAns(String response)
    {
        SettingData.showUserFragmentInRequest = false;
        if (response.equals("V"))
        {
            Toast.makeText(mainActivity, "התור נמחק", Toast.LENGTH_SHORT).show();
            QueuesData.askForReservedQueues=true;
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
        else
            ServerRequest.requestAnsHelper(response); //make toast
        SharedData.userOptionsFragment.doWhenGetResponseFromTheServer();
    }

    public static void cleanQueueAns(String response)
    {
        SettingData.showUserFragmentInRequest = false;
        if (response.equals("V"))
        {
            Toast.makeText(mainActivity, "התור בוטל והועבר לרשימת התורים הפנויים", Toast.LENGTH_SHORT).show();
            QueuesData.askForReservedQueues=true;
            QueuesData.askForEmptyQueues=true;
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
        else
            ServerRequest.requestAnsHelper(response); //make toast
        SharedData.userOptionsFragment.doWhenGetResponseFromTheServer();
    }


    public static void addQueueAns(String response)
    {
        SettingData.showUserFragmentInRequest = false;
        if (response.equals("V"))
        {
            String msg= "התור נוסף";
            Toast.makeText(mainActivity, msg, Toast.LENGTH_SHORT).show();
            QueuesData.askForEmptyQueues = true;
            QueuesData.askForReservedQueues = true;
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
            return;
        }
        else if (response.equals("queue exist"))
            Toast.makeText(mainActivity, "קביעת התור נכשלה - התור תפוס על ידי מישהו אחר", Toast.LENGTH_SHORT).show();
        else
            ServerRequest.requestAnsHelper(response); //make toast
        SharedData.userOptionsFragment.doWhenGetResponseFromTheServer();
    }

    public static void changeQueueAns(String response)
    {
        SettingData.showUserFragmentInRequest = false;
        if(response.equals("V"))
        {
            String msg= "התור שונה";
            Toast.makeText(mainActivity, msg, Toast.LENGTH_SHORT).show();
            QueuesData.askForReservedQueues=true;
            QueuesData.askForEmptyQueues = true;
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
        else if (response.equals("queue exist"))
            Toast.makeText(mainActivity, "תור קיים", Toast.LENGTH_SHORT).show();
        else
            ServerRequest.requestAnsHelper(response); //make toast
        SharedData.userOptionsFragment.doWhenGetResponseFromTheServer();
    }

    public static void unblockUserAns(String response)
    {
        SettingData.showUserFragmentInRequest = false;
        if(response.equals("V"))
        {
            Toast.makeText(mainActivity,"חסימת המשתמש הוסרה", Toast.LENGTH_SHORT).show();
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
        else
            ServerRequest.requestAnsHelper(response); //make toast
        SharedData.userOptionsFragment.doWhenGetResponseFromTheServer();
    }

    public static void blockUserAns(String response,boolean haveQueue)
    {
        SettingData.showUserFragmentInRequest = false;
        if(response.equals("V"))
        {
            Toast.makeText(mainActivity,"המשתמש נחסם", Toast.LENGTH_SHORT).show();
            if (haveQueue)
                QueuesData.askForReservedQueues=true;
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
        else
            ServerRequest.requestAnsHelper(response); //make toast
        SharedData.userOptionsFragment.doWhenGetResponseFromTheServer();
    }

    public static void removeUserAns(String response,boolean haveQueue)
    {
        SettingData.showUserFragmentInRequest = false;
        if (response.equals("V"))
        {
            Toast.makeText(mainActivity,"המשתמש נמחק", Toast.LENGTH_SHORT).show();
            if (haveQueue)
                QueuesData.askForReservedQueues=true;
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
        else
            ServerRequest.requestAnsHelper(response); //make toast
        SharedData.userOptionsFragment.doWhenGetResponseFromTheServer();
    }

    private static void getUsersListAnsHelper(String response)
    {
        String splitResponse[] = response.split("<");
        int blockedUsersAmount = Integer.parseInt(splitResponse[splitResponse.length - 1]);
        int haveQueueUsersAmount = Integer.parseInt(splitResponse[splitResponse.length - 2]);
        int notHaveQueueUsersAmount = Integer.parseInt(splitResponse[splitResponse.length - 3]);
        SettingData.usersWithQueue = new User[haveQueueUsersAmount];
        SettingData.usersWithoutQueue = new User[notHaveQueueUsersAmount];
        SettingData.blockedUsers = new User[blockedUsersAmount];
        int haveQueueUsersIndex = 0 , notHaveQueueUsersIndex = 0;
        int notBlockedUsersAmount = haveQueueUsersAmount + notHaveQueueUsersAmount;
        int responseIndex = 0;
        User newUser;
        for (int unblockedUserIndex = 0 ; unblockedUserIndex < notBlockedUsersAmount ; unblockedUserIndex++)
        {
            String name = splitResponse[responseIndex++];
            String phone = splitResponse[responseIndex++];
            String mail = splitResponse[responseIndex++];
            String queue = splitResponse[responseIndex++];
            newUser = new User(name,phone,mail,queue);
            if (queue.isEmpty())
                SettingData.usersWithoutQueue[notHaveQueueUsersIndex++] = newUser;
            else
                SettingData.usersWithQueue[haveQueueUsersIndex++] = newUser;
        }
        for (int blockedUserIndex = 0 ; blockedUserIndex < blockedUsersAmount ; blockedUserIndex++)
        {
            String name = splitResponse[responseIndex++];
            String phone = splitResponse[responseIndex++];
            String mail = splitResponse[responseIndex++];
            newUser = new User(name,phone,mail);
            SettingData.blockedUsers[blockedUserIndex] = newUser;
        }
    }

    public static void getUsersListAns(String response)
    {
        if (ServerRequest.requestAnsHelper(response)) //make toast if false
        {
            getUsersListAnsHelper(response);
            SettingData.askForUsersList = false;
            ShowUsersFragment.initScrollViewLocation(); //static method in fragments.settingFragments.ShowUsersFragments
            if (SharedData.isSettingCrntWindows())
                SharedData.settingFragment.showUsersFragment();
        }
        else //get users list from the server failed
        {
            if (SettingData.usersWithoutQueue != null)
            {
                SettingData.askForUsersList = false;
                SharedData.settingFragment.showUsersFragment();
            }
            else
            {
                SettingData.menuBtnClicked = null;
                SharedData.settingFragment.showNoneBtnIsClicked();
            }
        }
        SettingData.getUsersListInRequest = false;
        SharedData.settingFragment.checkIfRemoveLoadingView();
    }

    private static boolean responseErrorHandle(String response)
    {
        if (response.equals("permission problem"))
        {
            Toast.makeText(mainActivity, "פעולה לא בוצעה - בעיית הרשאות", Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if (response.equals(ServerRequest.REQUEST_ERROR))
        {
            Toast.makeText(mainActivity, "פעולה לא בוצעה - אין גישה לשרת", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static void lockUserCmdAns(String response)
    {
        if (response.equals("V"))
        {
            SettingData.userCmdIsLock = true;
            SharedData.settingFragment.setBlockSystemSwitch(true);
            Toast.makeText(mainActivity, "המערכת ננעלה לקביעת / שינוי תורים", Toast.LENGTH_SHORT).show();
        }
        else if (responseErrorHandle(response) == false)
            SharedData.settingFragment.setBlockSystemSwitch(false);
        SettingData.userCmdIsLockInRequest = false;
        SharedData.settingFragment.setBlockSystemSwitchEnabled();
        SharedData.settingFragment.checkIfRemoveLoadingView();
    }

    public static void unlockUserCmdAns(String response)
    {
        if (response.equals("V"))
        {
            SettingData.userCmdIsLock = false;
            SharedData.settingFragment.setBlockSystemSwitch(false);
            Toast.makeText(mainActivity, "המערכת פעילה", Toast.LENGTH_SHORT).show();
        }
        else if (responseErrorHandle(response) == false)
            SharedData.settingFragment.setBlockSystemSwitch(true);
        SettingData.userCmdIsLockInRequest = false;
        SharedData.settingFragment.checkIfRemoveLoadingView();
        SharedData.settingFragment.setBlockSystemSwitchEnabled();
    }
}
