package com.example.netstore.windows.main_window.employee_windows;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netstore.R;

public class EmployeeWindowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_view_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_fragment, new EmployeeMainWindowFragment(), "employee_fragment")
                .commit();
    }
}
