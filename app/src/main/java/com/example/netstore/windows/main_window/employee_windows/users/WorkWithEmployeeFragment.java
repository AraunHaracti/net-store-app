package com.example.netstore.windows.main_window.employee_windows.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.netstore.R;
import com.example.netstore.config.WorkWithItemMode;
import com.example.netstore.databinding.SignUpFragmentBinding;
import com.example.netstore.databinding.WorkWithEmployeeFragmentBinding;
import com.example.netstore.models.Employee;
import com.example.netstore.models.User;
import com.example.netstore.viewModels.ShoppingCartViewModel;
import com.example.netstore.viewModels.UserViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class WorkWithEmployeeFragment extends Fragment {
    private WorkWithEmployeeFragmentBinding binding;
    private Employee currentEmployee;

    private WorkWithItemMode doMode;

    public WorkWithEmployeeFragment() {
        doMode = WorkWithItemMode.Add;
    }

    public WorkWithEmployeeFragment(Employee employee) {
        currentEmployee = employee;
        doMode = WorkWithItemMode.Edit;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WorkWithEmployeeFragmentBinding.inflate(inflater, container, false);

        if (doMode == WorkWithItemMode.Edit) {
            binding.textEditName.setText(currentEmployee.name != null ? currentEmployee.name : "");

            binding.editTextSurname.setText(currentEmployee.surname != null ? currentEmployee.surname : "");
            binding.textEditEmail.setText(currentEmployee.email != null ? currentEmployee.email : "");
            binding.textEditJob.setText(currentEmployee.job != null ? currentEmployee.job : "");
            binding.textEditDepartment.setText(currentEmployee.department != null ? currentEmployee.department : "");
            binding.textEditSalary.setText(String.valueOf(currentEmployee.salary != 0.0 ? currentEmployee.salary : 0.0));

            binding.textEditBirthday.setText(currentEmployee.birthday != null ? new SimpleDateFormat("dd MMM yyyy г.").format(currentEmployee.birthday) : "");
            binding.textEditHireDate.setText(currentEmployee.hireDate != null ? new SimpleDateFormat("dd MMM yyyy г.").format(currentEmployee.hireDate) : "");

            selectedBirthday = currentEmployee.birthday;
            selectedHireDate = currentEmployee.hireDate;

            binding.layoutWithPassword.setVisibility(View.GONE);
        } else {
            this.currentEmployee = new Employee();
            binding.layoutWithPassword.setVisibility(View.VISIBLE);
        }

        return binding.getRoot();
    }

    private Date selectedBirthday;
    private Date selectedHireDate;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialDatePicker.Builder materialDateBuilderBirthday = MaterialDatePicker.Builder.datePicker();
        materialDateBuilderBirthday.setTitleText(getResources().getString(R.string.birthday));
        final MaterialDatePicker materialDatePickerBirthday = materialDateBuilderBirthday.build();

        binding.textEditBirthday.setOnClickListener(v -> materialDatePickerBirthday.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePickerBirthday.addOnPositiveButtonClickListener(selection -> {
            selectedBirthday = new Date((long)selection);
            binding.textEditBirthday.setText(materialDatePickerBirthday.getHeaderText());
        });


        MaterialDatePicker.Builder materialDateBuilderHireDate = MaterialDatePicker.Builder.datePicker();
        materialDateBuilderHireDate.setTitleText(getResources().getString(R.string.hire_date));
        final MaterialDatePicker materialDatePickerHireDate = materialDateBuilderHireDate.build();

        binding.textEditHireDate.setOnClickListener(v -> materialDatePickerHireDate.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePickerHireDate.addOnPositiveButtonClickListener(selection -> {
            selectedHireDate = new Date((long)selection);
            binding.textEditHireDate.setText(materialDatePickerHireDate.getHeaderText());
        });


        binding.saveBtn.setOnClickListener(v -> {

            Employee currentEmployee = getCurrentEmployee();

            if (currentEmployee == null)
                return;

            UserViewModel viewModel = new UserViewModel();

            switch (doMode) {
                case Add:
                    viewModel.getInfoData().observe(getViewLifecycleOwner(), observerObject -> {
                        if (Objects.equals(observerObject.tag, "reg employee") && observerObject.status) {
                            getParentFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getContext(), "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    String email = binding.textEditEmail.getText().toString();
                    String password = binding.textEditPassword.getText().toString();
                    viewModel.registrationEmployee(email, password, currentEmployee);
                    break;

                case Edit:
                    viewModel.getInfoData().observe(getViewLifecycleOwner(), observerObject -> {
                        if (Objects.equals(observerObject.tag, "update employee") && observerObject.status) {
                            getParentFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getContext(), "Update failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    viewModel.updateEmployee(currentEmployee);
                    break;
            }
        });
    }

    @Nullable
    private Employee getCurrentEmployee() {
        String name = binding.textEditName.getText().toString();
        String surname = binding.editTextSurname.getText().toString();
        String email = binding.textEditEmail.getText().toString();

        String job = binding.textEditJob.getText().toString();
        String department = binding.textEditDepartment.getText().toString();

        double salary = binding.textEditSalary.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(binding.textEditSalary.getText().toString());

        String password = binding.textEditPassword.getText().toString();
        String passwordRepeat = binding.textEditPasswordRepeat.getText().toString();

        if (doMode == WorkWithItemMode.Add && password.isEmpty()) {
            Toast.makeText(getContext(), "Password is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (doMode == WorkWithItemMode.Add && !password.equals(passwordRepeat)) {
            Toast.makeText(getContext(), "Passwords aren't match", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Name is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (surname.isEmpty()) {
            Toast.makeText(getContext(), "Surname is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Email is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (job.isEmpty()) {
            Toast.makeText(getContext(), "Job is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (department.isEmpty()) {
            Toast.makeText(getContext(), "Department is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (salary == 0.0) {
            Toast.makeText(getContext(), "Salary is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (selectedBirthday == null) {
            Toast.makeText(getContext(), "Birthday is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (selectedHireDate == null) {
            Toast.makeText(getContext(), "Hire date is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        Employee employee = new Employee(name, surname, selectedBirthday, email, User.UserType.Employee, selectedHireDate, job, department, salary);

        return employee;
    }
}
