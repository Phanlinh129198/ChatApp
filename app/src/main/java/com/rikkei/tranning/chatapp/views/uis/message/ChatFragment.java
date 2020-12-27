package com.rikkei.tranning.chatapp.views.uis.message;

import android.content.pm.PackageManager;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rikkei.tranning.chatapp.BR;
import com.rikkei.tranning.chatapp.R;
import com.rikkei.tranning.chatapp.base.BaseFragment;
import com.rikkei.tranning.chatapp.databinding.FragmentChatBinding;
import com.rikkei.tranning.chatapp.services.models.MessageModel;
import com.rikkei.tranning.chatapp.views.adapters.ChatAdapter;
import com.rikkei.tranning.chatapp.views.adapters.ImageAdapter;
import com.rikkei.tranning.chatapp.views.adapters.StickerAdapter;
import com.rikkei.tranning.chatapp.views.uis.ViewModelProviderFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ChatFragment extends BaseFragment<FragmentChatBinding, ChatViewModel> {
    private Integer[] stickerResource = {
            R.drawable.cuppy_hi,
            R.drawable.cuppy_battery,
            R.drawable.cuppy_bluescreen,
            R.drawable.cuppy_bye,
            R.drawable.cuppy_curious,
            R.drawable.cuppy_disgusting,
            R.drawable.cuppy_cry,
            R.drawable.cuppy_hmm,
            R.drawable.cuppy_love,
            R.drawable.cuppy_lovewithcookie,
            R.drawable.cuppy_phone,
            R.drawable.cuppy_angry,
            R.drawable.cuppy_angry1,
            R.drawable.cuppy_lol,
            R.drawable.cuppy_rofl,
            R.drawable.cuppy_tired,
            R.drawable.cuppy_upset

    };
    ChatAdapter chatAdapter;
    ImageAdapter imageAdapter;
    StickerAdapter stickerAdapter;
    String id;
    long lastPositionChat = 0;

    private static final int IMAGE_REQUEST = 1;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("chat");
    private Uri imageUri;
    String uriImage;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ValueEventListener listener;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;
    int loadPosition = 0;
    InputMethodManager inputMethodManager;
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

    @Override
    public int getBindingVariable() {
        return BR.viewModelChat;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    public ChatViewModel getViewModel() {
//        return ViewModelProviders.of(requireActivity()).get(ChatViewModel.class);
        return ViewModelProviders.of(this,new ViewModelProviderFactory()).get(ChatViewModel.class);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // mViewDataBinding.editTextMessage.requestFocus();
        inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//        if (mViewDataBinding.editTextMessage.requestFocus()) {
//            inputMethodManager.showSoftInput(mViewDataBinding.editTextMessage, InputMethodManager.SHOW_IMPLICIT);
//        }
    // nút back
        mViewDataBinding.ImageButtonBackChat.setOnClickListener(v -> removeFragment());
    //nút gửi thêm options
        mViewDataBinding.imageButtonPhotoChat.setOnClickListener(view12 -> buildRecyclerImage());
    //nút gửi
        mViewDataBinding.imageButtonSend.setOnClickListener(v -> {
            Bundle bundle = getArguments();
            String iD = null;
            if (bundle != null) {
                iD = bundle.getString("idUser");
            }
            String message = mViewDataBinding.editTextMessage.getText().toString().trim();
            mViewModel.sendMessage(iD, message, "Text");
            mViewDataBinding.editTextMessage.setText("");
        });
        //focus vào textbox
//        mViewDataBinding.editTextMessage.setOnFocusChangeListener((v, hasFocus) -> {
//            if (mViewDataBinding.recyclerSticker.getVisibility() == View.VISIBLE || mViewDataBinding.recyclerImage.getVisibility() == View.VISIBLE) {
//                mViewDataBinding.recyclerSticker.setVisibility(View.GONE);
//                mViewDataBinding.recyclerImage.setVisibility(View.GONE);
//                mViewDataBinding.imageSendSticker.setImageResource(R.drawable.ic_smile_1);
//                mViewDataBinding.imageButtonPhotoChat.setImageResource(R.drawable.ic_photo);
//            }
//        });

        mViewDataBinding.editTextMessage.setOnClickListener(v -> {
            mViewDataBinding.editTextMessage.requestFocus();
            mViewDataBinding.recyclerSticker.setVisibility(View.GONE);
            mViewDataBinding.recyclerImage.setVisibility(View.GONE);
            Log.d("Vao show key", chatAdapter.getItemCount() + "");
            inputMethodManager.showSoftInput(mViewDataBinding.editTextMessage, InputMethodManager.SHOW_IMPLICIT);
            mViewDataBinding.imageSendSticker.setImageResource(R.drawable.ic_smile_1);
            mViewDataBinding.imageButtonPhotoChat.setImageResource(R.drawable.ic_photo);
            Log.d("Count mess", chatAdapter.getItemCount() + "");
            mViewDataBinding.recyclerChat.smoothScrollToPosition(chatAdapter.getItemCount());
        });

        mViewDataBinding.editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewDataBinding.recyclerSticker.setVisibility(View.GONE);
                mViewDataBinding.imageSendSticker.setImageResource(R.drawable.ic_smile_1);
                //set nếu chưa nhập gì thì disable icon send
                if (TextUtils.isEmpty(s.toString())) {
                    mViewDataBinding.imageButtonSend.setEnabled(false);
                    mViewDataBinding.imageButtonSend.setImageResource(R.drawable.ic_send_unable);
                } else {
                    mViewDataBinding.imageButtonSend.setEnabled(true);
                    mViewDataBinding.imageButtonSend.setImageResource(R.drawable.ic_send);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        mViewDataBinding.imageSendSticker.setOnClickListener(view1 -> {
            //set hightlight icon sticker
            if (mViewDataBinding.recyclerSticker.getVisibility() == View.VISIBLE) {
                mViewDataBinding.editTextMessage.requestFocus();
                mViewDataBinding.recyclerImage.setVisibility(View.GONE);
                mViewDataBinding.recyclerSticker.setVisibility(View.GONE);
                inputMethodManager.showSoftInput(mViewDataBinding.editTextMessage, InputMethodManager.SHOW_IMPLICIT);
                mViewDataBinding.imageSendSticker.setImageResource(R.drawable.ic_smile_1);
            } else {
                mViewDataBinding.imageButtonPhotoChat.setImageResource(R.drawable.ic_photo);
                inputMethodManager.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
                mViewDataBinding.editTextMessage.clearFocus();
                mViewDataBinding.recyclerImage.setVisibility(View.GONE);
                mViewDataBinding.recyclerSticker.setVisibility(View.VISIBLE);
                mViewDataBinding.imageSendSticker.setImageResource(R.drawable.ic_smile_blue);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getString("idUser");
        }
        mViewModel.getInfoUserChat(id);
        mViewDataBinding.recyclerChat.setLayoutManager(layoutManager);
        mViewDataBinding.recyclerChat.setHasFixedSize(true);
        chatAdapter = new ChatAdapter(getContext(), "");

        chatAdapter.setOnItemClickListener(messageModel -> {
            //click vào tin nhắn là ảnh
            if (messageModel.getType().equals("Image")) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                //cho phép zoom ảnh
                ZoomImageFragment zoomImageFragment = new ZoomImageFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("image", messageModel.getMessage());
                zoomImageFragment.setArguments(bundle1);
                fragmentTransaction.add(R.id.frameLayoutChat, zoomImageFragment, null).commit();
                fragmentTransaction.addToBackStack(null);
            }
        });
        mViewDataBinding.recyclerChat.setAdapter(chatAdapter);
        //set online, offline
        mViewModel.userChatLiveData.observe(getViewLifecycleOwner(), userModel -> {
            chatAdapter.setImage(userModel.getUserImgUrl());
            if (userModel.getStatus().equals("offline")) {
                mViewDataBinding.stringStatusChat.setText(R.string.txt_status_offline);
                mViewDataBinding.statusChat.setImageResource(R.drawable.status_offline);
            } else {
                mViewDataBinding.stringStatusChat.setText(R.string.txt_status_online);
                mViewDataBinding.statusChat.setImageResource(R.drawable.status_online);
            }

            if (userModel.getUserImgUrl().equals("default")) {
                Glide.with(requireContext()).load(R.mipmap.ic_launcher).circleCrop().into(mViewDataBinding.imageViewTitleChat);
            } else {
                Glide.with(requireContext()).load(userModel.getUserImgUrl()).circleCrop().into(mViewDataBinding.imageViewTitleChat);
            }
            mViewDataBinding.textViewUserNameChat.setText(userModel.getUserName());
            mViewDataBinding.recyclerChat.setHasFixedSize(true);
            SimpleItemAnimator itemAnimator = (SimpleItemAnimator) mViewDataBinding.recyclerChat.getItemAnimator();
            assert itemAnimator != null;
            itemAnimator.setSupportsChangeAnimations(false);
        });
        checkSeen(id);
        mViewModel.displayMessage(id, lastPositionChat);
        mViewDataBinding.recyclerChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                loadPosition = recyclerView.getChildCount();
                int position = layoutManager.findFirstVisibleItemPosition();
                if (position == 0) {
                    mViewModel.displayMessage(id, lastPositionChat);
                }
            }
        });
        mViewModel.messageListLiveData.observe(getViewLifecycleOwner(), messageModels -> {
            ArrayList<MessageModel> arrayList = new ArrayList<>(messageModels);
            if (arrayList.size() > 0) {
                lastPositionChat = arrayList.get(0).getTimeLong();// tin nhắn cuối cùng hiển thị time
                // hiển thị tin nhắn gửi cùng lúc - chỉ show 1 icon user
                for (int i = 0; i < arrayList.size() - 1; i++) {
                    int j = i + 1;
                    if (arrayList.get(i).getIdReceiver().equals(arrayList.get(j).getIdReceiver())
                            && arrayList.get(i).getIdSender().equals(arrayList.get(j).getIdSender())) {
                        arrayList.get(i).setIsShow(true);
                    }
                }
                chatAdapter.submitList(arrayList);
                mViewDataBinding.recyclerChat.smoothScrollToPosition(arrayList.size()-1);
            }
            else {
                chatAdapter.submitList(arrayList);
            }
        });

        stickerAdapter = new StickerAdapter(Arrays.asList(stickerResource), requireContext());
        mViewDataBinding.recyclerSticker.setAdapter(stickerAdapter);
        mViewDataBinding.recyclerSticker.setHasFixedSize(true);
        //set click vào sticker
        stickerAdapter.setOnItemClickListener(nameSticker -> mViewModel.sendMessage(id, nameSticker, "sticker"));

        ArrayList<String> arrayImage = getAllShownImagesPath(getActivity());
        imageAdapter = new ImageAdapter(arrayImage, requireContext());
        mViewDataBinding.recyclerImage.setAdapter(imageAdapter);
        mViewDataBinding.recyclerImage.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mViewDataBinding.recyclerImage.setHasFixedSize(true);

        imageAdapter.setOnItemClickListener(uri -> {
            imageUri = uri;
            uploadImage();
        });
    }
// Khi click user bất kì check id user đó = id user get từ firebase => set checkseen = true
    public void checkSeen(String id) {
        String myId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = mViewModel.checkSeen(id);
        listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    MessageModel message = keyNode.getValue(MessageModel.class);
                    assert message != null;
                    if (message.getIdReceiver().equals(myId) && message.getIdSender().equals(id)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("checkSeen", true);
                        keyNode.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(listener);
    }
// nút back
    private void removeFragment() {
        inputMethodManager.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        databaseReference.removeEventListener(listener);
        Fragment fragment = getParentFragmentManager().findFragmentById(R.id.frameLayoutChat);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.exit_left, R.anim.pop_exit_left);
        assert fragment != null;
        fragmentTransaction.remove(fragment);
        getParentFragmentManager().popBackStack();
        fragmentTransaction.commit();
    }

    private void buildRecyclerImage() {
        mViewDataBinding.imageSendSticker.setImageResource(R.drawable.ic_smile_1);
        if (mViewDataBinding.recyclerImage.getVisibility() == View.VISIBLE) {
            mViewDataBinding.editTextMessage.requestFocus();
            mViewDataBinding.recyclerImage.setVisibility(View.GONE);
            mViewDataBinding.recyclerSticker.setVisibility(View.GONE);
            mViewDataBinding.imageButtonPhotoChat.setImageResource(R.drawable.ic_photo);
            inputMethodManager.showSoftInput(mViewDataBinding.editTextMessage, InputMethodManager.SHOW_IMPLICIT);
        } else {
            inputMethodManager.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
            mViewDataBinding.editTextMessage.clearFocus();
            mViewDataBinding.recyclerImage.setVisibility(View.VISIBLE);
            mViewDataBinding.recyclerSticker.setVisibility(View.GONE);
            mViewDataBinding.imageButtonPhotoChat.setImageResource(R.drawable.ic_photo_blue);
        }
    }

    private ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            mViewDataBinding.txtNotAllowPer.setVisibility(View.VISIBLE);

        } else {
            mViewDataBinding.txtNotAllowPer.setVisibility(View.GONE);
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            cursor = activity.getContentResolver().query(uri, projection, null,
                    null, null);
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);

                listOfAllImages.add(absolutePathOfImage);
            }
        }
        return listOfAllImages;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = requireContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Sending");
        progressDialog.show();
        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
            Log.d("Show Uri in fragment", imageUri.toString());
            uploadTask = fileReference.putFile(imageUri);
            //get url ảnh
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return fileReference.getDownloadUrl();

            }).addOnCompleteListener(task -> {
                // ép kiểu url => string uri => dùng viewmodel để hiển thị
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    assert downloadUri != null;
                    String mUri = downloadUri.toString();
                    uriImage = mUri;
                    mViewModel.sendMessage(id, mUri, "Image");
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            });
        } else {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Log.d("Show Uri in fragment", imageUri.toString());
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }
}
