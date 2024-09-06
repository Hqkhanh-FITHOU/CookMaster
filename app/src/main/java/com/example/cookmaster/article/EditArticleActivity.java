package com.example.cookmaster.article;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.cookmaster.ProgressViewDialog;
import com.example.cookmaster.R;
import com.example.cookmaster.databinding.ActivityEditArticleBinding;
import com.example.cookmaster.model.Article;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class EditArticleActivity extends AppCompatActivity {
    public static final int EDIT_ARTICLE = 32164;
    private ActivityEditArticleBinding binding;
    private String[] categories;
    private Article article;

    private FirebaseDatabase database;
    private Uri localImageUri;
    private ActivityResultLauncher<Intent> launcher;

    private ProgressViewDialog updateDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.topAppBar.setNavigationOnClickListener(v -> finish());

        categories = Article.getArrayOfCategories();
        binding.categoryArticle.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories));

        if(getIntent().getSerializableExtra("article") != null){
            article = (Article) getIntent().getSerializableExtra("article");
            binding.titleArticle.setText(article.getTitle());
            binding.introArticle.setText(article.getIntroduction());
            binding.instructArticle.setText(article.getInstruction());
            binding.categoryArticle.setText(article.getCategory());
            Glide.with(this).load(article.getImage()).into(binding.illustrationImage);
        }
        binding.cancelButton.setOnClickListener(v -> finish());
        
        binding.confirmButton.setOnClickListener(v -> {
            if (validateInputData()){
                onUpdateArticle();
            }
        });

        binding.chooseImageButton.setOnClickListener(v -> onPickImage());

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == RESULT_OK && result.getData() != null){
                localImageUri = result.getData().getData();
                Glide.with(this).load(localImageUri).into(binding.illustrationImage);
            }
        });
    }

    private void onPickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        launcher.launch(Intent.createChooser(intent, "Chọn ảnh"));
    }

    private void onUpdateArticle() {
        updateDialog = new ProgressViewDialog(this);
        updateDialog.setMessage("Đang lưu bài viết");
        updateDialog.show();

        database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String title = binding.titleArticle.getText().toString();
        String introduction = binding.introArticle.getText().toString();
        String instruction = binding.instructArticle.getText().toString();
        String category = binding.categoryArticle.getText().toString();

        article.setTitle(title);
        article.setIntroduction(introduction);
        article.setInstruction(instruction);
        article.setCategory(category);
        String newFileName = UUID.randomUUID().toString();
        if(localImageUri != null){
            article.setImage(localImageUri.toString());
            Uri dowloadUri = Uri.parse(article.getImage());
            StorageReference storageReference1 = storage.getReference().child("article_images/"+ newFileName);
            StorageReference storageReference2 = storage.getReference().child("article_images/"+ dowloadUri.getLastPathSegment());
            storageReference2.delete().addOnSuccessListener(aVoid -> {
                storageReference1.putFile(localImageUri).addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        article.setImage(uri.toString());
                    });
                });
            });
        }

        DatabaseReference reference = database.getReference("articles").child(article.getId());
        reference.setValue(article).addOnSuccessListener(command -> {
            setResult(EDIT_ARTICLE);
            updateDialog.dismiss();
            finish();
        });
    }

    private boolean validateInputData(){
        if(binding.titleArticle.getText().toString().isEmpty()){
            binding.titleArticleTextfield.setError("Tiêu đề trống");
            return false;
        }
        if(binding.categoryArticle.getText().toString().isEmpty()){
            binding.categoryArticleTextfield.setError("Chọn thể loại");
            return false;
        }
        if (binding.introArticle.getText().toString().isEmpty()){
            binding.introArticleTextfield.setError("Giới thiệu trống");
            return false;
        }
        if (binding.instructArticle.getText().toString().isEmpty()){
            binding.instructArticleTextfield.setError("Hướng dẫn trống");
            return false;
        }
//        if(localImageUri == null){
//            Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateDialog != null){
            updateDialog.dismiss();
        }
    }
}