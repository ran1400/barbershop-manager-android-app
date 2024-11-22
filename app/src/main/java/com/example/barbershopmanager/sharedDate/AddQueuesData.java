package com.example.barbershopmanager.sharedDate;

import java.util.Date;

public class AddQueuesData
{
    public static String addQueuesSpaceBetweenQueues;
    public static Date addQueuesDateStart;
    public static Integer addQueuesHourStart;
    public static Date addQueuesDateEnd;
    public static Integer addQueuesHourEnd;
    public static Date addQueueDate;
    public static Integer addQueueHour;
    public static Boolean[] daysTbState = {true, true, true, true, true, false, false};//friday and saturday are false
    public static boolean addQueuesInRequest = false;
    public static boolean addQueueInRequest = false;

    enum CrntWidnows {addQueue,addQueues}

    public static CrntWidnows crntWidnows = CrntWidnows.addQueues;

    public static boolean addQueueWindowsIsVisible()
    {
        return crntWidnows == CrntWidnows.addQueue;
    }

    public static void setCrntWindowToAddQueue()
    {
        crntWidnows = CrntWidnows.addQueue;
    }

    public static void setCrntWindowToAddQueues()
    {
        crntWidnows = CrntWidnows.addQueues;
    }
}
