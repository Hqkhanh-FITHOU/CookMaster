package com.example.cookmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.cookmaster.authenticate.LoginActivity;
import com.example.cookmaster.authenticate.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private FirebaseAuth mAuth;
    private boolean navigatable = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sessionManager = new SessionManager(this);
        mAuth = FirebaseAuth.getInstance();
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> {
            if (navigatable){
                return false;
            }
            return true;
        });
        super.onCreate(savedInstanceState);


        navigate();
    }

    private void navigate() {
        if(sessionManager.isLogin()) {
            mAuth.signInWithEmailAndPassword(sessionManager.getEmail(), sessionManager.getPassword()).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Log.i("Login", "Login successfully");
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    navigatable = true;
                    finish();
                }
            }).addOnFailureListener(e -> {
                Log.i("Login", e.getMessage());
            });
        }else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            navigatable = true;
            finish();
        }
    }
}
