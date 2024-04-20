package com.example.netstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.netstore.windows.sign_up_and_in_window.SignInFragment;
import com.example.netstore.windows.sign_up_and_in_window.SignUpAndInActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, SignUpAndInActivity.class);
        startActivity(intent);
    }
}