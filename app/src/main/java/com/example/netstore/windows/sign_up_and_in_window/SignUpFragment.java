package com.example.netstore.windows.sign_up_and_in_window;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.netstore.ObserverObject;
import com.example.netstore.R;
import com.example.netstore.databinding.SignUpFragmentBinding;
import com.example.netstore.models.User;
import com.example.netstore.viewModels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.Objects;

public class SignUpFragment extends Fragment {
    private SignUpFragmentBinding binding;
    private Date selectedBirthday;

    public SignUpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = SignUpFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText(getResources().getString(R.string.birthday));
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        binding.editTextBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                selectedBirthday = new Date((long)selection);
                binding.editTextBirthday.setText(materialDatePicker.getHeaderText());
            }
        });

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User currentUser = getCurrentUser();

                if (currentUser == null)
                    return;

                UserViewModel viewModel = new UserViewModel();

                viewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
                    @Override
                    public void onChanged(ObserverObject observerObject) {
                        if (Objects.equals(observerObject.tag, "reg user") && observerObject.status) {
                            getParentFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getContext(), "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                viewModel.registrationUser(email, password, currentUser);
            }
        });
    }

    @Nullable
    private User getCurrentUser() {
        String email = binding.editTextEmail.getText().toString();
        String password = binding.editTextPassword.getText().toString();
        String passwordRepeat = binding.editTextPasswordRepeat.getText().toString();
        String name = binding.editTextName.getText().toString();
        String surname = binding.editTextSurname.getText().toString();
        Date birthday = selectedBirthday;

        if (password.isEmpty()) {
            Toast.makeText(getContext(), "Password is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (!password.equals(passwordRepeat)) {
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

        if (birthday == null) {
            Toast.makeText(getContext(), "Date is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Email is empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        User user = new User(name, surname, birthday, email, User.UserType.Client);
        return user;
    }
}