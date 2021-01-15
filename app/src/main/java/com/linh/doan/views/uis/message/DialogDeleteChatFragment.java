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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.linh.doan.R;
import com.linh.doan.services.models.MessageModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DialogDeleteChatFragment extends DialogFragment {
    Button btnDelete, btnNo;
    private MessageModel messageModel;
    private String idFriend;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public DialogDeleteChatFragment(MessageModel messageModel, String idFriend) {
        this.messageModel = messageModel;
        this.idFriend = idFriend;
    }

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
        btnDelete = view.findViewById(R.id.buttonDialogDelete);
        btnNo = view.findViewById(R.id.buttonDialogNo);
        btnNo.setOnClickListener(view1 -> Objects.requireNonNull(getDialog()).dismiss());
        btnDelete.setOnClickListener(v -> delete());
    }

    public void delete() {
        String myId = firebaseUser.getUid();
        String key;
        if (idFriend.compareTo(myId) > 0) {
            key = idFriend + myId;
        } else {
            key = myId + idFriend;
        }

        String delete = messageModel.getDelete();
        delete = delete.replaceAll(myId, "");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("chat").child(key).child(messageModel.getIdKeyNode());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("delete", delete);

        databaseReference.updateChildren(childUpdates);

        Objects.requireNonNull(getDialog()).dismiss();
    }
}
