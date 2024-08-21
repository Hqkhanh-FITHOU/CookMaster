package com.example.cookmaster.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookmaster.R;
import com.example.cookmaster.model.Slider;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private List<Slider> sliderList;

    public SliderAdapter() {
        sliderList = new ArrayList<>();
        sliderList.add(new Slider(R.drawable.slider_img1, "Khai phá",""));
        sliderList.add(new Slider(R.drawable.slider_img2, "Thỏa mãn",""));
        sliderList.add(new Slider(R.drawable.slider_img3, "Đa dạng",""));
        sliderList.add(new Slider(R.drawable.slider_img4, "Lan tỏa",""));
    }


    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.SliderViewHolder holder, int position) {
        holder.sliderImage.setImageResource(sliderList.get(position).getResId());
        holder.sliderTitle.setText(sliderList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if (sliderList.isEmpty()){
            return 0;
        }
        return sliderList.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView sliderImage;
        TextView sliderTitle;
        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            sliderImage = itemView.findViewById(R.id.carousel_image_view);
            sliderTitle = itemView.findViewById(R.id.carousel_item_title);
        }
    }
}
