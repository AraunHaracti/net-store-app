package com.example.netstore.windows.main_window.employee_windows.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.netstore.databinding.WorkWithEmployeeFragmentBinding;
import com.example.netstore.models.Employee;

public class WorkWithEmployeeFragment extends Fragment {
    private WorkWithEmployeeFragmentBinding binding;
    private Employee currentEmployee;

    public WorkWithEmployeeFragment() {
    }

    public WorkWithEmployeeFragment(Employee employee) {
        currentEmployee = employee;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WorkWithEmployeeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
