package com.example.netstore.windows.sign_up_and_in_window;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.netstore.Config;
import com.example.netstore.ObserverObject;
import com.example.netstore.R;
import com.example.netstore.databinding.SignInFragmentBinding;
import com.example.netstore.models.User;
import com.example.netstore.viewModels.UserViewModel;
import com.example.netstore.windows.main_window.MainWindowActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.Objects;

public class SignInFragment extends Fragment {

    private SignInFragmentBinding binding;
    public SignInFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = SignInFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.signUpBtn.setOnClickListener(v ->
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, new SignUpFragment())
                        .addToBackStack("sign_up")
                        .commit());

        binding.signInBtn.setOnClickListener(v -> {
            if (!checkFields())
                return;


            String email = binding.editTextEmail.getText().toString();
            String password = binding.editTextPassword.getText().toString();

            UserViewModel viewModel = new UserViewModel();

            viewModel.getInfoData().observe(getViewLifecycleOwner(), new Observer<ObserverObject>() {
                @Override
                public void onChanged(ObserverObject observerObject) {
                    if (Objects.equals(observerObject.tag, "auth user") && observerObject.status) {
                        saveCurrentUser((User)observerObject.item);
                        startMainWindowActivity();
                    }
                    else {
                        Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            viewModel.authenticationUser(email, password);
        });
    }

    private boolean checkFields() {
        return !(binding.editTextEmail.getText().toString().isEmpty() ||
                binding.editTextPassword.getText().toString().isEmpty());
    }

    private void startMainWindowActivity() {
        Activity currentActivity = getActivity();
        Intent intent = new Intent(currentActivity.getBaseContext(), MainWindowActivity.class);
        startActivity(intent);
        currentActivity.finish();
    }

    private void saveCurrentUser(User user) {
        String currentUserJson = new Gson().toJson(user, User.class);
        SharedPreferences.Editor editorSP = getActivity().getSharedPreferences(Config.SP_FILE_TAG, Context.MODE_PRIVATE).edit();
        editorSP.putString(Config.SP_USER_TAG, currentUserJson);
        editorSP.apply();
    }
}