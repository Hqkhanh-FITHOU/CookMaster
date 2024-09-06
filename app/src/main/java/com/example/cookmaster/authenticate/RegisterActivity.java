package com.example.cookmaster.authenticate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cookmaster.R;
import com.example.cookmaster.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private String email, password, confirmPassword, username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        binding.registerButton.setOnClickListener(v -> register());
        onInputFieldChange();

        binding.loginTextview.setOnClickListener(v -> openLoginActivity());
    }

    private void openLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void onInputFieldChange() {
        binding.usernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.usernameTextfield.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.emailTextfield.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.passwordTextfield.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.confirmPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.congfirmPasswordTextfield.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean isVaildInput(){
        email = binding.emailInput.getText().toString().trim();
        password = binding.passwordInput.getText().toString().trim();
        username = binding.usernameInput.getText().toString().trim();
        confirmPassword = binding.confirmPasswordInput.getText().toString().trim();
        boolean valid = true;
        if (email.isEmpty()){
            binding.emailTextfield.setError("Nhập email");
            valid = false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailTextfield.setError("Email không hợp lệ");
            valid = false;
        }
        if (password.isEmpty()){
            binding.passwordTextfield.setError("Đặt mật khẩu");
            valid = false;
        }else if(password.length() < 8){
            binding.passwordTextfield.setError("Mật khẩu phải có ít nhất 8 ký tự");
            valid = false;
        }
        if (username.isEmpty()){
            binding.usernameTextfield.setError("Xác nhận mật khẩu");
            valid = false;
        }
        if (confirmPassword.isEmpty()){
            binding.congfirmPasswordTextfield.setError("Nhập xác nhận mật khẩu");
            valid = false;
        }else if (!password.equals(confirmPassword)){
            binding.congfirmPasswordTextfield.setError("Mật khẩu xác nhận không khớp");
            valid = false;
        }
        return valid;
    }

    private void register() {
        if (isVaildInput()){
            binding.registerButton.setVisibility(View.INVISIBLE);
            binding.progressCircular.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            //.setPhotoUri(uri)
                            .build();

                    if (user != null) {
                        user.updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                            binding.registerButton.setVisibility(View.VISIBLE);
                            binding.progressCircular.setVisibility(View.GONE);
                        });
                    }
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}