package com.example.netstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.netstore.config.SharedPreferencesConfig;
import com.example.netstore.models.User;
import com.example.netstore.windows.main_window.MainWindowActivity;
import com.example.netstore.windows.sign_up_and_in_window.SignUpAndInActivity;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesConfig.SP_FILE_TAG, MODE_PRIVATE);

        String user = sharedPreferences.getString(SharedPreferencesConfig.SP_USER_TAG, "");

        User currentUser = new Gson().fromJson(user, User.class);

        Intent intent;

        if (currentUser != null && currentUser.firebaseId != null) {
            intent = new Intent(this, MainWindowActivity.class);
        }
        else {
            intent = new Intent(this, SignUpAndInActivity.class);
        }

        startActivity(intent);
        finish();
    }
}