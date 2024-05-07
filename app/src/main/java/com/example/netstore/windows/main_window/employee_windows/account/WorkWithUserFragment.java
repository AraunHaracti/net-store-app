package com.example.netstore.windows.main_window.employee_windows.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.netstore.R;
import com.example.netstore.config.ObserverObject;
import com.example.netstore.databinding.WorkWithUserFragmentBinding;
import com.example.netstore.models.User;
import com.example.netstore.viewModels.UserViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkWithUserFragment extends Fragment {
    private WorkWithUserFragmentBinding binding;
    private User currentUser;
    private Date selectedBirthday;

    public WorkWithUserFragment(User user) {
        currentUser = user;
        selectedBirthday = currentUser.birthday;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WorkWithUserFragmentBinding.inflate(inflater, container, false);

        if (currentUser != null) {
            binding.textEditFirstname.setText(currentUser.name != null ? currentUser.name : "");
            binding.editTextLastname.setText(currentUser.surname != null ? currentUser.surname : "");
            binding.textEditBirthday.setText(currentUser.birthday != null ? new SimpleDateFormat("dd MMM yyyy г.").format(currentUser.birthday) : "");
            binding.textEditEmail.setText(currentUser.email != null ? currentUser.email : "");
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText(getResources().getString(R.string.birthday));
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        binding.textEditBirthday.setOnClickListener(v -> materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            selectedBirthday = new Date((long)selection);
            binding.textEditBirthday.setText(materialDatePicker.getHeaderText());
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!formCurrentUser())
                    return;

                UserViewModel viewModel = new UserViewModel();
                viewModel.saveCurrentUser(getContext(), currentUser);

                viewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
                    @Override
                    public void onChanged(ObserverObject observerObject) {
                        if (observerObject.tag == "update user" && observerObject.status) {
                            getParentFragmentManager().popBackStack();
                        }
                    }
                });

                viewModel.updateUser(currentUser);
            }
        });
    }

    private boolean formCurrentUser() {
        String email = binding.textEditEmail.getText().toString();
        String name = binding.textEditFirstname.getText().toString();
        String surname = binding.editTextLastname.getText().toString();
        Date birthday = selectedBirthday;

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Name is empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (surname.isEmpty()) {
            Toast.makeText(getContext(), "Surname is empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (birthday == null) {
            Toast.makeText(getContext(), "Date is empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Email is empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        currentUser = new User(currentUser._id, currentUser.firebaseId, email, name, surname, birthday, currentUser.type);
        return true;
    }
}
