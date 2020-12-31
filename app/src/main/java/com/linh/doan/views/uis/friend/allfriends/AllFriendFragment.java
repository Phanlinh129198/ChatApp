package com.linh.doan.views.uis.friend.allfriends;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.linh.doan.BR;
import com.linh.doan.base.BaseFragment;
import com.linh.doan.R;
import com.linh.doan.databinding.FragmentAllFriendsBinding;
import com.linh.doan.services.models.AllUserModel;
import com.linh.doan.views.adapters.AllFriendAdapter;
import com.linh.doan.views.uis.friend.DialogFriendFragment;
import com.linh.doan.views.uis.friend.SharedFriendViewModel;
import com.linh.doan.views.uis.message.ChatFragment;

import java.util.ArrayList;

public class AllFriendFragment extends BaseFragment<FragmentAllFriendsBinding, SharedFriendViewModel> {
    public AllFriendAdapter allFriendAdapter;

    @Override
    public int getBindingVariable() {
        return BR.viewModelAllFriend;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_all_friends;
    }

    @Override
    public SharedFriendViewModel getViewModel() {
        return ViewModelProviders.of(requireActivity()).get(SharedFriendViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SimpleItemAnimator itemAnimator = (SimpleItemAnimator) mViewDataBinding.RecyclerAllFriend.getItemAnimator();
        assert itemAnimator != null;
        itemAnimator.setSupportsChangeAnimations(false);
        allFriendAdapter = new AllFriendAdapter(getContext());
        mViewDataBinding.RecyclerAllFriend.setAdapter(allFriendAdapter);

        //set click vào các user
        allFriendAdapter.setOnItemClickListener(userModel -> {
            if (userModel.getUserType().equals("friend")) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ChatFragment chatFragment = new ChatFragment();
                Bundle bundle = new Bundle();
                bundle.putString("idUser", userModel.getUserId());
                chatFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.frameLayoutChat, chatFragment, null);
                fragmentTransaction.commit();
            } else {
                DialogFriendFragment dialog = new DialogFriendFragment();
                dialog.show(getParentFragmentManager(), null);
            }
        });
//        allFriendAdapter.setOnItemClickListener(new AllFriendAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(AllUserModel userModel, String t) {
//                switch (t) {
//                    case "Kết bạn":
//                        sharedFriendViewModel.createFriend(userModel);
//                        break;
//                    case "Hủy":
//                    case "Bạn bè":
//                        sharedFriendViewModel.deleteFriend(userModel);
//                        break;
//                    case "Đồng ý":
//                        sharedFriendViewModel.updateFriend(userModel);
//                        break;
//                }
//            }
//        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.allUserListLiveData.observe(getViewLifecycleOwner(), allUserModels -> {
            ArrayList<AllUserModel> allUserModelsMid = new ArrayList<>(allUserModels);
            // trường hợp chưa có user nào trong allfriend
            if (allUserModelsMid.isEmpty()) {
                mViewDataBinding.ImageViewNoResultAllFriend.setVisibility(View.VISIBLE);
            } else {
                mViewDataBinding.ImageViewNoResultAllFriend.setVisibility(View.GONE);
            }
            mViewModel.collectionArray(allUserModelsMid);
            allFriendAdapter.submitList(allUserModelsMid);
        });
    }
}
