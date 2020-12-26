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

public class RequestFriendAdapter extends ListAdapter<AllUserModel, RequestFriendAdapter.RequestFriendViewHolder> {
    Context context;
    SharedFriendViewModel sharedFriendViewModel;
    private OnItemClickListener listener;

    public RequestFriendAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
        sharedFriendViewModel = ViewModelProviders.of((FragmentActivity) context).get(SharedFriendViewModel.class);
    }

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
    public RequestFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new RequestFriendViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RequestFriendAdapter.RequestFriendViewHolder holder, int position) {
        final AllUserModel user = getItem(position);
        if (user.getUserImage().equals("default")) {
            Glide.with(context).load(R.mipmap.ic_launcher).circleCrop().into(holder.cimgUser);
        } else {
            Glide.with(context).load(user.getUserImage()).circleCrop()
                    .into(holder.cimgUser);
        }
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
        if (position == 0 && user.getUserType().equals("friendRequest")) {
            holder.txtSection.setText(R.string.txt_list_request_friend);
        } else if (position == 0 && user.getUserType().equals("sendRequest")) {
            holder.txtSection.setText(R.string.txt_list_send_friend);
        } else if (getItem(position - 1).getUserType().equals(user.getUserType())) {
            holder.txtSection.setVisibility(View.GONE);


        } else if (!getItem(position - 1).getUserType().equals(user.getUserType())) {
            holder.txtSection.setText(R.string.txt_list_send_friend);

        }

        holder.btnRequest.setOnClickListener(v -> {
            switch (holder.btnRequest.getText().toString()) {
                case "Hủy":
                case "Cancel":
                    sharedFriendViewModel.deleteFriend(user);
                    break;
                case "Đồng ý":
                case "Accept":
                    sharedFriendViewModel.updateFriend(user);   break;
            }
        });
    }

    public class RequestFriendViewHolder extends RecyclerView.ViewHolder {
        ImageView cimgUser;
        TextView txtUserName, txtSection;
        Button btnRequest;

        public RequestFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            cimgUser = itemView.findViewById(R.id.CircleImageViewItem);
            txtUserName = itemView.findViewById(R.id.TextViewNameUserItem);
            btnRequest = itemView.findViewById(R.id.ButtonRequestItem);
            txtSection = itemView.findViewById(R.id.textViewHeader);

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
