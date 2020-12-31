package com.linh.doan.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.linh.doan.R;
import com.linh.doan.services.models.ChatModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    Context context;
    ArrayList<ChatModel> chatModelArrayList;
    private OnItemClickListener listener;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public MessagesAdapter(Context context, ArrayList<ChatModel> chatModelArrayList) {
        this.context = context;
        this.chatModelArrayList = chatModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_friend_massage, parent, false);
        return new ViewHolder(view);
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_massage, parent, false);
//        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatModel chatModel = chatModelArrayList.get(position);
        holder.txtUserName.setText(chatModel.getUserModel().getUserName());
        if (chatModel.getUserModel().getUserImgUrl().equals("default")) {
            Glide.with(context).load(R.mipmap.ic_launcher).circleCrop().into(holder.cImgUser);
        } else {
            Glide.with(context).load(chatModel.getUserModel().getUserImgUrl()).circleCrop()
                    .into(holder.cImgUser);
        }
        String message = null;
        String date = null;
        String time = null;
        String type = null;
        String idSender = null;
        int count = 0;
        for (int i = 0; i < chatModel.getMessageModelArrayList().size(); i++) {
            if (chatModel.getMessageModelArrayList().get(i).getIdReceiver().equals(firebaseUser.getUid())
                    && chatModel.getMessageModelArrayList().get(i).getIdSender().equals(chatModel.getUserModel().getUserId())
                    && !chatModel.getMessageModelArrayList().get(i).getCheckSeen()) {
                count++;
            }
            idSender = chatModel.getMessageModelArrayList().get(i).getIdSender();
            message = chatModel.getMessageModelArrayList().get(i).getMessage();
            date = chatModel.getMessageModelArrayList().get(i).getDate();
            time = chatModel.getMessageModelArrayList().get(i).getTime();
            type = chatModel.getMessageModelArrayList().get(i).getType();
        }
        assert idSender != null;
        if (idSender.equals(firebaseUser.getUid())) {
            message = "Bạn: " + message;
        }
        assert type != null;
        if (type.equals("Text")) {
            holder.txtLastMessage.setText(message);
        } else if (type.equals("sticker")) {

            if (idSender.equals(firebaseUser.getUid())) {
                holder.txtLastMessage.setText("Bạn: Sticker");
            } else
                holder.txtLastMessage.setText("Sticker");
        } else {
            if (idSender.equals(firebaseUser.getUid())) {
                holder.txtLastMessage.setText("Bạn: Image");
            } else
                holder.txtLastMessage.setText("Image");
        }
        if (count > 0) {
            holder.txtSumUnRead.setVisibility(View.VISIBLE);
            if (count > 9) {
                holder.txtSumUnRead.setText("" + 9 + "+");
            } else {
                holder.txtSumUnRead.setText(" " + count + " ");
            }
            holder.txtLastMessage.setTextColor(Color.BLACK);
            holder.txtLastMessage.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtTime.setTextColor(Color.BLACK);
            holder.txtTime.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.txtSumUnRead.setVisibility(View.GONE);
            holder.txtLastMessage.setTypeface(Typeface.DEFAULT);
            holder.txtTime.setTypeface(Typeface.DEFAULT);
            holder.txtLastMessage.setTextColor(ContextCompat.getColor(context, R.color.grey));
            holder.txtTime.setTextColor(ContextCompat.getColor(context, R.color.grey));
        }
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String a = simpleDateFormatDate.format(calendar.getTime());
        calendar.add(Calendar.DATE, -1);
        // String before=simpleDateFormatDate.format(calendar.getTime());
        if (a.equals(date)) {
            holder.txtTime.setText(time);
        }
//            else if (a.trim().equals(before.trim())){
//                holder.txtTime.setText("Hôm qua");
//            }
        else {
            holder.txtTime.setText(date);
        }
    }

    @Override
    public int getItemCount() {
        return chatModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cImgUser;
        TextView txtUserName, txtLastMessage, txtTime, txtSumUnRead;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSumUnRead = itemView.findViewById(R.id.textViewSumUnRead);
            cImgUser = itemView.findViewById(R.id.imageViewCircleChat);
            txtUserName = itemView.findViewById(R.id.textViewUserChatName);
            txtLastMessage = itemView.findViewById(R.id.textViewLastMess);
            txtTime = itemView.findViewById(R.id.textViewTimeMessage);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(chatModelArrayList.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ChatModel userModel);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
