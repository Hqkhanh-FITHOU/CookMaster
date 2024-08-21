package com.example.cookmaster.subscreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookmaster.R;


public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View searchScreen = inflater.inflate(R.layout.fragment_search, container, false);
        // Inflate the layout for this fragment
        return searchScreen;
    }
}