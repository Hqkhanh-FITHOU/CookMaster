package com.example.cookmaster.authenticate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cookmaster.MainActivity;
import com.example.cookmaster.R;
import com.example.cookmaster.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        binding.loginButton.setOnClickListener(v -> login());
        binding.registerTextview.setOnClickListener(v -> openRegisterActivity());
        binding.forgotPasswordTextview.setOnClickListener(v -> openForgotPasswordActivity());
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
            binding.emailTextfield.setError("Nhập mật khẩu");
            valid = false;
        }else if(password.length() < 8){
            binding.emailTextfield.setError("Mật khẩu phải có ít nhất 8 ký tự");
            valid = false;
        } else {binding.emailTextfield.setError(null);}
        return valid;
    }

    private void login() {
        if(isValidInput()){
            binding.loginButton.setVisibility(View.INVISIBLE);
            binding.progressCircular.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                binding.loginButton.setVisibility(View.VISIBLE);
                                binding.progressCircular.setVisibility(View.GONE);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
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