package com.example.cookmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cookmaster.R;
import com.example.cookmaster.article.ArticleByCategoryActivity;
import com.example.cookmaster.model.Category;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Category> categories;
    private final int EXPANDED_VIEW = 1;
    private final int NORMAL_VIEW = 0;
    private boolean isExpanded;

    public CategoryAdapter(List<Category> categories, boolean expanded) {
        this.categories = categories;
        this.isExpanded = expanded;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == EXPANDED_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item2, parent, false);
            return new CategoryExpandedViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryNormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Category category = categories.get(position);
        if (holder.getItemViewType() == EXPANDED_VIEW) {
            CategoryExpandedViewHolder expandedViewHolder = (CategoryExpandedViewHolder) holder;
            expandedViewHolder.categoryName2.setText(category.getCategoryName());
            Glide.with(holder.itemView.getContext()).load(category.getCategoryImage()).into(expandedViewHolder.categoryImage2);
            expandedViewHolder.layout2.setOnClickListener(v -> {openArticleByCategoryActivity(expandedViewHolder.itemView.getContext(), category);});
        }else{
            CategoryNormalViewHolder normalViewHolder = (CategoryNormalViewHolder) holder;
            normalViewHolder.categoryName.setText(category.getCategoryName());
            Glide.with(holder.itemView.getContext()).load(category.getCategoryImage()).into(normalViewHolder.categoryImage);
            normalViewHolder.layout.setOnClickListener(v -> {openArticleByCategoryActivity(normalViewHolder.itemView.getContext(), category);});
        }
    }

    private void openArticleByCategoryActivity(Context context, Category category) {
        Intent intent = new Intent(context, ArticleByCategoryActivity.class);
        intent.putExtra("categoryName", category.getCategoryName());
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        if(!categories.isEmpty()){
            return categories.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isExpanded) {
            return EXPANDED_VIEW;
        }
        return NORMAL_VIEW;
    }

    public class CategoryNormalViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
        MaterialCardView layout;
        public CategoryNormalViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.category_item_image);
            categoryName = itemView.findViewById(R.id.category_item_title);
            layout = itemView.findViewById(R.id.category_item_layout);
        }
    }

    public class CategoryExpandedViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage2;
        TextView categoryName2;
        MaterialCardView layout2;
        public CategoryExpandedViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage2 = itemView.findViewById(R.id.category_item2_image);
            categoryName2 = itemView.findViewById(R.id.category_item2_title);
            layout2 = itemView.findViewById(R.id.category_item2_layout);
        }
    }
}
