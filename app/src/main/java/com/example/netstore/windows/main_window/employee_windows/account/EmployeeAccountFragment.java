package com.example.netstore.windows.main_window.employee_windows.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.netstore.R;
import com.example.netstore.config.ObserverObject;
import com.example.netstore.config.SharedPreferencesConfig;
import com.example.netstore.databinding.EmployeeAccountFragmentBinding;
import com.example.netstore.models.Employee;
import com.example.netstore.models.User;
import com.example.netstore.viewModels.UserViewModel;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;

public class EmployeeAccountFragment extends Fragment {
    private EmployeeAccountFragmentBinding binding;
    private Employee currentEmployee;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EmployeeAccountFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserViewModel viewModel = new UserViewModel();

        viewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
            @Override
            public void onChanged(ObserverObject observerObject) {
                if (observerObject.tag == "get employee" && observerObject.status == true) {
                    currentEmployee = ((Employee) observerObject.item);
                    setValues(currentEmployee);
                }
                else {
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SharedPreferencesConfig.SP_FILE_TAG, Context.MODE_PRIVATE);
        User user = new Gson().fromJson(sharedPreferences.getString(SharedPreferencesConfig.SP_USER_TAG, ""), User.class);
        viewModel.getEmployee(user);

        binding.editItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new WorkWithUserFragment(currentEmployee), "user_item")
                        .addToBackStack("account_employee")
                        .commit();
            }
        });
    }

    private void setValues(Employee employee) {
        binding.textViewFirstname.setText(employee.name != null ? employee.name : "");
        binding.textViewLastname.setText(employee.surname != null ? employee.surname : "");
        binding.textViewBirthday.setText(employee.birthday != null ? new SimpleDateFormat("dd MMM yyyy г.").format(employee.birthday) : "");
        binding.textViewEmail.setText(employee.email != null ? employee.email : "");
        binding.textViewDepartment.setText(employee.department != null ? employee.department : "");
        binding.textViewHireDate.setText(employee.hireDate != null ? new SimpleDateFormat("dd MMM yyyy г.").format(employee.hireDate) : "");
        binding.textViewJob.setText(employee.job != null ? employee.job : "");
        binding.textViewSalary.setText(employee.salary != 0.0 ? String.valueOf(employee.salary) : "");
    }
}
