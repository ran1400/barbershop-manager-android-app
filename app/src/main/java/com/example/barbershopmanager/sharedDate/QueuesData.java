package com.example.barbershopmanager.sharedDate;

import com.example.barbershopmanager.utils.dataStructures.ReservedQueue;

import org.json.JSONArray;

import java.util.Date;

public class QueuesData
{
    public static Date pastQueuesDateStart;
    public static Date pastQueuesDateEnd;
    public static String[] pastQueuesArray = null;
    public static JSONArray emptyQueuesArray = null;
    public static ReservedQueue[] reservedQueuesArray = null;
    public static boolean askForEmptyQueues = true;
    public static boolean askForReservedQueues = true;
    public static  boolean pastQueuesInRequest = false;
    public static boolean haveInternet = true;
    public static String pastQueueStringStart, pastQueueStringEnd; //the dates in string shape

    public static void refreshQueues()
    {
        askForEmptyQueues = true;
        askForReservedQueues = true;
    }
}
