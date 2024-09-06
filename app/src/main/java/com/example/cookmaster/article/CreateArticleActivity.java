package com.example.cookmaster.article;

import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cookmaster.ProgressViewDialog;
import com.example.cookmaster.R;
import com.example.cookmaster.databinding.ActivityCreateArticleBinding;
import com.example.cookmaster.model.Article;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class CreateArticleActivity extends AppCompatActivity {
    private ActivityCreateArticleBinding binding;
    public static final int CREATE_ARTICLE = 1021;
    private static final int IMAGE_PICKER_REQUEST = 1022;
    private String[] categories;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private Article article;
    private Uri localImageUri;
    private FirebaseUser user;
    private MaterialAlertDialogBuilder saveDaftDialog;
    private ProgressViewDialog saveDialog;

    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user = FirebaseAuth.getInstance().getCurrentUser();
        binding.topAppBar.setNavigationOnClickListener(v -> finish());

        if(getIntent().getSerializableExtra("article") != null){
            article = (Article) getIntent().getSerializableExtra("article");
            binding.titleArticle.setText(article.getTitle());
            binding.introArticle.setText(article.getIntroduction());
            binding.instructArticle.setText(article.getInstruction());
            binding.categoryArticle.setText(article.getCategory());
            Glide.with(this).load(article.getImage()).into(binding.illustrationImage);
        }

        categories = Article.getArrayOfCategories();

        binding.categoryArticle.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories));

        binding.publishButton.setOnClickListener(v -> {
            onClickPublish();
        });

        binding.topAppBar.setOnMenuItemClickListener(this::onMenuItemClick);

        binding.cancelButton.setOnClickListener(v->{
            onClickCancel();
        });

        binding.noteImg.setOnClickListener(v -> {
            onClickNote();
        });

        binding.illustrationImage.setOnClickListener(v -> {
            onChooseImage();
        });

        onTextChanged();

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        localImageUri = result.getData().getData();
                        Glide.with(this)
                                .load(localImageUri).into(binding.illustrationImage);
                    }
                }
        );

        if (binding.titleArticle.getText().toString().isEmpty()){
            binding.topAppBar.getMenu().getItem(0).setVisible(false);
        }
    }

    private void onChooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        launcher.launch(Intent.createChooser(intent, "Select Picture"));
    }


    private void onClickNote() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Ảnh minh họa")
                .setMessage("Ảnh này sẽ hiển thị đại diện cho bài viết và sẽ được đặt ở giữa hai phần giới thiệu và hướng dẫn khi nhấn mở bài viết")
                .setPositiveButton("Ok", (dialog, which) -> {
                    //do nothing
                }).show();
    }

    private void onClickCancel() {
        finish();
    }

    private boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.save_draft){
            saveDraft();
            return true;
        }
        return false;
    }

    private void saveDraft() {
        saveDaftDialog = new MaterialAlertDialogBuilder(this);
        saveDaftDialog.setTitle("Lưu bản nháp")
                .setMessage("Lưu lại bản nháp và dừng viết bài")
                .setNegativeButton("Hủy bỏ", (dialog, which) -> {
                    //do nothing
                    dialog.dismiss();
                })
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    upLoadImageAndCreateArticle(localImageUri, false);
                    dialog.dismiss();
                    finish();
                }).show();

    }



    private void upLoadImageAndCreateArticle(Uri imageUri, boolean publishArticle){
        saveDialog = new ProgressViewDialog(this);

        String titleString = binding.titleArticle.getText().toString().trim();
        String introductionString = binding.introArticle.getText().toString().trim();
        String instructionString = binding.instructArticle.getText().toString().trim();
        String categoryString = binding.categoryArticle.getText().toString().trim();
        Date publishAt = new Date();

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        String newFileName = UUID.randomUUID().toString();

        if(imageUri == null){
            saveDialog.setMessage("Đang lưu bản nháp");
            saveDialog.show();
            article = new Article();
            article.setTitle(titleString);
            article.setIntroduction(introductionString);
            article.setInstruction(instructionString);
            article.setCategory(categoryString);
            article.setPublished(publishArticle);
            article.setPublishedAt(publishAt);
            article.setView(0);
            article.setStar(0);
            article.setImage("");
            article.setCreatedUserId(user.getUid());

            if(Objects.equals(getIntent().getStringExtra("create"), "create")){
                article.setId(UUID.randomUUID().toString());
            }else if(getIntent().getStringExtra("update") != null){
                article.setId(getIntent().getStringExtra("update"));
            }

            database.getReference().child("articles").child(article.getId()).setValue(article);
            saveDialog.dismiss();
            return;
        }
        saveDialog.setMessage("Đang đăng tải");
        saveDialog.show();
        StorageReference fileRef = storage.getReference().child("article_images/" + newFileName);
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                article = new Article();
                article.setTitle(titleString);
                article.setIntroduction(introductionString);
                article.setInstruction(instructionString);
                article.setCategory(categoryString);
                article.setPublished(publishArticle);
                article.setPublishedAt(publishAt);
                article.setId(UUID.randomUUID().toString());
                article.setView(0);
                article.setStar(0);
                article.setImage(uri.toString());
                article.setCreatedUserId(user.getUid());

                database.getReference().child("articles").child(article.getId()).setValue(article);
                saveDialog.dismiss();
            });
        }).addOnFailureListener(command -> {
            saveDialog.dismiss();
            Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show();
        });
    }


    private void onClickPublish() {
        if(validateInputData()){
            upLoadImageAndCreateArticle(localImageUri, true);
            setResult(CREATE_ARTICLE);
            finish();
        }
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
        if(localImageUri == null){
            Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void onTextChanged() {
        //title text field
        binding.titleArticle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 0){
                    binding.titleArticleTextfield.setError(null);
                    binding.topAppBar.getMenu().getItem(0).setVisible(true);
                }else {
                    binding.topAppBar.getMenu().getItem(0).setVisible(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //instruction text field
        binding.instructArticle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 0){
                    binding.instructArticleTextfield.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //introduction text field
        binding.introArticle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0){
                    binding.introArticleTextfield.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.categoryArticle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0){
                    binding.categoryArticleTextfield.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(saveDialog!=null){
            saveDialog.dismiss();
        }
    }
}