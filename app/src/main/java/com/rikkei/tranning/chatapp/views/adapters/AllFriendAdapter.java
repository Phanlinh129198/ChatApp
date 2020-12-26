package com.rikkei.tranning.chatapp.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rikkei.tranning.chatapp.R;
import com.rikkei.tranning.chatapp.services.models.AllUserModel;
import com.rikkei.tranning.chatapp.views.uis.friend.SharedFriendViewModel;

public class AllFriendAdapter extends ListAdapter<AllUserModel, AllFriendAdapter.ViewHolder> {
    Context context;
    SharedFriendViewModel sharedFriendViewModel;
    private OnItemClickListener listener;

    public AllFriendAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
        sharedFriendViewModel = ViewModelProviders.of((FragmentActivity) context).get(SharedFriendViewModel.class);
    }

    // fĩ bug load lại màn hình khi chọn options cancel, addfriend,...
    private static final DiffUtil.ItemCallback<AllUserModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<AllUserModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull AllUserModel oldItem, @NonNull AllUserModel newItem) {
            return oldItem.getUserId().equals(newItem.getUserId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull AllUserModel oldItem, @NonNull AllUserModel newItem) {
            return oldItem.getUserName().equals(newItem.getUserName())
                    && oldItem.getUserImage().equals(newItem.getUserImage())
                    && oldItem.getUserType().equals(newItem.getUserType());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final AllUserModel user = getItem(position);

        //set avatar các user trong màn all friend
        if (user.getUserImage().equals("default")) {
            Glide.with(context).load(R.mipmap.ic_launcher).circleCrop().into(holder.cimgUser);
        } else {
            Glide.with(context).load(user.getUserImage()).circleCrop()
                    .into(holder.cimgUser);
        }
        //phân loại chữ theo a-z
        holder.txtSection.setText(user.getUserName().substring(0, 1).toUpperCase());
        holder.txtUserName.setText(user.getUserName());

        switch (user.getUserType()) {
            case "NoFriend":
                holder.btnRequest.setText(R.string.txt_request_friend);
                holder.btnRequest.setBackgroundResource(R.drawable.button_custom);
                holder.btnRequest.setTextColor(ContextCompat.getColor(context, R.color.white));
                break;
            case "friend":
                holder.btnRequest.setText(R.string.txt_cancel_friend);
                holder.btnRequest.setBackgroundResource(R.drawable.button_custom);
                holder.btnRequest.setTextColor(ContextCompat.getColor(context, R.color.white));
                break;
            case "sendRequest":
                holder.btnRequest.setText(R.string.txt_cancel);
                holder.btnRequest.setBackgroundResource(R.drawable.custom_button_unfriend);
                holder.btnRequest.setTextColor(ContextCompat.getColor(context, R.color.blue));
                break;
            case "friendRequest":
                holder.btnRequest.setText(R.string.txt_accept);
                holder.btnRequest.setBackgroundResource(R.drawable.button_custom);
                holder.btnRequest.setTextColor(ContextCompat.getColor(context, R.color.white));
                break;
        }

        // group các chữ cái đầu trong list user
        if (position > 0) {
            int i = position - 1;
            if (i <= this.getItemCount() && user.getUserName().substring(0, 1).toUpperCase()
                    .equals(getNoteAt(i).getUserName().substring(0, 1).toUpperCase())) {
                holder.txtSection.setVisibility(View.GONE);
            }
        }

        holder.btnRequest.setOnClickListener(v -> {
            switch (holder.btnRequest.getText().toString()) {
                case "Kết bạn":
                case "Add Friend":
                    sharedFriendViewModel.createFriend(user);
                    holder.btnRequest.setText(R.string.txt_cancel);
                    break;
                case "Hủy":
                case "Hủy bạn":
                case "UnFriend":
                case "Cancel":
                    sharedFriendViewModel.deleteFriend(user);
                    holder.btnRequest.setText(R.string.txt_request_friend);
                    break;
                case "Đồng ý":
                case "Accept":
                    sharedFriendViewModel.updateFriend(user);
                    holder.btnRequest.setText(R.string.txt_cancel_friend);
                    break;
            }
        });
    }

    public AllUserModel getNoteAt(int position) {
        return getItem(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cimgUser;
        TextView txtUserName, txtSection;
        Button btnRequest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cimgUser = itemView.findViewById(R.id.CircleImageViewItem);
            txtUserName = itemView.findViewById(R.id.TextViewNameUserItem);
            btnRequest = itemView.findViewById(R.id.ButtonRequestItem);
            txtSection = itemView.findViewById(R.id.textViewHeader);// section để group từng chữ cái đầu của tên
            //check hiển thị thông báo không nhắn tin đc
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
               if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(AllUserModel userModel);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
