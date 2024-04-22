package com.example.netstore.windows.main_window.employee_windows.users;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.netstore.R;
import com.example.netstore.adapters.EmployeeListAdapter;
import com.example.netstore.config.ObserverObject;
import com.example.netstore.databinding.ListItemsWithBtnFragmentBinding;
import com.example.netstore.models.Employee;
import com.example.netstore.viewModels.UserViewModel;

import java.util.List;

public class EmployeeListFragment extends Fragment {
    private ListItemsWithBtnFragmentBinding binding;
    private List<Employee> employeeList;
    private EmployeeListAdapter employeeListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ListItemsWithBtnFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateList();

        binding.addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new WorkWithEmployeeFragment(), "add employee")
                        .addToBackStack("employees")
                        .commit();
            }
        });

        binding.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDoingsDialog((Employee) parent.getItemAtPosition(position));
                return false;
            }
        });
    }

    public void showDoingsDialog(Employee employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Выберите действие для работника: " + employee.name)
                .setItems(new String[] {"Изменить"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                getParentFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.container_fragment, new WorkWithEmployeeFragment(employee), "edit employee")
                                        .addToBackStack("employees")
                                        .commit();

                                break;
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateList() {
        UserViewModel userViewModel = new UserViewModel();
        userViewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
            @Override
            public void onChanged(ObserverObject observerObject) {
                if(observerObject.tag == "get list employee" && observerObject.status) {
                    employeeList = (List<Employee>) observerObject.item;
                    employeeListAdapter = new EmployeeListAdapter(getContext(), employeeList);
                    binding.listView.setAdapter(employeeListAdapter);
                }
            }
        });

        userViewModel.getEmployees();
    }
}
