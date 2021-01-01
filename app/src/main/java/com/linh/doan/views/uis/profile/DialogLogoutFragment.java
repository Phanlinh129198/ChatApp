package com.linh.doan.views.uis.profile;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.linh.doan.R;
import com.linh.doan.views.uis.SplashActivity;
import com.linh.doan.views.uis.friend.SharedFriendViewModel;

import java.util.Objects;

public class DialogLogoutFragment extends DialogFragment {
    Button btnLogout, btnNo;
    private SharedFriendViewModel chatViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_logout, container, false);
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
                ViewModelProviders.of(this).get(SharedFriendViewModel.class);
        btnLogout = view.findViewById(R.id.buttonDialogLogout);
        btnNo = view.findViewById(R.id.buttonDialogNo);
        btnNo.setOnClickListener(view1 -> Objects.requireNonNull(getDialog()).dismiss());
        btnLogout.setOnClickListener(v -> logout());
    }

    public void logout() {
        chatViewModel.updateStatus("status", "offline");
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        requireActivity().finish();

    }
}
