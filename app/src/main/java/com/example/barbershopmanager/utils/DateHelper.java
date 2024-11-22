package com.example.barbershopmanager.utils;


import android.widget.Toast;

import com.example.barbershopmanager.sharedDate.SharedData;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class DateHelper
{

    public static final String[] daysOfWeek = {"יום ראשון" , "יום שני" , "יום שלישי" ,  "יום רביעי" , "יום חמישי" , "יום שישי" , "יום שבת"};


    public static String fromDateToStr(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH) ;
        return day + "." + month + "." + c.get(Calendar.YEAR);
    }

    public static String fromHourToStr(int time)
    {
        int hour = time / 100 ;
        int min = time % 100 ;
        String minStr,hourStr;
        if (min < 10)
            minStr = "0" + min ;
        else
            minStr = String.valueOf(min);
        if (hour < 10)
            hourStr = "0" + hour ;
        else
            hourStr = String.valueOf(hour);
        return minStr + " : " + hourStr;
    }

    public static String fromTimeToStr(Date date , int hour)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH) ;
        return day + "." + month + "." + c.get(Calendar.YEAR) + "  -  " + fromHourToStr(hour);
    }

    public static String makeHourStr(int num) //for send this to the server
    {
        int hour = num / 100;
        int min = num % 100;
        String hourStr,minStr;
        if (hour < 10)
            hourStr = "0" + hour ;
        else
            hourStr = String.valueOf(hour);
        if (min < 10)
            minStr = "0" + min;
        else
            minStr = String.valueOf(min);
        return hourStr+minStr;
    }

    public static boolean checkIfFutureDate(Date date , int hour)
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
        Date timeStamp = new Date();
        String timeStampStr = sdf.format(timeStamp);
        String inputTimeStr = sdf.format(date).substring(0,8) + makeHourStr(hour);
        if ( Long.parseLong(timeStampStr) > Long.parseLong(inputTimeStr) )
        {
            Toast.makeText(SharedData.mainActivity,"לא ניתן להכניס תור לא עתידי",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean checkIfFutureDate(Date date , String hour , String min)
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
        Date timeStamp = new Date();
        String timeStampStr = sdf.format(timeStamp);
        String inputTimeStr = sdf.format(date).substring(0,8) + hour + min;
        if ( Long.parseLong(timeStampStr) > Long.parseLong(inputTimeStr) )
        {
            Toast.makeText(SharedData.mainActivity,"לא ניתן להכניס תור לא עתידי",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static String getTime(Date date,String hourStr,String minStr) //for send this to the server
    {
        String monthStr,dayStr;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10)
            monthStr = "0" + month;
        else
            monthStr = String.valueOf(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day < 10)
            dayStr = "0" + day;
        else
            dayStr = String.valueOf(day);
        return year + monthStr + dayStr + hourStr + minStr + "00"; // 00 for the seconds
    }

    public static String getTimeString(Date date,String hourStr,String minStr) //for show to the user
    {
        String monthStr,dayStr;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10)
            monthStr = "0" + month;
        else
            monthStr = String.valueOf(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day < 10)
            dayStr = "0" + day;
        else
            dayStr = String.valueOf(day);
        return dayStr + "." + monthStr + "." + year + " - " + hourStr + ":" + minStr;
    }

    public static String getTime(Date date) //for send to the server
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String monthStr,dayStr;
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH) ;
        if (month < 10)
            monthStr = "0" + month;
        else
            monthStr = "" + month;
        if (day < 10)
            dayStr = "0" + day;
        else
            dayStr = "" + day;
        return c.get(Calendar.YEAR) + monthStr + dayStr;
    }

    public static String getTime(Date date, int hour) //for send this to the server
    {
        String hourStr,minStr;
        int hourVal = (hour / 100);
        int minVal = (hour % 100);
        if (hourVal < 10)
            hourStr = "0" + hourVal;
        else
            hourStr = String.valueOf(hourVal);
        if (minVal < 10)
            minStr = "0" + minVal;
        else
            minStr = String.valueOf(minVal);
        String monthStr,dayStr;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10)
            monthStr = "0" + month;
        else
            monthStr = String.valueOf(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day < 10)
            dayStr = "0" + day;
        else
            dayStr = String.valueOf(day);
        return year + monthStr + dayStr + hourStr + minStr + "00"; // 00 for the seconds
    }

    public static String flipDateString (String input) //input is yyyy-mm-dd res is day.month.year
    {
        String year = input.substring(0,4);
        String month = input.substring(5,7);
        String day = input.substring(8,10);
        return day + "." + month + "." + year;
    }

    public static String flipYYYYMMDD (String input) //input is yyyymmdd res is day.month.year
    {
        String year = input.substring(0,4);
        String month = input.substring(4,6);
        String day = input.substring(6,8);
        return day + "." + month + "." + year;
    }

    public static String flipDateAndHour(String serverDate) //input is yyyy-mm-dd hh:mm:ss to dd.mm.year hh:mm
    {
        String year = serverDate.substring(0,4);
        String month = serverDate.substring(5,7);
        String day = serverDate.substring(8,10);
        String hour = serverDate.substring(11,13);
        String min = serverDate.substring(14,16);
        return day+"."+month+"."+year+" " +hour +":" +min;
    }


    public static String getDayOfWeek(String time)
    {
        int year = Integer.parseInt(time.substring(0,4));
        int month = Integer.parseInt(time.substring(5,7));
        int day = Integer.parseInt(time.substring(8,10));
        Date date = new GregorianCalendar(year,month-1,day).getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayNum = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return daysOfWeek[dayNum];
    }

}