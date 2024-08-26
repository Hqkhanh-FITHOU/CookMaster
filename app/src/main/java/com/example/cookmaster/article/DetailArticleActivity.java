package com.example.cookmaster.article;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.cookmaster.R;
import com.example.cookmaster.databinding.ActivityDetailArticleBinding;
import com.example.cookmaster.model.Article;
import com.google.firebase.database.FirebaseDatabase;

public class DetailArticleActivity extends AppCompatActivity {
    private ActivityDetailArticleBinding binding;
    private Article article;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        binding.articleDetailToolbar.setNavigationOnClickListener(v -> finish());

        fetchArticle();

        binding.ratingButton.setOnClickListener(v -> openRatingBottomSheet());
    }

    private void openRatingBottomSheet() {
        //to do
        Toast.makeText(this, "rating", Toast.LENGTH_SHORT).show();
    }

    private void fetchArticle() {
        article = (Article) getIntent().getSerializableExtra("article");

        if (article != null) {
            binding.title.setText(article.getTitle());
            binding.introArticleText.setText(article.getIntroduction());
            binding.instructArticleText.setText(article.getInstruction());
            Glide.with(this).load(article.getImage()).into(binding.image);
            binding.viewArticleText.setText(article.getView()+"");
            binding.ratingText.setText(article.getStar()+"");
        }
    }

    private void fetchCreatedUser() {
        //to do
    }

}