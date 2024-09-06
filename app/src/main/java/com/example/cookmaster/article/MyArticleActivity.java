package com.example.cookmaster.article;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cookmaster.R;
import com.example.cookmaster.adapter.ArticleAdapter;
import com.example.cookmaster.databinding.ActivityMyArticleBinding;
import com.example.cookmaster.model.Article;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyArticleActivity extends AppCompatActivity {
    private ActivityMyArticleBinding binding;
    private ActivityResultLauncher<Intent> launcher;
    private ArticleAdapter articleAdapter;
    private FirebaseDatabase database;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        binding.chipPublished.setChecked(true);


        binding.myArticleToolbar.setNavigationOnClickListener(v -> finish());

        binding.myArticleToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.new_article){
                Intent intent = new Intent(this, CreateArticleActivity.class);
                intent.putExtra("create", "create");
                launcher.launch(intent);
                return true;
            }
            return false;
        });


        fetchPublishedArticles();


        binding.myArticleRecyclerView.setAdapter(articleAdapter);

        binding.chipGroup.setOnCheckedStateChangeListener((chipGroup, list) -> {
            if (binding.chipGroup.getCheckedChipId() == R.id.chip_published){

                fetchPublishedArticles();

            } else if (binding.chipGroup.getCheckedChipId() == R.id.chip_draft){

                fetchDraftArticles();

            }
        });


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == CreateArticleActivity.CREATE_ARTICLE) {
                binding.chipPublished.setChecked(true);
                fetchPublishedArticles();
                Toast.makeText(this, "Đã thêm bài viết mới", Toast.LENGTH_SHORT).show();
            }else if (result.getResultCode() == EditArticleActivity.EDIT_ARTICLE){
                binding.chipPublished.setChecked(true);
                fetchPublishedArticles();
                Toast.makeText(this, "Đã cập nhật bài viết", Toast.LENGTH_SHORT).show();
            }
        });



    }



    private void fetchPublishedArticles() {
        binding.myArticleRecyclerView.setVisibility(View.GONE);
        binding.progressIndicator.setVisibility(View.VISIBLE);
        binding.noArticleText.setVisibility(View.GONE);

        if (user!=null){
            DatabaseReference reference = database.getReference("articles");
            reference.orderByChild("createdUserId").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount() > 0){
                        List<Article> articleList = new ArrayList<>();
                        for (DataSnapshot snap : snapshot.getChildren()){
                            Article article = snap.getValue(Article.class);
                            if(article.isPublished()){
                                articleList.add(article);
                            }
                        }
                        if (!articleList.isEmpty()){
                            binding.myArticleRecyclerView.setVisibility(View.VISIBLE);
                            binding.progressIndicator.setVisibility(View.GONE);
                            binding.noArticleText.setVisibility(View.GONE);
                            articleAdapter = new ArticleAdapter(articleList,ArticleAdapter.EDIT_VIEW);
                            binding.myArticleRecyclerView.setAdapter(articleAdapter);
                        }else{
                            binding.myArticleRecyclerView.setVisibility(View.GONE);
                            binding.progressIndicator.setVisibility(View.GONE);
                            binding.noArticleText.setVisibility(View.VISIBLE);
                        }

                    }else {
                        binding.myArticleRecyclerView.setVisibility(View.GONE);
                        binding.progressIndicator.setVisibility(View.GONE);
                        binding.noArticleText.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MyArticleActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "fetchDraftArticles: "+error.getMessage());
                }
            });
        }
    }

    private void fetchDraftArticles(){
        binding.myArticleRecyclerView.setVisibility(View.GONE);
        binding.progressIndicator.setVisibility(View.VISIBLE);
        binding.noArticleText.setVisibility(View.GONE);

        if (user!=null){
            DatabaseReference reference = database.getReference("articles");
            reference.orderByChild("createdUserId").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getChildrenCount() > 0){
                                List<Article> articleList = new ArrayList<>();
                                for (DataSnapshot snap : snapshot.getChildren()){
                                    Article article = snap.getValue(Article.class);
                                    if(!article.isPublished()){
                                        articleList.add(article);
                                    }
                                }
                                if (!articleList.isEmpty()){
                                    binding.myArticleRecyclerView.setVisibility(View.VISIBLE);
                                    binding.progressIndicator.setVisibility(View.GONE);
                                    binding.noArticleText.setVisibility(View.GONE);
                                    articleAdapter = new ArticleAdapter(articleList,ArticleAdapter.EDIT_VIEW);
                                    binding.myArticleRecyclerView.setAdapter(articleAdapter);
                                }else{
                                    binding.myArticleRecyclerView.setVisibility(View.GONE);
                                    binding.progressIndicator.setVisibility(View.GONE);
                                    binding.noArticleText.setVisibility(View.VISIBLE);
                                }
                            }else {
                                binding.myArticleRecyclerView.setVisibility(View.GONE);
                                binding.progressIndicator.setVisibility(View.GONE);
                                binding.noArticleText.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MyArticleActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "fetchDraftArticles: "+error.getMessage());
                        }
                    });

        }
    }


}