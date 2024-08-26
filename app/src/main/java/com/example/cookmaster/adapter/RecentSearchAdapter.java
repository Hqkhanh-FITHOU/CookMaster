package com.example.cookmaster.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookmaster.R;

import java.util.List;

public class RecentSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> list;

    private OnRecentSearchItemClickListener listener;

    public RecentSearchAdapter(List<String> list) {
        this.list = list;
    }

    public void setOnRecentSearchItemClickListener(OnRecentSearchItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggest_search_item, parent, false);
        return new RecentSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecentSearchViewHolder viewHolder = (RecentSearchViewHolder) holder;
        String recentString = list.get(position);
        viewHolder.recentString.setText(recentString);

        viewHolder.layout.setOnClickListener(v -> {
            listener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        if (list.isEmpty()){
            return 0;
        }
        return list.size();
    }

    public class RecentSearchViewHolder extends RecyclerView.ViewHolder {
        TextView recentString;
        ConstraintLayout layout;
        public RecentSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            recentString = itemView.findViewById(R.id.recent_text);
            layout = itemView.findViewById(R.id.recent_layout);
        }
    }

    public interface OnRecentSearchItemClickListener {
        void onItemClick(int position);
    }
}
