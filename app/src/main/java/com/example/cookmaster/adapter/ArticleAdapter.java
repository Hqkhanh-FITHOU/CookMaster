package com.example.cookmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cookmaster.R;
import com.example.cookmaster.article.CreateArticleActivity;
import com.example.cookmaster.article.DetailArticleActivity;
import com.example.cookmaster.article.EditArticleActivity;
import com.example.cookmaster.model.Article;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int FULL_VIEW = 1;
    public static final int MINIMALIST_VIEW = 2;
    public static final int EDIT_VIEW = 3;
    private List<Article> articles;

    private int curentViewType;
    private FirebaseDatabase database;




    public ArticleAdapter(List<Article> articles, int viewType) {
        this.articles = articles;
        this.curentViewType = viewType;
    }

    public void setArticles(List<Article> articles){
        this.articles = articles;
    }

    public void setViewType(int viewType){
        this.curentViewType = viewType;
    }





    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case MINIMALIST_VIEW:{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item2, parent, false);
                return new ArticleMinimalistViewHolder(view);
            }
            case EDIT_VIEW:{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item3, parent, false);
                return new ArticleEditViewHolder(view);
            }
            default: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
                return new ArticleFullViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Article article = articles.get(position);
        if (curentViewType == FULL_VIEW) {
            ArticleFullViewHolder holder1 = (ArticleFullViewHolder) holder;
            holder1.articleTitle.setText(article.getTitle());
            holder1.articleRating.setText(article.getStar()+"");
            holder1.articleFavorit.setText("0");
            holder1.articleView.setText(article.getView()+"");
            holder1.articleCategory.setText(article.getCategory());
            if(article.getImage() != null){
                Glide.with(holder.itemView.getContext()).load(article.getImage()).into(holder1.articleImage);
            }else{
                Glide.with(holder.itemView.getContext()).load(R.drawable.image).into(holder1.articleImage);
            }
            holder1.itemLayout.setOnClickListener(v -> {
                increaseView(article.getId(), article.getView());
                openDetailArticleActivity(holder1.itemView.getContext(), article);
            });
        }else if (curentViewType == MINIMALIST_VIEW) {
            ArticleMinimalistViewHolder holder2 = (ArticleMinimalistViewHolder) holder;
            holder2.articleTitle2.setText(article.getTitle());
            holder2.articleRating2.setText(article.getStar()+"");
            holder2.articleFavorit2.setText("0");
            holder2.articleView2.setText(article.getView()+"");
            holder2.articleCategory2.setText(article.getCategory());
            if(article.getImage() != null){
                Glide.with(holder.itemView.getContext()).load(article.getImage()).into(holder2.articleImage2);
            }else{
                Glide.with(holder.itemView.getContext()).load(R.drawable.image).into(holder2.articleImage2);
            }
            holder2.itemLayout2.setOnClickListener(v -> {
                increaseView(article.getId(), article.getView());
                openDetailArticleActivity(holder2.itemView.getContext(), article);

            });
        }else {
            ArticleEditViewHolder holder3 = (ArticleEditViewHolder) holder;
            holder3.articleTitle3.setText(article.getTitle());
            holder3.articleRating3.setText(article.getStar()+"");
            holder3.articleFavorit3.setText("0");
            holder3.articleView3.setText(article.getView()+"");
            holder3.articleCategory3.setText(article.getCategory());
            if(article.getImage() != null){
                Glide.with(holder.itemView.getContext()).load(article.getImage()).into(holder3.articleImage3);
            }else{
                Glide.with(holder.itemView.getContext()).load(R.drawable.image).into(holder3.articleImage3);
            }
            holder3.itemLayout3.setOnClickListener(v -> {
                if(article.isPublished()){
                    Intent intent = new Intent(holder3.itemView.getContext(), EditArticleActivity.class);
                    intent.putExtra("article", article);
                    holder3.itemView.getContext().startActivity(intent);
                }else{
                    Intent intent = new Intent(holder3.itemView.getContext(), CreateArticleActivity.class);
                    intent.putExtra("article", article);
                    holder3.itemView.getContext().startActivity(intent);

                }
            });
            holder3.editButton.setOnClickListener(v -> {
                if(article.isPublished()){
                    Intent intent = new Intent(holder3.itemView.getContext(), EditArticleActivity.class);
                    intent.putExtra("article", article);
                    holder3.itemView.getContext().startActivity(intent);
                }else{
                    Intent intent = new Intent(holder3.itemView.getContext(), CreateArticleActivity.class);
                    intent.putExtra("article", article);
                    holder3.itemView.getContext().startActivity(intent);
                }
            });
            holder3.deleteButton.setOnClickListener(v -> {
                if(article.isPublished()){
                    onChangeOwnership(holder3.itemView.getContext(), article, position);

                }else {
                    onArticleDelete(holder3.itemView.getContext(), article, position);

                }
            });
        }
    }

    private void onChangeOwnership(Context context, Article article, int position) {
        database = FirebaseDatabase.getInstance();
        new MaterialAlertDialogBuilder(context)
                .setTitle("Xóa bài viết")
                .setMessage("Xóa bài viết vĩnh viễn")
                .setNegativeButton("Hủy bỏ", (dialog, which) -> {
                    //do nothing
                    dialog.dismiss();
                })
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    DatabaseReference reference = database.getReference("articles");
                    reference.child(article.getId()).child("createdUserId").setValue("Admin").addOnSuccessListener(task -> {
                        articles.remove(position);
                        notifyItemRemoved(position);
                        dialog.dismiss();
                        Log.d("ArticleAdapter", "articles.size(): " + articles.size());
                    });
                }).show();
    }

    private void onArticleDelete(Context context, Article article, int position) {
        database = FirebaseDatabase.getInstance();
        new MaterialAlertDialogBuilder(context)
                .setTitle("Xóa bài viết")
                .setMessage("Xóa bài viết vĩnh viễn")
                .setNegativeButton("Hủy bỏ", (dialog, which) -> {
                    //do nothing
                    dialog.dismiss();
                })
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    DatabaseReference reference = database.getReference("articles");
                    reference.child(article.getId()).removeValue().addOnSuccessListener(task -> {
                        articles.remove(position);
                        notifyItemRemoved(position);
                        dialog.dismiss();
                        Log.d("ArticleAdapter", "articles.size(): " + articles.size());
                    });
                }).show();
    }

    private void openDetailArticleActivity(Context context, Article article){
        Intent intent = new Intent(context, DetailArticleActivity.class);
        intent.putExtra("article", article);
        context.startActivity(intent);
    }

    private void increaseView(String articleId, int viewCount){
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("articles");
        reference.child(articleId).child("view").setValue(viewCount + 1);
    }

    @Override
    public int getItemViewType(int position) {
        return curentViewType;
    }

    @Override
    public int getItemCount() {
        if (!articles.isEmpty()){
            return articles.size();
        }
        return 0;
    }

    public class ArticleFullViewHolder extends RecyclerView.ViewHolder {
        ImageView articleImage;
        TextView articleTitle;
        TextView articleRating;
        TextView articleFavorit;
        TextView articleView;
        MaterialCardView itemLayout;
        TextView articleCategory;
        public ArticleFullViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImage = itemView.findViewById(R.id.article_item_image);
            articleTitle = itemView.findViewById(R.id.article_item_title);
            articleRating = itemView.findViewById(R.id.article_item_rating);
            articleFavorit = itemView.findViewById(R.id.article_item_favorite_count);
            articleView = itemView.findViewById(R.id.article_item_view_count);
            itemLayout = itemView.findViewById(R.id.article_item_layout);
            articleCategory = itemView.findViewById(R.id.article_item_category);
        }
    }

    public class ArticleMinimalistViewHolder extends RecyclerView.ViewHolder {
        ImageView articleImage2;
        TextView articleTitle2;
        TextView articleRating2;
        TextView articleFavorit2;
        TextView articleView2;
        TextView articleCategory2;
        MaterialCardView itemLayout2;
        public ArticleMinimalistViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImage2 = itemView.findViewById(R.id.article_item2_image);
            articleTitle2 = itemView.findViewById(R.id.article_item2_title);
            articleRating2 = itemView.findViewById(R.id.article_item2_rating);
            articleFavorit2 = itemView.findViewById(R.id.article_item2_favorite_count);
            articleView2 = itemView.findViewById(R.id.article_item2_view_count);
            itemLayout2 = itemView.findViewById(R.id.article_item2_layout);
            articleCategory2 = itemView.findViewById(R.id.article_item2_category);
        }
    }

    public class ArticleEditViewHolder extends RecyclerView.ViewHolder {
        ImageView articleImage3;
        TextView articleTitle3;
        TextView articleRating3;
        TextView articleFavorit3;
        TextView articleView3;
        MaterialCardView itemLayout3;
        Button editButton, deleteButton;
        TextView articleCategory3;
        public ArticleEditViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImage3 = itemView.findViewById(R.id.article_item3_image);
            articleTitle3 = itemView.findViewById(R.id.article_item3_title);
            articleRating3 = itemView.findViewById(R.id.article_item3_rating);
            articleFavorit3 = itemView.findViewById(R.id.article_item3_favorite_count);
            articleView3 = itemView.findViewById(R.id.article_item3_view_count);
            itemLayout3 = itemView.findViewById(R.id.article_item3_layout);
            articleCategory3 = itemView.findViewById(R.id.article_item3_category);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }



}
