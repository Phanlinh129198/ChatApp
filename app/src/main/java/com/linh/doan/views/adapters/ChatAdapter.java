package com.linh.doan.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.linh.doan.R;
import com.linh.doan.services.models.MessageModel;
import com.linh.doan.views.uis.message.ChatViewModel;
import com.linh.doan.views.uis.message.DialogDeleteChatFragment;
import com.linh.doan.views.uis.profile.DialogLogoutFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ChatAdapter extends ListAdapter<MessageModel, ChatAdapter.ViewHolder> {
    Context context;
    public int TITLE_LEFT = 0;
    public int TITLE_RIGHT = 1;
    public String urlImage;
    private OnItemClickListener listener;
    private Fragment fragment;

    public ChatAdapter(Context context, String urlImage,Fragment fragment) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.fragment = fragment;
        this.urlImage = urlImage;
    }

    public void setImage(String urlImage) {
        this.urlImage = urlImage;
    }

    private static final DiffUtil.ItemCallback<MessageModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<MessageModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull MessageModel oldItem, @NonNull MessageModel newItem) {
            return oldItem.getTimeLong() == newItem.getTimeLong();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MessageModel oldItem, @NonNull MessageModel newItem) {
            return oldItem.getIdSender().equals(newItem.getIdSender())
                    && oldItem.getIdReceiver().equals(newItem.getIdReceiver())
                    && oldItem.getMessage().equals(newItem.getMessage())
                    && oldItem.getType().equals(newItem.getType())
                    && oldItem.getDate().equals(newItem.getDate())
                    && oldItem.getIsShow().equals(newItem.getIsShow());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TITLE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_right, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        MessageModel messageModel = getItem(position);
        if (messageModel.getType().equals("Text")) {
            holder.txtMessage.setVisibility(View.VISIBLE);
            holder.txtMessage.setText(messageModel.getMessage());
            holder.imgMessage.setVisibility(View.GONE);
            holder.imgSticker.setVisibility(View.GONE);
        } else if (messageModel.getType().equals("sticker")) {
            holder.imgMessage.setVisibility(View.GONE);
            holder.txtMessage.setVisibility(View.GONE);
            holder.imgSticker.setVisibility(View.VISIBLE);
            int resID = context.getResources().getIdentifier(messageModel.getMessage(), "drawable", context.getPackageName());
            holder.imgSticker.setImageResource(resID);
        } else {
            holder.imgSticker.setVisibility(View.GONE);
            holder.imgMessage.setVisibility(View.VISIBLE);
            holder.txtMessage.setVisibility(View.GONE);
            Glide.with(context).load(messageModel.getMessage()).into(holder.imgMessage);
        }

        if (messageModel.getIsShow()) {
            holder.txtDate.setVisibility(View.GONE);
        } else {
            holder.txtDate.setVisibility(View.VISIBLE);
        }
        if (urlImage.equals("default")) {
            Glide.with(context).load(R.mipmap.ic_launcher).circleCrop().into(holder.imgUserChat);
        } else {
            Glide.with(context).load(urlImage).circleCrop().into(holder.imgUserChat);
        }
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String a = simpleDateFormatDate.format(calendar.getTime());
        if (messageModel.getDate().equals(a)) {
            holder.txtDate.setText(messageModel.getTime());
            holder.txtChatDate.setText("Hôm nay");
        } else {
            holder.txtChatDate.setText(messageModel.getDate());
            holder.txtDate.setText(messageModel.getDate() + " " + messageModel.getTime());
        }
        if (position > 0) {
            int i = position - 1;
            MessageModel message = getNoteAt(i);
            if (i <= this.getItemCount() && messageModel.getIdSender().equals(message.getIdSender())
                    && messageModel.getIdReceiver().equals(message.getIdReceiver())) {
                holder.imgUserChat.setVisibility(View.GONE);
                holder.view.setVisibility(View.VISIBLE);
            }
            if (i <= this.getItemCount() && messageModel.getDate().equals(message.getDate())) {
                holder.txtChatDate.setVisibility(View.GONE);
            }
        }
        holder.txtMessage.setOnClickListener(v -> {
            if (holder.txtDate.getVisibility() == View.VISIBLE) {
                holder.txtDate.setVisibility(View.GONE);
            } else {
                holder.txtDate.setVisibility(View.VISIBLE);
            }
        });
        holder.txtMessage.setOnLongClickListener(view -> {
            DialogDeleteChatFragment dialog = new DialogDeleteChatFragment();
            dialog.show(fragment.getParentFragmentManager(), null);
            return true;
        });

        holder.imgSticker.setOnClickListener(view -> {
            if (holder.txtDate.getVisibility() == View.VISIBLE) {
                holder.txtDate.setVisibility(View.GONE);
            } else {
                holder.txtDate.setVisibility(View.VISIBLE);
            }
        });
        holder.imgSticker.setOnLongClickListener(view -> {
            DialogDeleteChatFragment dialog = new DialogDeleteChatFragment();
            dialog.show(fragment.getParentFragmentManager(), null);
            return true;
        });

//        if (messageModel.getType().equals("Image")) {
//            holder.imgMessage.setOnClickListener(v -> {
//                if (listener != null && position != RecyclerView.NO_POSITION) {
//                    listener.onItemClick(getItem(position));
//                }
//            });
//        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUserChat, imgMessage, imgSticker;
        TextView txtMessage, txtDate, txtChatDate;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChatDate = itemView.findViewById(R.id.textViewChatDate);
            imgMessage = itemView.findViewById(R.id.imageViewMessage);
            imgUserChat = itemView.findViewById(R.id.imageViewUserChat);
            txtMessage = itemView.findViewById(R.id.textViewMessage);
            imgSticker = itemView.findViewById(R.id.imageViewSticker);
            view = itemView.findViewById(R.id.view);
            txtDate = itemView.findViewById(R.id.textViewDate);
            imgMessage.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }

    public MessageModel getNoteAt(int position) {
        return getItem(position);
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String id = "";
        if (firebaseUser != null) {
            id = firebaseUser.getUid();
        }
        if (getNoteAt(position).getIdSender().equals(id)) {
            return TITLE_RIGHT;
        } else {
            return TITLE_LEFT;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MessageModel messageModel);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
