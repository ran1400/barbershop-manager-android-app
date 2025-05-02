package com.example.barbershopmanager.sharedDate;

import java.util.Date;

public class DeleteQueuesData
{
    public static Date deleteQueuesFromDate;
    public static Integer deleteQueuesFromHour;
    public static String deleteQueuesFromStr;
    public static Date deleteQueuesToDate;
    public static Integer deleteQueuesToHour;
    public static String deleteQueuesToStr;
    public static Date deleteQueueDate;
    public static Integer deleteQueueHour;
    public static boolean cbDeleteEmptyQueues,cbDeleteReservedQueues ; // checkBoxes state
    public static boolean cbDeleteEmptyQueue,cbDeleteReservedQueue ; // checkBoxes state
    public static boolean deleteQueuesInRequest = false;
    public static boolean deleteQueueInRequest = false;

    public enum CrntWidnows {deleteQueue, deleteQueues}

    public static CrntWidnows crntWidnows = CrntWidnows.deleteQueues;

    public static boolean deleteQueueWindowsIsVisible()
    {
        return crntWidnows == CrntWidnows.deleteQueue;
    }

    public static void setCrntWindowToDeleteQueue()
    {
        crntWidnows = CrntWidnows.deleteQueue;
    }

    public static void setCrntWindowToDeleteQueues()
    {
        crntWidnows = CrntWidnows.deleteQueues;
    }
}
