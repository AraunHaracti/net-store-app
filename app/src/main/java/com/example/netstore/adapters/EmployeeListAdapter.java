package com.example.netstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.netstore.R;
import com.example.netstore.models.Employee;

import java.util.List;

public class EmployeeListAdapter extends ArrayAdapter<Employee> {
    private Context context;
    private List<Employee> employeeList;

    public EmployeeListAdapter(@NonNull Context context, @NonNull List<Employee> employeeList) {
        super(context, R.layout.employee_list_item_fragment, employeeList);
        this.context = context;
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(context).inflate(R.layout.employee_list_item_fragment, parent, false);

        Employee currentEmployee = employeeList.get(position);

        TextView nameTextView = listItemView.findViewById(R.id.text_view_firstname);
        TextView lastnameTextView = listItemView.findViewById(R.id.text_view_lastname);
        TextView birthdayTextView = listItemView.findViewById(R.id.text_view_birthday);
        TextView emailTextView = listItemView.findViewById(R.id.text_view_email);
        TextView hireDateTextView = listItemView.findViewById(R.id.text_view_hire_date);
        TextView jobTextView = listItemView.findViewById(R.id.text_view_job);
        TextView departmentTextView = listItemView.findViewById(R.id.text_view_department);
        TextView salaryTextView = listItemView.findViewById(R.id.text_view_salary);

        nameTextView.setText(currentEmployee.name != null ? currentEmployee.name : "");
        lastnameTextView.setText(currentEmployee.surname != null ? currentEmployee.surname : "");
        birthdayTextView.setText(currentEmployee.birthday != null ? currentEmployee.birthday.toString() : "");
        emailTextView.setText(currentEmployee.email != null ? currentEmployee.email : "");
        hireDateTextView.setText(currentEmployee.hireDate != null ? currentEmployee.hireDate.toString() : "");
        jobTextView.setText(currentEmployee.job != null ? currentEmployee.job : "");
        departmentTextView.setText(currentEmployee.department != null ? currentEmployee.department : "");
        salaryTextView.setText(currentEmployee.salary != 0.0 ? String.valueOf(currentEmployee.salary) : "");

        return listItemView;
    }
}
