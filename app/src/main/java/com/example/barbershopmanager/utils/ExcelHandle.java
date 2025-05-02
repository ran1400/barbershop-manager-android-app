package com.example.barbershopmanager.utils;

import static android.content.ContentValues.TAG;

import android.os.Environment;
import android.util.Log;


import com.example.barbershopmanager.sharedDate.QueuesData;
import com.example.barbershopmanager.sharedDate.SettingData;
import com.example.barbershopmanager.utils.dataStructures.User;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExcelHandle
{

    private Cell cell;
    private Sheet sheets[];
    private Workbook workbook = new HSSFWorkbook();
    private CellStyle headerCellStyle;
    private String sheetNames[];
    private String fileName;

    public boolean makePestQueuesListFile()
    {
        sheets = new Sheet[1];
        sheetNames = new String[]{"תורים קודמים"};
        fileName = "queues " + QueuesData.pastQueueStringStart + " - " + QueuesData.pastQueueStringEnd  + ".xls";
        return makePestQueuesListFileHelper();
    }

    private boolean makePestQueuesListFileHelper()
    {
        boolean isWorkbookWrittenIntoStorage;

        // Check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly())
        {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        // Creating a New HSSF Workbook (.xls format)
        workbook = new HSSFWorkbook();

        setHeaderCellStyle();
        sheets[0] = workbook.createSheet(sheetNames[0]);
        sheets[0].setColumnWidth(0, (8 * 400));
        sheets[0].setColumnWidth(1, (4 * 400));
        sheets[0].setColumnWidth(2, (15 * 400));


        setHeaderPestQueuesListFile();
        fillDataInPestQueuesListFile();
        isWorkbookWrittenIntoStorage = storeExcelInStorage();

        return isWorkbookWrittenIntoStorage;
    }

    private void setHeaderPestQueuesListFile()
    {
        Row row = sheets[0].createRow(0);
        String headers[] = {"תאריך","שעה","שם"};
        for (int i = 0 ; i < headers.length ; i++)
        {
            cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerCellStyle);
        }
    }

    public String makeUserListFile()
    {
        sheets = new Sheet[2];
        sheetNames = new String[]{"משתמשים","משתמשים חסומים"};
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.getDefault());
        String crntTime = sdf.format(new Date());
        fileName = "users list " + crntTime + ".xls";
        Boolean res = makeUserListFileHelper();
        if (res)
            return fileName;
        else
            return null;
    }


    private boolean makeUserListFileHelper()
    {
        boolean isWorkbookWrittenIntoStorage;

        // Check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly())
        {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        // Creating a New HSSF Workbook (.xls format)
        workbook = new HSSFWorkbook();

        setHeaderCellStyle();

        for (int i = 0 ; i < sheets.length ; i++)
        {
            sheets[i] = workbook.createSheet(sheetNames[i]);
            sheets[i].setColumnWidth(0, (10 * 400));
            sheets[i].setColumnWidth(1, (8 * 400));
            sheets[i].setColumnWidth(2, (20 * 400));
            sheets[i].setColumnWidth(3, (12 * 400));
        }

        setHeaderUserListFile();
        fillDataInUserListFile();
        isWorkbookWrittenIntoStorage = storeExcelInStorage();

        return isWorkbookWrittenIntoStorage;
    }


    private boolean isExternalStorageReadOnly()
    {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(externalStorageState);
    }


    private boolean isExternalStorageAvailable()
    {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(externalStorageState);
    }


    private void setHeaderCellStyle()
    {
        headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(HSSFColor.AQUA.index);
        headerCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
    }

    private void setHeaderUserListFile()
    {

            Row rowSheet1 = sheets[0].createRow(0);
            String headersSheet1[] = {"שם","פלאפון","מייל","תור"};
            for (int headerIndex = 0 ; headerIndex < headersSheet1.length ; headerIndex++)
            {
                cell = rowSheet1.createCell(headerIndex);
                cell.setCellValue(headersSheet1[headerIndex]);
                cell.setCellStyle(headerCellStyle);
            }

            Row rowSheet2 = sheets[1].createRow(0);
            String headersSheet2[] = {"שם","פלאפון","מייל"};
            for (int headerIndex = 0 ; headerIndex < headersSheet2.length ; headerIndex++)
            {
                cell = rowSheet2.createCell(headerIndex);
                cell.setCellValue(headersSheet2[headerIndex]);
                cell.setCellStyle(headerCellStyle);
            }
    }

    private void fillDataInPestQueuesListFile()
    {
        String date,hour,name;
        for ( int i = 0 ; i < QueuesData.pastQueuesArray.length ; i++)
        {
            date = QueuesData.pastQueuesArray[i].substring(0,10);
            hour = QueuesData.pastQueuesArray[i].substring(11,16);
            name = QueuesData.pastQueuesArray[i].substring(19);
            // Create a New Row for every new entry in list
            Row row = sheets[0].createRow(i+1);

            // Create Cells for each row
            cell = row.createCell(0);
            cell.setCellValue(DateHelper.flipDateString(date));

            cell = row.createCell(1);
            cell.setCellValue(hour);

            cell = row.createCell(2);
            cell.setCellValue(name);
        }
    }

    private void fillDataInUserListFile()
    {
        int rowNum = 1;
        int sheetIndex = 0;
        User tmpUser;
        for (int userIndex = 0; userIndex < SettingData.usersWithQueue.length; userIndex++)
        {
            Row row = sheets[sheetIndex].createRow(rowNum++);
            tmpUser = SettingData.usersWithQueue[userIndex];
            addUserToSheet(row, tmpUser.name, tmpUser.phone, tmpUser.mail, DateHelper.flipDateAndHour(tmpUser.queue));
        }
        for ( int userIndex = 0 ; userIndex < SettingData.usersWithoutQueue.length; userIndex++)
        {
            Row row = sheets[sheetIndex].createRow(rowNum++);
            tmpUser = SettingData.usersWithoutQueue[userIndex];
            addUserToSheet(row, tmpUser.name, tmpUser.phone, tmpUser.mail, "אין");
        }

        sheetIndex++; //move to block users sheet
        rowNum = 1;

        for ( int userIndex = 0 ; userIndex < SettingData.blockedUsers.length; userIndex++)
        {
            Row row = sheets[sheetIndex].createRow(rowNum++);
            tmpUser = SettingData.blockedUsers[userIndex];
            addBlockedUserToSheet(row, tmpUser.name, tmpUser.phone, tmpUser.mail);
        }
    }

    private void addBlockedUserToSheet(Row row,String name,String phone,String mail)
    {
        cell = row.createCell(0);
        cell.setCellValue(name);

        cell = row.createCell(1);
        cell.setCellValue(phone);

        cell = row.createCell(2);
        cell.setCellValue(mail);
    }

    private void addUserToSheet(Row row,String name,String phone,String mail,String queue)
    {
        cell = row.createCell(0);
        cell.setCellValue(name);

        cell = row.createCell(1);
        cell.setCellValue(phone);

        cell = row.createCell(2);
        cell.setCellValue(mail);

        cell = row.createCell(3);
        cell.setCellValue(queue);
    }


    private boolean storeExcelInStorage()
    {
        boolean isSuccess;
        File downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadPath ,fileName);
        FileOutputStream fileOutputStream = null;
        try
        {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            isSuccess = true;
        }
        catch (IOException e)
        {
            Log.e(TAG, "Error writing Exception: ", e);
            isSuccess = false;
        }
        catch (Exception e)
        {
            Log.e(TAG, "Failed to save file due to Exception: ", e);
            isSuccess = false;
        }
        finally
        {
            try
            {
                if (null != fileOutputStream)
                    fileOutputStream.close();

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return isSuccess;
    }
}
