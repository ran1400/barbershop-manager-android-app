package com.example.barbershopmanager.fragments.dialogFragments;

import com.example.barbershopmanager.sharedDate.SharedData;

public class AlertDialog
{
    public static void showAlertDialog(String title, String msg, SimpleMethod doIfPositive)
    {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(SharedData.mainActivity);
        alertDialog.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("כן", (dialog, id) -> doIfPositive.execute())
                .setNegativeButton("לא", null);
        alertDialog.show();
    }

    public static void showAlertDialog(String title,String msg,String op1,String op2,SimpleMethod doIfOp1,SimpleMethod doIfOp2)
    {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(SharedData.mainActivity);
        alertDialog.setTitle(title)
                .setMessage(msg)
                .setPositiveButton(op1, (dialog, id) -> doIfOp1.execute())
                .setNegativeButton(op2, (dialog, id) -> doIfOp2.execute())
                .setNeutralButton("חזור", null);
        alertDialog.show();
    }
}
