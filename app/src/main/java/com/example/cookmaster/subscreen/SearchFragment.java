package com.example.cookmaster.subscreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookmaster.R;
import com.example.cookmaster.adapter.ArticleAdapter;
import com.example.cookmaster.adapter.RecentSearchAdapter;
import com.example.cookmaster.databinding.FragmentSearchBinding;
import com.example.cookmaster.model.Article;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;

    private List<Article> articles;
    private ArticleAdapter adapter;
    private List<String> recentSearch;

    private FirebaseDatabase database;

    private RecentSearchAdapter recentSearchAdapter;

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
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View searchScreen = binding.getRoot();
        database = FirebaseDatabase.getInstance();
        // Inflate the layout for this fragment
        recentSearch = new ArrayList<>();
        fetchRecentSearch();
        recentSearchAdapter = new RecentSearchAdapter(recentSearch);
        recentSearchAdapter.setOnRecentSearchItemClickListener(position -> onSelectRecentSearch(position));
        binding.recentList.setAdapter(recentSearchAdapter);




        articles = new ArrayList<>();
        fetchArticles();
        adapter = new ArticleAdapter(articles, false);
        binding.listArticle.setAdapter(adapter);

        binding.chipGroup.setOnCheckedStateChangeListener((chipGroup, list) -> onChipStatusChanged(list, searchScreen));


        binding.searchView.getEditText().setOnEditorActionListener((v, actionId, event) -> {

            binding.searchBar.setText(binding.searchView.getText());
            binding.searchView.hide();

            fetchArticlesWithQuery(binding.searchView.getText().toString().trim());

            return true;
        });

        binding.searchView.getToolbar().setNavigationOnClickListener(v -> {
            binding.searchBar.setText(binding.searchView.getText());
            binding.searchView.hide();
        });

        return searchScreen;
    }

    private void onSelectRecentSearch(int position) {
        binding.searchBar.setText(recentSearch.get(position));
        binding.searchView.hide();
        fetchArticlesWithQuery(recentSearch.get(position).trim());
    }

    private void fetchRecentSearch(){
        recentSearch.add("Món ăn Việt Nam");
        recentSearch.add("Món kho");
        recentSearch.add("Thịt chua ngọt");
        recentSearch.add("Khâu nhục tứ xuyên");
        recentSearch.add("Gà hầm thuốc bắc");
        recentSearch.add("Món xào");
    }

    private void onChipStatusChanged(List<Integer> listId, View view) {
        articles.clear();
        binding.loading.setVisibility(View.VISIBLE);
        binding.noResultText.setVisibility(View.GONE);
        binding.listArticle.setVisibility(View.GONE);

        if (listId.isEmpty()) {
            fetchArticles();
            adapter.notifyDataSetChanged();
            binding.loading.setVisibility(View.GONE);
            binding.noResultText.setVisibility(View.VISIBLE);
            binding.listArticle.setVisibility(View.GONE);
        }else {
            for (Integer id : listId) {
                String categoryName =  ((Chip)view.findViewById(id)).getText().toString().trim();
                fetchArticles(categoryName);
            }
            adapter.setArticles(articles);
            adapter.notifyDataSetChanged();
        }
        if(articles.isEmpty()){
            binding.loading.setVisibility(View.GONE);
            binding.noResultText.setVisibility(View.VISIBLE);
            binding.listArticle.setVisibility(View.GONE);
        }
    }

    private void fetchArticles(){
        binding.loading.setVisibility(View.VISIBLE);
        binding.noResultText.setVisibility(View.GONE);
        binding.listArticle.setVisibility(View.GONE);
        DatabaseReference reference = database.getReference("articles");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() > 0){
                    for(DataSnapshot snap : snapshot.getChildren()){
                        Article article = snap.getValue(Article.class);
                        if (article.isPublished()){
                            articles.add(article);
                        }
                    }
                    binding.loading.setVisibility(View.GONE);
                    binding.noResultText.setVisibility(View.GONE);
                    binding.listArticle.setVisibility(View.VISIBLE);
                }else {
                    binding.loading.setVisibility(View.GONE);
                    binding.noResultText.setVisibility(View.VISIBLE);
                    binding.listArticle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchArticles(String categoryName) {
        DatabaseReference reference = database.getReference("articles");
        if(!categoryName.isEmpty()){
            Query query = reference.orderByChild("category").equalTo(categoryName);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getChildrenCount() > 0){
                        for(DataSnapshot snap : snapshot.getChildren()){
                            Article article = snap.getValue(Article.class);
                            if (article.isPublished()){
                                articles.add(article);
                            }
                        }
                        binding.loading.setVisibility(View.GONE);
                        binding.noResultText.setVisibility(View.GONE);
                        binding.listArticle.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void fetchArticlesWithQuery(String searchQuery){
        //handles searching logic here
        //including logic of adding recent search
    }


}