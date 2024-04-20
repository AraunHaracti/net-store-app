package com.example.netstore.windows.sign_up_and_in_window;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.netstore.R;
import com.example.netstore.databinding.SignInFragmentBinding;
import com.example.netstore.windows.main_window.MainWindowActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment {

    private SignInFragmentBinding binding;
    private FirebaseAuth mAuth;

    public SignInFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
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

            mAuth.signInWithEmailAndPassword(binding.editTextLogin.getText().toString(),
                            binding.editTextPassword.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            saveCurrentUser(uid);
                            startMainWindowActivity();
                        } else {
                            Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private boolean checkFields() {
        return !(binding.editTextLogin.getText().toString().isEmpty() ||
                binding.editTextPassword.getText().toString().isEmpty());
    }

    private void startMainWindowActivity() {
        Activity currentActivity = getActivity();
        Intent intent = new Intent(currentActivity.getBaseContext(), MainWindowActivity.class);
        startActivity(intent);
        currentActivity.finish();
    }

    private void saveCurrentUser(String uid) {
        // TODO
    }
}