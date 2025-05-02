package com.example.barbershopmanager.fragments.SettingFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.barbershopmanager.utils.ExcelHandle;
import com.example.barbershopmanager.R;
import com.example.barbershopmanager.fragments.dialogFragments.SimpleMethod;
import com.example.barbershopmanager.utils.dataStructures.User;
import com.example.barbershopmanager.fragments.dialogFragments.AlertDialog;
import com.example.barbershopmanager.sharedDate.SharedData;
import com.example.barbershopmanager.sharedDate.SettingData;


public class ShowUsersFragment extends Fragment
{

    public static int scrollViewLocation = 0; //remember the location for come back to this fragment
    public static boolean initScrollVewLocation = false;
    private Button goDownBtn,getUsersFileBtn;
    private View blockedUserLocation; //on press go down scroll to blockedUserLocation
    private View withoutQueueUsersLocation; //on press go down scroll to withoutQueueUsersLocation
    private LinearLayout usersListLayout;
    private ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_show_users, container, false);
        SharedData.showUsersFragment = this;
        getUsersFileBtn = view.findViewById(R.id.getUsersFileBtn);
        getUsersFileBtn.setOnClickListener(this::getUsersFileBtn);
        usersListLayout = view.findViewById(R.id.usersListLayout);
        scrollView = view.findViewById(R.id.usersListScrollView);
        goDownBtn = view.findViewById(R.id.showUsersGoDownBtn);
        goDownBtn.setOnClickListener(this::goDownBtn);
        Button refreshBtn = view.findViewById(R.id.showUsersRefreshBtn);
        refreshBtn.setOnClickListener(this::refreshBtn);
        createUsersList();
        showGoDownBtn();
        scrollView.scrollTo(0,scrollViewLocation);
        return view;
    }

    public void onPause()
    {
        super.onPause();
        if (initScrollVewLocation)
        {
            scrollViewLocation = 0;
            initScrollVewLocation = false;
        }
        scrollViewLocation = scrollView.getScrollY(); //remember the location for come back to this fragment
    }

    private void showGoDownBtn()
    {
        int count = 0;
        if (SettingData.usersWithQueue.length > 0)
            count++;
        if (withoutQueueUsersLocation != null)
            count++;
        if (blockedUserLocation != null)
            count++;
        if (count < 2)
            goDownBtn.setVisibility(View.GONE);
    }

    private void createUsersList()
    {
        int usersAmount = SettingData.usersWithoutQueue.length + SettingData.usersWithQueue.length + SettingData.blockedUsers.length ;
        if (usersAmount == 0)
        {
            addTextToLayout( "אין משתמשים רשומים\n");
            getUsersFileBtn.setVisibility(View.INVISIBLE);
        }
        else
            addTextToLayout(usersAmount + " משתמשים רשומים");
        int btnIndex = 0;
        User tmpUser;
        if (SettingData.usersWithQueue.length > 0 )
        {
            addTextToLayout( "\nמשתמשים עם תור :\n");
            for (int i = 0; i < SettingData.usersWithQueue.length; i++)
            {
                tmpUser = SettingData.usersWithQueue[i];
                addBtnToLayout(tmpUser.name, btnIndex++);
            }
        }
        if (SettingData.usersWithoutQueue.length > 0 )
        {
            withoutQueueUsersLocation =  addTextToLayout( "\nמשתמשים ללא תור :\n");
            for (int i = 0; i < SettingData.usersWithoutQueue.length; i++)
            {
                tmpUser = SettingData.usersWithoutQueue[i];
                addBtnToLayout(tmpUser.name, btnIndex++);
            }
        }
        else
            withoutQueueUsersLocation = null;
        if (SettingData.blockedUsers.length > 0 )
        {
            blockedUserLocation = addTextToLayout( "\nמשתמשים חסומים :\n");
            for (int i = 0; i < SettingData.blockedUsers.length; i++)
            {
                tmpUser = SettingData.blockedUsers[i];
                addBtnToLayout(tmpUser.name, btnIndex++);
            }
        }
        else
            blockedUserLocation = null;
        if ( blockedUserLocation == null  && withoutQueueUsersLocation ==  null)
            goDownBtn.setVisibility(View.GONE);
        addTextToLayout("\n\n");
    }

    private void addBtnToLayout(String btnText, int btnId)
    {
        Button pressUserBtn = new Button(SharedData.mainActivity);
        pressUserBtn.setText(btnText);
        pressUserBtn.setOnClickListener(this::userClicked);
        pressUserBtn.setId(btnId);
        usersListLayout.addView(pressUserBtn);
    }

    public static void initScrollViewLocation()
    {
        initScrollVewLocation = true;
    }

    private void refreshBtn(View view)
    {
        SettingData.askForUsersList = true;
        SettingData.menuBtnClicked = null;
        SharedData.settingFragment.refreshUsersList();
    }

    private void goDownBtn(View view)
    {
        if (SettingData.usersWithQueue.length == 0)
        {
            if (blockedUserLocation != null)
                scrollView.scrollTo(0, blockedUserLocation.getTop());
            return;
        }
        if (withoutQueueUsersLocation != null && blockedUserLocation != null) //if the button is visible not both null
        {
            SimpleMethod doIfUserPressOp1 = () -> scrollView.scrollTo(0, withoutQueueUsersLocation.getTop());
            SimpleMethod doIfUserPressOp2 = () -> scrollView.scrollTo(0, blockedUserLocation.getTop());
            String dialogTitle = "לאיפה לגלול?";
            String op1 = "משתמשים ללא תור" ;
            String op2 = "מתמשים חסומים" ;
            AlertDialog.showAlertDialog(dialogTitle,"",op1,op2,doIfUserPressOp1,doIfUserPressOp2);
        }
        //one of them is null
        else if (withoutQueueUsersLocation != null)
            scrollView.scrollTo(0, withoutQueueUsersLocation.getTop());
        else  // withoutQueueUsersLocation is null
            scrollView.scrollTo(0, blockedUserLocation.getTop());
    }

    private void userClicked(View view)
    {
        SettingData.userClickedBtnId = view.getId();
        SharedData.settingFragment.userClicked();
    }

    private View addTextToLayout(String txt)
    {
        TextView textView = new TextView(SharedData.mainActivity);
        textView.setText(txt);
        textView.setTextSize(20);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        usersListLayout.addView(textView);
        return textView;
    }

    public void getUsersFileBtn(View view)
    {
        ExcelHandle excelHandle = new ExcelHandle();
        String fileIsWritten = excelHandle.makeUserListFile();
        if (fileIsWritten != null)
            Toast.makeText(SharedData.mainActivity,"הקובץ " + fileIsWritten + " נשמר בתיקיית הורדות", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(SharedData.mainActivity,"שמירת הקובץ נכשלה", Toast.LENGTH_SHORT).show();
    }
}