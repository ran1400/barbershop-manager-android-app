package com.example.barbershopmanager.utils.dataStructures;

import com.example.barbershopmanager.utils.DateHelper;

public class ReservedQueue
{
    public String userName;  // name of queue holder
    public String userPhone;  //phone number of queue holder
    public String queueDate;
    public String queueHour;
    public String userMail;

    public ReservedQueue(String queue,String userName,String userPhone,String userMail)
    {
        this.queueDate = queue.substring(0,10);
        this.queueHour = queue.substring(11,16);
        this.userPhone = userPhone;
        this.userName = userName;
        this.userMail = userMail;
    }

    public String getHourAndNameString()
    {
        return queueHour + " , " + userName;
    }

    public String getDateAndDayString()
    {
        return DateHelper.flipDateString(queueDate) + "  " + DateHelper.getDayOfWeek(queueDate);
    }

    public String getTime() //for send to the server
    {
        return queueDate + " " + queueHour;
    }
}
