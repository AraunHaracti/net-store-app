package com.example.netstore.windows.main_window;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netstore.config.Config;
import com.example.netstore.MainActivity;
import com.example.netstore.models.User;
import com.example.netstore.windows.main_window.client_windows.ClientWindowActivity;
import com.example.netstore.windows.main_window.employee_windows.EmployeeWindowActivity;
import com.google.gson.Gson;

public class MainWindowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SP_FILE_TAG, MODE_PRIVATE);

        String user = sharedPreferences.getString(Config.SP_USER_TAG, "");

        User currentUser = new Gson().fromJson(user, User.class);

        Intent intent;

        switch (currentUser.type) {
            case Client:
                intent = new Intent(this, ClientWindowActivity.class);
                break;
            case Employee:
                intent = new Intent(this, EmployeeWindowActivity.class);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
        finish();
    }
}