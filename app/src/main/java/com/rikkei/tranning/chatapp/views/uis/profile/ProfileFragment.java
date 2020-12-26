package com.rikkei.tranning.chatapp.views.uis.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.rikkei.tranning.chatapp.BR;
import com.rikkei.tranning.chatapp.base.BaseFragment;
import com.rikkei.tranning.chatapp.R;
import com.rikkei.tranning.chatapp.databinding.FragmentProfileBinding;
import com.rikkei.tranning.chatapp.helper.LocaleHelper;
import com.rikkei.tranning.chatapp.views.uis.message.ZoomImageFragment;

import java.util.Locale;


public class ProfileFragment extends BaseFragment<FragmentProfileBinding, ProfileViewModel> {
    Locale myLocale;
    String image;

    @Override
    public int getBindingVariable() {
        return BR.viewModelProfile;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public ProfileViewModel getViewModel() {
        return ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewDataBinding.TextViewLanguage.setText(Locale.getDefault().getDisplayName());
        mViewDataBinding.ImageButtonEditProfile.setOnClickListener(v -> replaceFragment());
        mViewDataBinding.RelativeLayoutLogout.setOnClickListener(view1 -> {
            DialogLogoutFragment dialog = new DialogLogoutFragment();
            dialog.show(getParentFragmentManager(), null);
        });
        mViewDataBinding.imageButtonChangeLanguage.setOnClickListener(v -> showMenu());
        mViewDataBinding.TextViewLanguage.setOnClickListener(v -> showMenu());
        mViewDataBinding.CircleImageViewUser.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ZoomImageFragment zoomImageFragment = new ZoomImageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("image", image);
            zoomImageFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.frameLayoutChat, zoomImageFragment, null).commit();
            fragmentTransaction.addToBackStack(null);
        });
        mViewDataBinding.ImageViewImageUser.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ZoomImageFragment zoomImageFragment = new ZoomImageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("image", image);
            zoomImageFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.frameLayoutChat, zoomImageFragment, null).commit();
            fragmentTransaction.addToBackStack(null);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getInfoUser();
        mViewModel.userMutableLiveData.observe(getViewLifecycleOwner(), user -> {
            mViewDataBinding.TextViewNameUser.setText(user.getUserName());
            mViewDataBinding.TextViewEmailUser.setText(user.getUserEmail());
            image = user.getUserImgUrl();
            if (user.getUserImgUrl().equals("default")) {
                mViewDataBinding.ImageViewImageUser.setImageResource(R.mipmap.ic_launcher);
                mViewDataBinding.CircleImageViewUser.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(requireContext()).load(user.getUserImgUrl()).into(mViewDataBinding.ImageViewImageUser);
                Glide.with(requireContext()).load(user.getUserImgUrl()).circleCrop().into(mViewDataBinding.CircleImageViewUser);
            }
        });
    }

    public void replaceFragment() {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Fragment fragmentEditProfile = getParentFragmentManager().findFragmentByTag("fragmentEditProfile");
        if (fragmentEditProfile == null) {
            fragmentEditProfile = new EditProfileFragment();
            fragmentTransaction.add(R.id.frameLayoutChat, fragmentEditProfile, "fragmentEditProfile");
        } else {
            fragmentTransaction.replace(R.id.frameLayoutChat, fragmentEditProfile, "fragmentEditProfile");
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), mViewDataBinding.imageButtonChangeLanguage);

        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_language, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.language_en:
                    myLocale = new Locale("en", "US");
                    if (!mViewDataBinding.TextViewLanguage.getText().equals("English")) {
                        onChangeLanguage("en");
                        mViewDataBinding.TextViewLanguage.setText(R.string.txt_language_en);
                    }
                    break;
                case R.id.language_vi:
                    myLocale = new Locale("vi", "VN");
                    if (!mViewDataBinding.TextViewLanguage.getText().equals("Tiếng Việt")) {
                        onChangeLanguage("vi");
                        mViewDataBinding.TextViewLanguage.setText(R.string.txt_language_vi);
                    }

                    break;
            }
            return false;
        });

        popupMenu.show();
    }

    public void onChangeLanguage(String language) {
        LocaleHelper.onReAttach(getContext(), language);
        requireActivity().finish();
        Intent intent = requireActivity().getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.nope, R.anim.nope);
    }
}
