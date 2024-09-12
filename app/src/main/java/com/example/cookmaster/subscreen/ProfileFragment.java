package com.example.cookmaster.subscreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.cookmaster.R;
import com.example.cookmaster.adapter.ProfileOptionAdapter;
import com.example.cookmaster.adapter.helper.OnOptionProfileClickListener;
import com.example.cookmaster.article.FavoritArticleActivity;
import com.example.cookmaster.article.MyArticleActivity;
import com.example.cookmaster.authenticate.ChangePasswordActivity;
import com.example.cookmaster.authenticate.EditProfileActivity;
import com.example.cookmaster.authenticate.LoginActivity;
import com.example.cookmaster.authenticate.SessionManager;
import com.example.cookmaster.databinding.FragmentProfileBinding;
import com.example.cookmaster.model.Option;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private ProfileOptionAdapter optionAdapter;
    private List<Option> optionList;
    private FirebaseUser user;
    private ActivityResultLauncher launcher;

    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View profileScreen = binding.getRoot();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            binding.username.setText(user.getDisplayName());
            binding.email.setText(user.getEmail());
            Glide.with(this).load(user.getPhotoUrl()).into(binding.profileImage);
        }


        optionList = new ArrayList<>();
        setOptionListContentView();
        optionAdapter = new ProfileOptionAdapter(optionList);

        binding.listOption.setAdapter(optionAdapter);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == EditProfileActivity.EDIT_PROFILE){
                //reload data after edit profile
                binding.username.setText(user.getDisplayName());
                Glide.with(this).load(user.getPhotoUrl()).into(binding.profileImage);
            }
        });
        // Inflate the layout for this fragment
        onClickProfileOption();
        return profileScreen;
    }

    private void onClickProfileOption() {

        optionAdapter.setOptionListener(option -> {
            switch (option) {
                case ProfileOptionAdapter.EDIT_PROFILE_OPTION:{
                    Intent intent = new Intent(getContext(), EditProfileActivity.class);
                    launcher.launch(intent);
                    break;
                }
                case ProfileOptionAdapter.MY_ARTICLES_OPTION:{
                    Intent intent = new Intent(getContext(), MyArticleActivity.class);
                    launcher.launch(intent);
                    break;
                }
                case ProfileOptionAdapter.FAVORIT_ARTICLES_OPTION:{
                    Intent intent = new Intent(getContext(), FavoritArticleActivity.class);
                    startActivity(intent);
                    break;
                }
                case ProfileOptionAdapter.CHANGE_PASSWORD_OPTION:{
                    Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                    startActivity(intent);
                    break;
                }
                case ProfileOptionAdapter.LOGOUT_OPTION:{
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    FirebaseAuth.getInstance().signOut();
                    new SessionManager(getActivity()).setLoginState(false);
                    getActivity().finish();
                    break;
                }
            }
        });
    }

    private void setOptionListContentView() {
        optionList.add(new Option(R.drawable.edit_profile_icon, "Chỉnh sửa hồ sơ"));
        optionList.add(new Option(R.drawable.article_icon, "Bài viết của tôi"));
        optionList.add(new Option(R.drawable.favorite_icon, "Bài viết yêu thích"));
        optionList.add(new Option(R.drawable.password_icon, "Đổi mật khẩu"));
        optionList.add(new Option(R.drawable.logout_red_icon, "Đăng xuất"));
    }


}