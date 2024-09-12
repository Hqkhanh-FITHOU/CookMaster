package com.example.cookmaster.authenticate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cookmaster.MainActivity;
import com.example.cookmaster.R;
import com.example.cookmaster.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private String email, password;

    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(this);

        binding.loginButton.setOnClickListener(v -> login());
        binding.registerTextview.setOnClickListener(v -> openRegisterActivity());
        binding.forgotPasswordTextview.setOnClickListener(v -> openForgotPasswordActivity());
        onTextChanged();


    }

    private void onTextChanged() {
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
    }



    private void checkLoginState() {
        if(sessionManager.isLogin()) {
            mAuth.signInWithEmailAndPassword(sessionManager.getEmail(), sessionManager.getPassword()).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Log.i("Login", "Login successfully");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(e -> {
                Log.i("Login", e.getMessage());
            });
        }else {
            Log.i("Login", "login failed");
            Toast.makeText(this, "login failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void openForgotPasswordActivity() {
        //TODO: open forgot password activity
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }


    private boolean isValidInput(){
        email = binding.emailInput.getText().toString().trim();
        password = binding.passwordInput.getText().toString().trim();
        boolean valid = true;
        //check email
        if(email.isEmpty()){
            binding.emailTextfield.setError("Nhập email");
            valid = false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            valid = false;
            binding.emailTextfield.setError("Email không hợp lệ");
        } else {binding.emailTextfield.setError(null);}
        //check password
        if(password.isEmpty()){
            binding.passwordTextfield.setError("Nhập mật khẩu");
            valid = false;
        }else if(password.length() < 8){
            binding.passwordTextfield.setError("Mật khẩu phải có ít nhất 8 ký tự");
            valid = false;
        } else {binding.emailTextfield.setError(null);}
        return valid;
    }

    private void login() {
        if(isValidInput()){
            binding.loginButton.setVisibility(View.INVISIBLE);
            binding.progressCircular.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            binding.loginButton.setVisibility(View.VISIBLE);
                            binding.progressCircular.setVisibility(View.GONE);

                            sessionManager.saveLoginInfo(email, password);
                            sessionManager.setLoginState(true);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        binding.loginButton.setVisibility(View.VISIBLE);
                        binding.progressCircular.setVisibility(View.GONE);
                        e.printStackTrace();
                    });
        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }
}