package com.example.babershopmanager.utils.dataStructures;

import com.example.babershopmanager.fragments.QueuesFragment;
import com.example.babershopmanager.utils.DateHelper;

public class ReservedQueue
{
    public String name;  // name of queue holder
    public String phone;  //phone number of queue holder
    public String date;
    public String hour;
    public String id;

    public ReservedQueue(String date,String name,String phone,String id)
    {
        this.date = date.substring(0,10);
        this.hour = date.substring(11,16);
        this.phone = phone;
        this.name = name;
        this.id = id;
    }

    public String getHourAndNameString()
    {
        return hour + " , " + name;
    }

    public String getDateAndDayString()
    {
        return DateHelper.flipDateString(date) + "  " + DateHelper.getDayOfWeek(date);
    }

    public String getTime() //for send to the server
    {
        String yearStr = date.substring(0,4);
        String monthStr = date.substring(5,7);
        String dayStr = date.substring(8,10);
        String hourStr = hour.substring(0,2);
        String minStr = hour.substring(3,5);
        return yearStr + monthStr + dayStr + hourStr + minStr ;
    }

}
