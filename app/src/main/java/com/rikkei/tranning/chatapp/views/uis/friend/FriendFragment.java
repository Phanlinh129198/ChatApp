package com.rikkei.tranning.chatapp.views.uis.friend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;
import com.rikkei.tranning.chatapp.BR;
import com.rikkei.tranning.chatapp.base.BaseFragment;
import com.rikkei.tranning.chatapp.R;
import com.rikkei.tranning.chatapp.databinding.FragmentFriendBinding;
import com.rikkei.tranning.chatapp.services.models.AllUserModel;
import com.rikkei.tranning.chatapp.views.adapters.MainViewPaperAdaper;
import com.rikkei.tranning.chatapp.views.uis.friend.allfriends.AllFriendFragment;
import com.rikkei.tranning.chatapp.views.uis.friend.myfriends.MyFriendFragment;
import com.rikkei.tranning.chatapp.views.uis.friend.requestfriends.RequestFriendFragment;

import java.util.ArrayList;
import java.util.Objects;

public class FriendFragment extends BaseFragment<FragmentFriendBinding, SharedFriendViewModel> {
    ArrayList<AllUserModel> allUserArrayList = new ArrayList<>();
    TabLayout.Tab tabRequest;
    TabLayout.Tab tabFriend;
    TabLayout.Tab tabAll;
    TextView countNotifi;//biến lấy số lượng request
    View textRequest;

    @Override
    public int getBindingVariable() {
        return BR.viewModelFriend;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_friend;
    }

    @Override
    public SharedFriendViewModel getViewModel() {
        return ViewModelProviders.of(requireActivity()).get(SharedFriendViewModel.class);
    }

    @SuppressLint("InflateParams")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        MainViewPaperAdaper mainViewPaperAdaper = new MainViewPaperAdaper(getParentFragmentManager());
        mainViewPaperAdaper.AddFragment(new MyFriendFragment(), String.valueOf(R.string.txt_title_friend));
        mainViewPaperAdaper.AddFragment(new AllFriendFragment(), String.valueOf(R.string.txt_all_friend_title));
        mainViewPaperAdaper.AddFragment(new RequestFriendFragment(), String.valueOf(R.string.txt_request_friend_title));
        mViewDataBinding.viewPagerFriend.setAdapter(mainViewPaperAdaper);
        mViewDataBinding.tabLayoutFriend.setupWithViewPager(mViewDataBinding.viewPagerFriend);
        mViewDataBinding.imageButtonDeleteSearchFriend.setOnClickListener(view1 -> {
            mViewDataBinding.editTextSearchFriend.setText(null);
            mViewDataBinding.imageButtonDeleteSearchFriend.setVisibility(View.GONE);
        });
        mViewDataBinding.editTextSearchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.searchFriend(s.toString(), allUserArrayList);
                if (TextUtils.isEmpty(s.toString())) {
                    mViewDataBinding.imageButtonDeleteSearchFriend.setVisibility(View.GONE);
                } else {
                    mViewDataBinding.imageButtonDeleteSearchFriend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // chia 3 tab

        //friend
        View textFriend = LayoutInflater.from(getContext()).inflate(R.layout.custom_tablayout, null);
        TextView textNotifiFriend = textFriend.findViewById(R.id.text1);
        textNotifiFriend.setText(R.string.txt_title_friend);
        tabFriend = mViewDataBinding.tabLayoutFriend.getTabAt(0);
        assert tabFriend != null;
        tabFriend.setCustomView(textFriend);

        // all friend
        View textAll = LayoutInflater.from(getContext()).inflate(R.layout.custom_tablayout, null);
        TextView textNotifiAll = textAll.findViewById(R.id.text1);
        textNotifiAll.setText(R.string.txt_all_friend_title);
        tabAll = mViewDataBinding.tabLayoutFriend.getTabAt(1);
        assert tabAll != null;
        tabAll.setCustomView(textAll);

        //request
        textRequest = LayoutInflater.from(getContext()).inflate(R.layout.custom_tablayout, null);
        TextView textNotifiRequest = textRequest.findViewById(R.id.text1);
        countNotifi = textNotifiFriend.findViewById(R.id.textNotifi);
        textNotifiRequest.setText(R.string.txt_request_friend_title);
        tabRequest = mViewDataBinding.tabLayoutFriend.getTabAt(2);
        assert tabRequest != null;
        tabRequest.setCustomView(textRequest);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getUserFromLiveData.observe(getViewLifecycleOwner(), allUserModelArrayList -> allUserArrayList = allUserModelArrayList);

        mViewModel.countNotifiRequest.observe(getViewLifecycleOwner(), s -> {
            View viewRequest = Objects.requireNonNull(mViewDataBinding.tabLayoutFriend.getTabAt(2)).getCustomView();

            assert viewRequest != null;
            TextView count = viewRequest.findViewById(R.id.textNotifi);


            if (s.equals("0"))
                count.setVisibility(View.GONE);
            else {
                count.setVisibility(View.VISIBLE);
                count.setText(s);

            }
        });
    }
}
