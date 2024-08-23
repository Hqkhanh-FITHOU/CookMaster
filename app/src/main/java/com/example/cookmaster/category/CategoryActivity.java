package com.example.cookmaster.category;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookmaster.R;
import com.example.cookmaster.adapter.CategoryAdapter;
import com.example.cookmaster.databinding.ActivityCategoryBinding;
import com.example.cookmaster.model.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private ActivityCategoryBinding binding;
    private List<Category> categories;

    private CategoryAdapter categoryAdapter;
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();

        categories = new ArrayList<>();
        fecthCategories();

        categoryAdapter = new CategoryAdapter(categories, true);
        binding.categoryList.setAdapter(categoryAdapter);

        binding.categoryToolbar.setNavigationOnClickListener(v -> {
            finish();
        });

    }

    private void fecthCategories() {
        binding.indicatorProgress.setVisibility(View.VISIBLE);
        binding.categoryList.setVisibility(View.GONE);

        database.getReference("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Category category = snap.getValue(Category.class);
                    categories.add(category);
                }
                binding.indicatorProgress.setVisibility(View.GONE);
                binding.categoryList.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeFragment", "Error fetching categories", error.toException());
                binding.indicatorProgress.setVisibility(View.GONE);
                binding.categoryList.setVisibility(View.GONE);
                Toast.makeText(CategoryActivity.this, "Lỗi hiển thị danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }
}