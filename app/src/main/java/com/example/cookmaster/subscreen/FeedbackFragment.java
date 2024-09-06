package com.example.cookmaster.subscreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookmaster.R;
import com.example.cookmaster.databinding.FragmentFeedbackBinding;


public class FeedbackFragment extends Fragment {

    private FragmentFeedbackBinding binding;

    public FeedbackFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFeedbackBinding.inflate(inflater, container, false);
        View feedbackCreen = binding.getRoot();
        // Inflate the layout for this fragment
        return feedbackCreen;
    }
}