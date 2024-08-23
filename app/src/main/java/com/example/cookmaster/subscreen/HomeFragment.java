package com.example.cookmaster.subscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cookmaster.adapter.ArticleAdapter;
import com.example.cookmaster.adapter.CategoryAdapter;
import com.example.cookmaster.adapter.SliderAdapter;
import com.example.cookmaster.article.ImpressArticleActivity;
import com.example.cookmaster.category.CategoryActivity;
import com.example.cookmaster.databinding.FragmentHomeBinding;
import com.example.cookmaster.model.Article;
import com.example.cookmaster.model.Category;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private FirebaseDatabase database;

    private SliderAdapter sliderAdapter;
    private List<Category> categories;
    private List<Article> articles;
    private ArticleAdapter articleAdapter;
    private CategoryAdapter categoryAdapter;

    private ActivityResultLauncher<Intent> launcher;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View homeScreen = binding.getRoot();
        // Inflate the layout for this fragment
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            }
        });

        database = FirebaseDatabase.getInstance();

        categories = new ArrayList<>();
        fetchCategories();
        categoryAdapter = new CategoryAdapter(categories, false);
        binding.categoryRecyclerView.setAdapter(categoryAdapter);

        sliderAdapter = new SliderAdapter();
        CarouselSnapHelper snapHelper = new CarouselSnapHelper();
        snapHelper.attachToRecyclerView(binding.silderRecyclerView);
        binding.silderRecyclerView.setAdapter(sliderAdapter);

        articles = new ArrayList<>();
        fetchArticles();
        articleAdapter = new ArticleAdapter(articles, true);
        binding.exploreMealRecyclerView.setAdapter(articleAdapter);

        binding.moreCategoryText.setOnClickListener(v -> {
            openCategoryActivity();
        });

        binding.exploreMealMoreText.setOnClickListener(v -> {
            openImpressArticleActivity();
        });

        return homeScreen;
    }

    private void openCategoryActivity() {
        Intent intent = new Intent(getContext(), CategoryActivity.class);
        launcher.launch(intent);
    }

    private void openImpressArticleActivity() {
        Intent intent = new Intent(getContext(), ImpressArticleActivity.class);
        launcher.launch(intent);
    }

    private void fetchCategories() {
        binding.categoryProgressIndicator.setVisibility(View.VISIBLE);
        binding.categoryRecyclerView.setVisibility(View.GONE);

        database.getReference("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Category category = snap.getValue(Category.class);
                    categories.add(category);
                }
                binding.categoryProgressIndicator.setVisibility(View.GONE);
                binding.categoryRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeFragment", "Error fetching categories", error.toException());
                binding.categoryProgressIndicator.setVisibility(View.GONE);
                binding.categoryRecyclerView.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Lỗi hiển thị danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchArticles() {
        binding.exploreMealProgressIndicator.setVisibility(View.VISIBLE);
        binding.exploreMealRecyclerView.setVisibility(View.GONE);
        binding.exploreMealEmptyText.setVisibility(View.GONE);

        DatabaseReference reference = database.getReference("articles");
        Query query = reference.orderByChild("star").limitToFirst(12);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 0) {
                    binding.exploreMealEmptyText.setVisibility(View.VISIBLE);
                    binding.exploreMealProgressIndicator.setVisibility(View.GONE);
                    binding.exploreMealRecyclerView.setVisibility(View.GONE);
                }else{
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Article article = snap.getValue(Article.class);
                        assert article != null;
                        if(article.isPublished()){
                            articles.add(article);
                        }
                    }
                    binding.exploreMealProgressIndicator.setVisibility(View.GONE);
                    binding.exploreMealRecyclerView.setVisibility(View.VISIBLE);
                    binding.exploreMealEmptyText.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeFragment", "Error fetching categories", error.toException());
                binding.exploreMealProgressIndicator.setVisibility(View.GONE);
                binding.exploreMealRecyclerView.setVisibility(View.GONE);
                binding.exploreMealEmptyText.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Lỗi hiển thị danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}