package com.example.cookmaster.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookmaster.R;
import com.example.cookmaster.adapter.helper.OnOptionProfileClickListener;
import com.example.cookmaster.model.Option;

import java.util.List;

public class ProfileOptionAdapter extends RecyclerView.Adapter<ProfileOptionAdapter.ProfileOptionViewHolder> {
    private List<Option> list;
    private int lastPosition;
    private OnOptionProfileClickListener optionListener;

    public static final int EDIT_PROFILE_OPTION = 0;
    public static final int MY_ARTICLES_OPTION = 1;
    public static final int FAVORIT_ARTICLES_OPTION = 2;
    public static final int CHANGE_PASSWORD_OPTION = 3;
    public static final int LOGOUT_OPTION = 4;

    public ProfileOptionAdapter(List<Option> list) {
        this.list = list;
        lastPosition = list.size()-1;
    }

    public void setOptionListener(OnOptionProfileClickListener optionListener) {
        this.optionListener = optionListener;
    }

    @NonNull
    @Override
    public ProfileOptionAdapter.ProfileOptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_option_item, parent, false);
        return new ProfileOptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileOptionAdapter.ProfileOptionViewHolder holder, int position) {
        Option option = list.get(position);
        holder.optionName.setText(option.getOptionName());
        holder.optionIcon.setImageResource(option.getOptionIcon());
        if (position == lastPosition){
            holder.optionName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.errorInput));
            holder.optionArrow.setImageResource(R.drawable.back_red_icon);
        }

        holder.optionLayout.setOnClickListener(v -> {
            optionListener.onClick(position);
        });
    }


    @Override
    public int getItemCount() {
        if (!list.isEmpty()) {
            return list.size();
        }
        return 0;
    }

    public class ProfileOptionViewHolder extends RecyclerView.ViewHolder {
        ImageView optionIcon;
        TextView optionName;

        ImageView optionArrow;
        RelativeLayout optionLayout;
        public ProfileOptionViewHolder(@NonNull View itemView) {
            super(itemView);
            optionIcon = itemView.findViewById(R.id.option_icon);
            optionName = itemView.findViewById(R.id.option_name);
            optionArrow = itemView.findViewById(R.id.option_arrow);
            optionLayout = itemView.findViewById(R.id.option_layout);
        }
    }
}
