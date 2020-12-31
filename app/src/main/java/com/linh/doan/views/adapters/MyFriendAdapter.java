package com.linh.doan.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.linh.doan.R;
import com.linh.doan.services.models.AllUserModel;

public class MyFriendAdapter extends ListAdapter<AllUserModel, MyFriendAdapter.ViewHolder> {
    Context context;
    private OnItemClickListener listener;

    public MyFriendAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final AllUserModel user = getItem(position);
        holder.relativeItemFriend.setVisibility(View.VISIBLE);
        holder.btnRequest.setVisibility(View.GONE);
        if (user.getUserImage().equals("default")) {
            Glide.with(context).load(R.mipmap.ic_launcher).circleCrop().into(holder.cImgUser);
        } else {
            Glide.with(context).load(user.getUserImage()).circleCrop()
                    .into(holder.cImgUser);
        }
        holder.txtSection.setText(user.getUserName().substring(0, 1).toUpperCase());
        holder.txtUserName.setText(user.getUserName());
        if (position > 0) {
            int i = position - 1;
            if (i <= this.getItemCount() && user.getUserName().substring(0, 1).toUpperCase()
                    .equals(getNoteAt(i).getUserName().substring(0, 1).toUpperCase())) {
                holder.txtSection.setVisibility(View.GONE);
            }
        }
    }

    public AllUserModel getNoteAt(int position) {
        return getItem(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cImgUser;
        RelativeLayout relativeItemFriend;
        TextView txtUserName, txtSection;
        Button btnRequest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeItemFriend = itemView.findViewById(R.id.relativeItemFriend);
            cImgUser = itemView.findViewById(R.id.CircleImageViewItem);
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
