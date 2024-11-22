package com.example.barbershopmanager.sharedDate;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.barbershopmanager.MainActivity;
import com.example.barbershopmanager.fragments.AddQueuesFragment;
import com.example.barbershopmanager.fragments.DeleteQueuesFragment;
import com.example.barbershopmanager.fragments.QueuesFragment;
import com.example.barbershopmanager.fragments.QueuesFragments.EmptyQueuesFragment;
import com.example.barbershopmanager.fragments.QueuesFragments.PastQueuesFragment;
import com.example.barbershopmanager.fragments.QueuesFragments.ReservedQueuesFragment;
import com.example.barbershopmanager.fragments.SettingFragment;
import com.example.barbershopmanager.fragments.SettingFragments.NotificationsFragment;
import com.example.barbershopmanager.fragments.SettingFragments.SendMsgFragment;
import com.example.barbershopmanager.fragments.SettingFragments.ShowUsersFragment;
import com.example.barbershopmanager.fragments.SettingFragments.UserOptionsFragment;

public class SharedData
{

        public static MainActivity mainActivity;
        public static SettingFragment settingFragment;
        public static QueuesFragment queuesFragment;
        public static EmptyQueuesFragment emptyQueuesFragment;
        public static PastQueuesFragment pastQueuesFragment;
        public static ReservedQueuesFragment reservedQueuesFragment;
        public static AddQueuesFragment addQueuesFragment;
        public static DeleteQueuesFragment deleteQueuesFragment;
        public static SendMsgFragment sendMsgFragment;
        public static ShowUsersFragment showUsersFragment;
        public static UserOptionsFragment userOptionsFragment;
        public static NotificationsFragment notificationsFragment;

        enum CrntWindow
        {
                queues,
                addQueues,
                deleteQueues,
                setting
        }

        public static boolean getFromMemory(String infoName)
        {
                SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                return sharedPreferences.getBoolean(infoName,true);
        }

        public static void writeToMemory(String infoName,boolean bool)
        {
                SharedPreferences sharedPreferences  = mainActivity.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(infoName,bool);
                editor.commit();
        }

        public static CrntWindow crntWindow = null;

        public static void setQueuesCrntWindow()
        {
                crntWindow = CrntWindow.queues;
        }

        public static void setAddQueuesCrntWindow()
        {
                crntWindow = CrntWindow.addQueues;
        }

        public static void setDeleteQueuesCrntWindow()
        {
                crntWindow = CrntWindow.deleteQueues;
        }

        public static void setSettingQueuesCrntWindow()
        {
                crntWindow = CrntWindow.setting;
        }

        public static boolean isAddQueuesCrntWindows()
        {
                return crntWindow == CrntWindow.addQueues;
        }

        public static boolean isDeleteQueuesCrntWindows()
        {
                return crntWindow == CrntWindow.deleteQueues;
        }

        public static boolean isQueuesCrntWindows()
        {
                return crntWindow == CrntWindow.queues;
        }
        public static boolean isSettingCrntWindows()
        {
                return crntWindow == CrntWindow.setting;
        }

}