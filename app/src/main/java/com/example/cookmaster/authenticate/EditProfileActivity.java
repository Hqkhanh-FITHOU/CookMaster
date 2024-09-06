package com.example.cookmaster.authenticate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.cookmaster.R;
import com.example.cookmaster.databinding.ActivityEditProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {
    private ActivityEditProfileBinding binding;
    private FirebaseUser user;
    private ActivityResultLauncher<Intent> launcher;
    private Uri localImageUri;
    private FirebaseStorage storage;

    public static final int EDIT_PROFILE = 456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user = FirebaseAuth.getInstance().getCurrentUser();

        binding.usernameInput.setText(user.getDisplayName());
        binding.emailInput.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).into(binding.profileImage);

        binding.editProfileToolbar.setNavigationOnClickListener(v -> finish());

        binding.cancelButton.setOnClickListener(v -> finish());

        binding.confirmButton.setOnClickListener(v -> editProfile());

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                localImageUri = result.getData().getData();
                binding.profileImage.setImageURI(localImageUri);
            }
        });

        binding.changeAvatarButton.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            intent.setType("image/*");
            launcher.launch(intent);
        });
    }

    private void editProfile() {
        binding.indicatorProgress.setVisibility(View.VISIBLE);
        binding.confirmButton.setVisibility(View.INVISIBLE);
        if(isValidInput()){
            String username = binding.usernameInput.getText().toString().trim();
            if(user != null ){
                if(localImageUri != null){
                    storage = FirebaseStorage.getInstance();
                    String filename = UUID.randomUUID().toString();
                    StorageReference fileRef = storage.getReference().child("profile_images/" + filename);
                    fileRef.putFile(localImageUri).addOnSuccessListener(taskSnapshot -> {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                            UserProfileChangeRequest profileUpdateRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .setPhotoUri(uri)
                                    .build();
                            user.updateProfile(profileUpdateRequest).addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    binding.indicatorProgress.setVisibility(View.GONE);
                                    binding.confirmButton.setVisibility(View.VISIBLE);
                                    setResult(EDIT_PROFILE);
                                    finish();
                                }
                            });
                        });
                    });
                }else{
                    UserProfileChangeRequest profileUpdateRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();
                    user.updateProfile(profileUpdateRequest).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            binding.indicatorProgress.setVisibility(View.GONE);
                            binding.confirmButton.setVisibility(View.VISIBLE);
                            finish();
                        }
                    });
                }

            }
        }else {
            Toast.makeText(this, "update error", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidInput() {
        String username = binding.usernameInput.getText().toString().trim();
        String email = binding.emailInput.getText().toString().trim();
        boolean valid = true;
        if (username.isEmpty()) {
            binding.usernameTextfield.setError("Nhập tên người dùng");
            valid = false;
        }
        if (email.isEmpty()) {
            binding.emailTextfield.setError("Nhập email");
            valid = false;
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailTextfield.setError("Email không hợp lệ");
            valid = false;
        }
        return true;
    }
}