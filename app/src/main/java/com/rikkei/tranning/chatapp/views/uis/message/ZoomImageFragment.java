package com.rikkei.tranning.chatapp.views.uis.message;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.rikkei.tranning.chatapp.R;

public class ZoomImageFragment extends Fragment {
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView imageView;
    ImageButton imgBack;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zom_image, container, false);
        imageView = view.findViewById(R.id.imageView_zoom);
        imgBack = view.findViewById(R.id.imageButtonBackImageZoom);
        Bundle bundle = getArguments();
        String image = null;
        if (bundle != null) {
            image = bundle.getString("image");
        }
        assert image != null;
        if (image.equals("default")) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(requireContext()).load(image).into(imageView);
        }
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        view.setOnTouchListener((v, event) -> {
            scaleGestureDetector.onTouchEvent(event);
            return true;
        });
        imgBack.setOnClickListener(v -> removeFragment());
        return view;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }
    }

    public void removeFragment() {
        Fragment fragment = getParentFragmentManager().findFragmentById(R.id.frameLayoutChat);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        assert fragment != null;
        fragmentTransaction.remove(fragment);
        getParentFragmentManager().popBackStack();
        fragmentTransaction.commit();
    }
}
