package com.linh.doan.views.uis.message;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.linh.doan.R;

import java.util.Objects;

public class DialogDeleteChatFragment extends DialogFragment {
    Button btnDelete, btnNo;
    private ChatViewModel chatViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_delete_item_chat, container, false);
        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatViewModel =
                ViewModelProviders.of(this).get(ChatViewModel.class);
        btnDelete = view.findViewById(R.id.buttonDialogDelete);
        btnNo = view.findViewById(R.id.buttonDialogNo);
        btnNo.setOnClickListener(view1 -> Objects.requireNonNull(getDialog()).dismiss());
        btnDelete.setOnClickListener(v -> delete());
    }

    public void delete() {
chatViewModel.sendMessage();
    }
}
