package com.example.cookmaster.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cookmaster.R;
import com.example.cookmaster.model.Article;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    private List<Article> articles;

    public ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleAdapter.ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleAdapter.ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.articleTitle.setText(article.getTitle());
        holder.articleRating.setText(article.getStar()+"");
        holder.articleFavorit.setText("0");
        holder.articleView.setText(article.getView()+"");
        if(article.getImage() != null){
            Glide.with(holder.itemView.getContext()).load(article.getImage()).into(holder.articleImage);
        }else{
            Glide.with(holder.itemView.getContext()).load(R.drawable.image).into(holder.articleImage);
        }
    }

    @Override
    public int getItemCount() {
        if (!articles.isEmpty()){
            return articles.size();
        }
        return 0;
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView articleImage;
        TextView articleTitle;
        TextView articleRating;
        TextView articleFavorit;
        TextView articleView;
        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImage = itemView.findViewById(R.id.article_item_image);
            articleTitle = itemView.findViewById(R.id.article_item_title);
            articleRating = itemView.findViewById(R.id.article_item_rating);
            articleFavorit = itemView.findViewById(R.id.article_item_favorite_count);
            articleView = itemView.findViewById(R.id.article_item_view_count);
        }
    }
}
