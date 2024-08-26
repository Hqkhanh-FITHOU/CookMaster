package com.example.cookmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cookmaster.R;
import com.example.cookmaster.article.DetailArticleActivity;
import com.example.cookmaster.model.Article;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int FULL_VIEW = 1;
    private static final int MINIMALIST_VIEW = 2;
    private List<Article> articles;
    private boolean isFullView;

    public ArticleAdapter(List<Article> articles, boolean fullView) {
        this.articles = articles;
        this.isFullView = fullView;
    }

    public void setArticles(List<Article> articles){
        this.articles = articles;
    }

    public void setFullView(boolean fullView){
        this.isFullView = fullView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == FULL_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
            return new ArticleFullViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item2, parent, false);
        return new ArticleMinimalistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Article article = articles.get(position);
        if (isFullView) {
            ArticleFullViewHolder articleViewHolder = (ArticleFullViewHolder) holder;
            articleViewHolder.articleTitle.setText(article.getTitle());
            articleViewHolder.articleRating.setText(article.getStar()+"");
            articleViewHolder.articleFavorit.setText("0");
            articleViewHolder.articleView.setText(article.getView()+"");
            if(article.getImage() != null){
                Glide.with(holder.itemView.getContext()).load(article.getImage()).into(articleViewHolder.articleImage);
            }else{
                Glide.with(holder.itemView.getContext()).load(R.drawable.image).into(articleViewHolder.articleImage);
            }
            articleViewHolder.itemLayout.setOnClickListener(v -> { openDetailArticleActivity(articleViewHolder.itemView.getContext(), article); });
        }else{
            ArticleMinimalistViewHolder articleViewHolder = (ArticleMinimalistViewHolder) holder;
            articleViewHolder.articleTitle2.setText(article.getTitle());
            articleViewHolder.articleRating2.setText(article.getStar()+"");
            articleViewHolder.articleFavorit2.setText("0");
            articleViewHolder.articleView2.setText(article.getView()+"");
            if(article.getImage() != null){
                Glide.with(holder.itemView.getContext()).load(article.getImage()).into(articleViewHolder.articleImage2);
            }else{
                Glide.with(holder.itemView.getContext()).load(R.drawable.image).into(articleViewHolder.articleImage2);
            }
            articleViewHolder.itemLayout2.setOnClickListener(v -> { openDetailArticleActivity(articleViewHolder.itemView.getContext(), article); });
        }
    }

    private void openDetailArticleActivity(Context context, Article article){
        Intent intent = new Intent(context, DetailArticleActivity.class);
        intent.putExtra("article", article);
        context.startActivity(intent);
    }

    @Override
    public int getItemViewType(int position) {
        if (isFullView) {
            return FULL_VIEW;
        }
        return MINIMALIST_VIEW;
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
        public ArticleFullViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImage = itemView.findViewById(R.id.article_item_image);
            articleTitle = itemView.findViewById(R.id.article_item_title);
            articleRating = itemView.findViewById(R.id.article_item_rating);
            articleFavorit = itemView.findViewById(R.id.article_item_favorite_count);
            articleView = itemView.findViewById(R.id.article_item_view_count);
            itemLayout = itemView.findViewById(R.id.article_item_layout);
        }
    }

    public class ArticleMinimalistViewHolder extends RecyclerView.ViewHolder {
        ImageView articleImage2;
        TextView articleTitle2;
        TextView articleRating2;
        TextView articleFavorit2;
        TextView articleView2;
        MaterialCardView itemLayout2;
        public ArticleMinimalistViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImage2 = itemView.findViewById(R.id.article_item2_image);
            articleTitle2 = itemView.findViewById(R.id.article_item2_title);
            articleRating2 = itemView.findViewById(R.id.article_item2_rating);
            articleFavorit2 = itemView.findViewById(R.id.article_item2_favorite_count);
            articleView2 = itemView.findViewById(R.id.article_item2_view_count);
            itemLayout2 = itemView.findViewById(R.id.article_item2_layout);
        }
    }
}
