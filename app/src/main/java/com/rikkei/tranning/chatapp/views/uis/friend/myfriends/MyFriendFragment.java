package com.rikkei.tranning.chatapp.views.uis.friend.myfriends;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.rikkei.tranning.chatapp.BR;
import com.rikkei.tranning.chatapp.base.BaseFragment;
import com.rikkei.tranning.chatapp.R;
import com.rikkei.tranning.chatapp.databinding.FragmentMyFriendsBinding;
import com.rikkei.tranning.chatapp.services.models.AllUserModel;
import com.rikkei.tranning.chatapp.views.adapters.MyFriendAdapter;
import com.rikkei.tranning.chatapp.views.uis.friend.SharedFriendViewModel;
import com.rikkei.tranning.chatapp.views.uis.message.ChatFragment;

import java.util.ArrayList;

public class MyFriendFragment extends BaseFragment<FragmentMyFriendsBinding, SharedFriendViewModel> {
    MyFriendAdapter myFriendAdapter;

    @Override
    public int getBindingVariable() {
        return BR.viewModelMyFriend;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_friends;
    }

    @Override
    public SharedFriendViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(SharedFriendViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myFriendAdapter = new MyFriendAdapter(getContext());
        mViewDataBinding.RecyclerMyFriend.setAdapter(myFriendAdapter);
        //xử lý khi click vào 1 friend bất kì
        myFriendAdapter.setOnItemClickListener(userModel -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            Fragment chatFragment = getParentFragmentManager().findFragmentByTag("fragmentChat");
            if (chatFragment == null) {//trường hợp chưa từng nhắn tin cho nhau
                chatFragment = new ChatFragment();
                fragmentTransaction.add(R.id.frameLayoutChat, chatFragment, "fragmentChat");
            } else {//trường hợp đã có tin nhắn trc đó
                fragmentTransaction.replace(R.id.frameLayoutChat, chatFragment, "fragmentChat");
            }
            Bundle bundle = new Bundle();
            bundle.putString("idUser", userModel.getUserId());
            chatFragment.setArguments(bundle);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    //tìm hiểu sau
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //thêm user vào danh sách bạn
        mViewModel.allUserListLiveData.observe(getViewLifecycleOwner(), allUserModels -> {
            ArrayList<AllUserModel> allUserModelsMid = new ArrayList<>();
            for (int i = 0; i < allUserModels.size(); i++) {
                if (allUserModels.get(i).getUserType().equals("friend")) {
                    allUserModelsMid.add(allUserModels.get(i));
                }
            }
            if (allUserModelsMid.isEmpty()) {
                mViewDataBinding.ImageViewNoResultMyFriend.setVisibility(View.VISIBLE);
            } else {
                mViewDataBinding.ImageViewNoResultMyFriend.setVisibility(View.GONE);
            }
            mViewModel.collectionArray(allUserModelsMid);
            myFriendAdapter.submitList(allUserModelsMid);
        });
    }
}
