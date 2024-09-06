package com.example.cookmaster.article;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cookmaster.R;
import com.example.cookmaster.adapter.ArticleAdapter;
import com.example.cookmaster.databinding.ActivityImpressArticleBinding;
import com.example.cookmaster.model.Article;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImpressArticleActivity extends AppCompatActivity {
    private ActivityImpressArticleBinding binding;
    private List<Article> impressingArticlesList;
    private ArticleAdapter articleAdapter;
    private FirebaseDatabase database;

    private boolean isSeeAll, isProminent, isMostViewed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImpressArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();

        isMostViewed = binding.mostViewedChip.isChecked();
        isProminent = binding.mostProminentChip.isChecked();
        isSeeAll = binding.seeAllChip.isChecked();



        impressingArticlesList = new ArrayList<>();
        fetchArticlesList();
        articleAdapter = new ArticleAdapter(impressingArticlesList, ArticleAdapter.MINIMALIST_VIEW);
        binding.articleList.setAdapter(articleAdapter);

        binding.chipGroup.setOnCheckedStateChangeListener((chipGroup, list) -> {
            changeChipStatus();
            fetchArticlesList();
            articleAdapter.notifyDataSetChanged();
        });

        binding.articleToolbar.setTitle("Nổi bật");

        binding.articleToolbar.setNavigationOnClickListener(view -> {finish();});


    }

    private void changeChipStatus() {
        isMostViewed = binding.mostViewedChip.isChecked();
        isProminent = binding.mostProminentChip.isChecked();
        isSeeAll = binding.seeAllChip.isChecked();
    }

    private void fetchArticlesList() {
        impressingArticlesList.clear();
        binding.articleList.setVisibility(View.GONE);
        binding.loadingIndicator.setVisibility(View.VISIBLE);

        DatabaseReference reference = database.getReference().child("articles");
        Query query = null;
        if(isProminent){
            query = reference.orderByChild("star").limitToFirst(20);
        }
        else if (isMostViewed){
            query = reference.orderByChild("view").limitToFirst(20);
        }
        else {
            query = reference.orderByChild("star");
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0){
                    for (DataSnapshot snap : snapshot.getChildren()){
                        Article article = snap.getValue(Article.class);
                        if (article.isPublished()){ impressingArticlesList.add(article); }
                    }
                    binding.articleList.setVisibility(View.VISIBLE);
                    binding.loadingIndicator.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ImpressArticleActivity", "Error fetching articles: " + error.getMessage());
                binding.articleList.setVisibility(View.GONE);
                binding.loadingIndicator.setVisibility(View.GONE);
            }
        });


    }
}