package com.linh.doan.views.uis.message;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.linh.doan.BR;
import com.linh.doan.R;
import com.linh.doan.base.BaseFragment;
import com.linh.doan.databinding.FragmentMessageBinding;
import com.linh.doan.services.models.ChatModel;
import com.linh.doan.views.adapters.MessagesAdapter;
import com.linh.doan.views.uis.message.ChatFragment;
import com.linh.doan.views.uis.message.ChatViewModel;

import java.util.ArrayList;

public class MessageFragment extends BaseFragment<FragmentMessageBinding, com.linh.doan.views.uis.message.ChatViewModel> {
    ArrayList<ChatModel> arraySearch = new ArrayList<>();
    ArrayList<ChatModel> messageArrayList = new ArrayList<>();
    MessagesAdapter messagesAdapter;

    @Override
    public int getBindingVariable() {
        return BR.messageViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public com.linh.doan.views.uis.message.ChatViewModel getViewModel() {
        return ViewModelProviders.of(requireActivity()).get(ChatViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        SimpleItemAnimator itemAnimator = (SimpleItemAnimator) mViewDataBinding.recyclerMessage.getItemAnimator();
        assert itemAnimator != null;
        itemAnimator.setSupportsChangeAnimations(false);
        messagesAdapter = new MessagesAdapter(getContext(), messageArrayList);
        mViewDataBinding.recyclerMessage.setAdapter(messagesAdapter);
        messagesAdapter.setOnItemClickListener(userModel -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            Fragment chatFragment = getParentFragmentManager().findFragmentByTag("chat");
            if (chatFragment == null) {
                chatFragment = new ChatFragment();
                fragmentTransaction.add(R.id.frameLayoutChat, chatFragment, "chat");
            } else {
                fragmentTransaction.replace(R.id.frameLayoutChat, chatFragment, "chat");
            }
            Bundle bundle = new Bundle();
            bundle.putString("idUser", userModel.getUserModel().getUserId());
            chatFragment.setArguments(bundle);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            mViewDataBinding.editTextSearchUserChat.setText(null);
        });
        mViewDataBinding.imageButtonDelete.setOnClickListener(view1 -> {
            mViewDataBinding.editTextSearchUserChat.setText(null);
            mViewDataBinding.imageButtonDelete.setVisibility(View.GONE);
        });
        mViewDataBinding.editTextSearchUserChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mViewModel.searchUserChat(charSequence.toString(), arraySearch);
                if (TextUtils.isEmpty(charSequence.toString())) {
                    mViewDataBinding.imageButtonDelete.setVisibility(View.GONE);
                } else {
                    mViewDataBinding.imageButtonDelete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.arrayInfoUserChatLiveData.observe(getViewLifecycleOwner(), chatModels -> {
            messageArrayList.clear();
            if (chatModels.isEmpty()) {
                mViewDataBinding.ImageViewNoResultChat.setVisibility(View.VISIBLE);
            } else {
                mViewModel.softArray(chatModels);
                int count = mViewModel.getCountUnReadMessage(chatModels);
                if (count > 9) {
                    mViewModel.countUnReadMessage.setValue("9+");
                } else {
                    mViewModel.countUnReadMessage.setValue(count + "");
                }
                mViewDataBinding.ImageViewNoResultChat.setVisibility(View.GONE);
            }
            if (messageArrayList.isEmpty()) {
                messageArrayList.addAll(chatModels);
            }
            messagesAdapter.notifyDataSetChanged();
        });
        mViewModel.arraySearchLiveData.observe(getViewLifecycleOwner(), chatModels -> arraySearch = chatModels);
    }
}