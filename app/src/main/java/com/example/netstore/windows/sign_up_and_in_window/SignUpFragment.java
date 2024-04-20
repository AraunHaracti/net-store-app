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
import com.example.netstore.databinding.SignUpFragmentBinding;
import com.example.netstore.models.User;
import com.example.netstore.viewModels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class SignUpFragment extends Fragment {
    private SignUpFragmentBinding binding;
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
                        if ((boolean) observerObject.item) {
                            getParentFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                viewModel.Registration(currentUser);
            }
        });
    }

    @Nullable
    private User getCurrentUser() {
        String login = binding.editTextLogin.getText().toString();
        String password = binding.editTextPassword.getText().toString();
        String passwordRepeat = binding.editTextPasswordRepeat.getText().toString();
        String name = binding.editTextName.getText().toString();
        String surname = binding.editTextSurname.getText().toString();
        Date date = new Date(binding.editTextBirthday.getText().toString());
        String email = binding.editTextEmail.getText().toString();
        String phone = binding.editTextPhone.getText().toString();





        User user = new User(login, password, name, surname, date, email, phone, User.UserType.Client);
        return user;
//    }
//
//        return !(binding.editTextLogin.getText().toString().isEmpty() ||
//                binding.editTextPassword.getText().toString().isEmpty() ||
//                binding.editTextPasswordRepeat.getText().toString().isEmpty() ||
//                binding.editTextName.getText().toString().isEmpty() ||
//                binding.editTextSurname.getText().toString().isEmpty() ||
//                binding.editTextBirthday.getText().toString().isEmpty() ||
//                binding.editTextEmail.getText().toString().isEmpty() ||
//                binding.editTextPhone.getText().toString().isEmpty());
    }
}