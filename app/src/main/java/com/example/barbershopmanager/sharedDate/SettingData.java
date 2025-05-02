package com.example.barbershopmanager.sharedDate;

import com.example.barbershopmanager.utils.dataStructures.User;


public class SettingData
{

    public enum MenuBtnClicked {GET_USERS,USER,NOTIFICATIONS_SETTING,SEND_MSG}
    public static MenuBtnClicked menuBtnClicked;
    public static boolean userCmdIsLock;
    public static boolean userCmdIsLockInRequest = false;
    public static boolean getUsersListInRequest = false;
    public static boolean getSettingFragmentInRequest = false;
    public static boolean askForUsersList = true;
    public static User[] usersWithQueue = null;
    public static User[] usersWithoutQueue = null;
    public static User[] blockedUsers = null;
    public static boolean sendPushMsgInRequest = false;
    public static boolean setInAppMsgInRequest = false;
    public static boolean sendMailInRequest = false;
    public static int userClickedBtnId;
    public static boolean showUserFragmentInRequest = false;
    public static int secondsAmountToGetNotification; //for notification manager fragment
    public static boolean sendUserBlockNotifications; //for notification manager fragment
    public static boolean sendUserRemoveNotifications; //for notification manager fragment
    public static boolean sendUserUnblockNotifications; //for notification manager fragment
    public static boolean setSecondsAmountToSendNotificationInRequest = false;  //for notification manager fragment
    public static boolean sendUserBlockNotificationsInRequest = false; //for notification manager fragment
    public static boolean sendUserQueueNotificationsInRequest = false; //for notification manager fragment
    public static boolean sendUserRemoveNotificationsInRequest = false; //for notification manager fragment
    public static boolean sendUserUnblockNotificationsInRequest = false; //for notification manager fragment

}
