package com.example.cookmaster.article;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cookmaster.R;
import com.example.cookmaster.adapter.ArticleAdapter;
import com.example.cookmaster.databinding.ActivityArticleByCategoryBinding;
import com.example.cookmaster.model.Article;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArticleByCategoryActivity extends AppCompatActivity {
    private ActivityArticleByCategoryBinding binding;

    private List<Article> articles;
    private ArticleAdapter articleAdapter;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArticleByCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.articleToolbar.setTitle(getIntent().getStringExtra("categoryName"));

        binding.articleToolbar.setNavigationOnClickListener(v -> {finish();});

        database = FirebaseDatabase.getInstance();

        articles = new ArrayList<>();
        fetchArticles();
        articleAdapter = new ArticleAdapter(articles, ArticleAdapter.FULL_VIEW);
        binding.articleList.setAdapter(articleAdapter);

    }

    private void fetchArticles() {
        binding.loadingIndicator.setVisibility(View.VISIBLE);
        binding.articleList.setVisibility(View.GONE);
        binding.noArticleText.setVisibility(View.GONE);
        DatabaseReference reference = database.getReference("articles");
        Query query = reference.orderByChild("category").equalTo(getIntent().getStringExtra("categoryName"));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() > 0){
                    for (DataSnapshot snap : snapshot.getChildren()){
                        Article article = snap.getValue(Article.class);
                        if (article.isPublished()){ articles.add(article); }
                    }
                    binding.noArticleText.setVisibility(View.GONE);
                    binding.loadingIndicator.setVisibility(View.GONE);
                    binding.articleList.setVisibility(View.VISIBLE);
                }else {
                    binding.noArticleText.setVisibility(View.VISIBLE);
                    binding.loadingIndicator.setVisibility(View.GONE);
                    binding.articleList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.noArticleText.setVisibility(View.VISIBLE);
                binding.noArticleText.setText("Lỗi hiển thị dữ liệu");
                binding.loadingIndicator.setVisibility(View.GONE);
                binding.articleList.setVisibility(View.GONE);
                Log.e("ArticleByCategoryActivity", error.getMessage());
            }
        });
    }

}