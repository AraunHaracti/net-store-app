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
    private Context mContext;
    private List<Employee> mEmployees;

    public EmployeeListAdapter(@NonNull Context context, @NonNull List<Employee> users) {
        super(context, R.layout.employee_info_fragment, users);
        mContext = context;
        mEmployees = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.employee_info_fragment, parent, false);
        }

        Employee currentEmployee = mEmployees.get(position);

        TextView nameTextView = listItemView.findViewById(R.id.text_view_firstname);
        if (currentEmployee.name != null) {
            nameTextView.setText(currentEmployee.name);
        }

        TextView lastnameTextView = listItemView.findViewById(R.id.text_view_lastname);
        if (currentEmployee.surname != null) {
            lastnameTextView.setText(currentEmployee.surname);
        }

        TextView birthdayTextView = listItemView.findViewById(R.id.text_view_birthday);
        if (currentEmployee.birthday != null) {
            birthdayTextView.setText(currentEmployee.birthday.toString());
        }

        TextView emailTextView = listItemView.findViewById(R.id.text_view_email);
        if (currentEmployee.email != null) {
            emailTextView.setText(currentEmployee.email);
        }

        TextView hireDateTextView = listItemView.findViewById(R.id.text_view_hire_date);
        if (currentEmployee.hireDate != null) {
            hireDateTextView.setText(currentEmployee.hireDate.toString());
        }

        TextView jobTextView = listItemView.findViewById(R.id.text_view_job);
        if (currentEmployee.job != null) {
            jobTextView.setText(currentEmployee.job);
        }

        TextView departmentTextView = listItemView.findViewById(R.id.text_view_department);
        if (currentEmployee.department != null) {
            departmentTextView.setText(currentEmployee.department);
        }

        TextView salaryTextView = listItemView.findViewById(R.id.text_view_salary);
        salaryTextView.setText(String.valueOf(currentEmployee.salary));

        return listItemView;
    }
}
