package com.example.netstore.windows.main_window.employee_windows;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.netstore.MainActivity;
import com.example.netstore.R;
import com.example.netstore.config.Config;
import com.example.netstore.databinding.EmployeeMainWindowFragmentBinding;
import com.example.netstore.windows.main_window.employee_windows.account.EmployeeAccountFragment;
import com.example.netstore.windows.main_window.employee_windows.inventorying.InventoryingChosenDoingFragment;
import com.example.netstore.windows.main_window.employee_windows.orders.OrderListFragment;
import com.example.netstore.windows.main_window.employee_windows.places.PlaceListFragment;
import com.example.netstore.windows.main_window.employee_windows.products.ProductListFragment;
import com.example.netstore.windows.main_window.employee_windows.users.UserBtnFragment;

public class EmployeeMainWindowFragment extends Fragment {
    private EmployeeMainWindowFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EmployeeMainWindowFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new EmployeeAccountFragment(), "employee_account")
                        .addToBackStack("employee_main_window")
                        .commit();
            }
        });

        binding.placesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new PlaceListFragment(), "places")
                        .addToBackStack("employee_main_window")
                        .commit();
            }
        });

        binding.productsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new ProductListFragment(), "products")
                        .addToBackStack("employee_main_window")
                        .commit();
            }
        });

        binding.usersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new UserBtnFragment(), "user_btn")
                        .addToBackStack("employee_main_window")
                        .commit();
            }
        });

        binding.ordersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new OrderListFragment(), "orders")
                        .addToBackStack("employee_main_window")
                        .commit();
            }
        });

        binding.inventoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new InventoryingChosenDoingFragment(), "inventorying")
                        .addToBackStack("employee_main_window")
                        .commit();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Выйти?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences.Editor spEditor = getContext().getSharedPreferences(Config.SP_FILE_TAG, Context.MODE_PRIVATE).edit();
                spEditor.putString(Config.SP_USER_TAG, "");
                spEditor.apply();

                closeActivity();
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();

        binding.signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private void closeActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }
}
