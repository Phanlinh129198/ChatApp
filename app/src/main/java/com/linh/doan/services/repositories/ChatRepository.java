package com.linh.doan.services.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.linh.doan.services.models.ChatModel;
import com.linh.doan.services.models.MessageModel;
import com.linh.doan.services.models.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ChatRepository {
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    ArrayList<String> listIdChat = new ArrayList<>();
    ArrayList<UserModel> arrayAllUserChat = new ArrayList<>();


    MutableLiveData<Boolean> isOk = new MutableLiveData<>(false);
    MutableLiveData<Boolean> isLoadInfoUser = new MutableLiveData<>(false);
    MutableLiveData<Boolean> isLoadedMessage = new MutableLiveData<>(false);

    public void infoUserFromFirebase(String id, final DataStatus dataStatus) {
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel account = dataSnapshot.getValue(UserModel.class);
                dataStatus.DataIsLoaded(account);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void createMessage(String id, String message, String type) {
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm ", Locale.getDefault());
        String hour = simpleDateFormatTime.format(calendar.getTime());
        String date = simpleDateFormatDate.format(calendar.getTime());
        databaseReference = FirebaseDatabase.getInstance().getReference("chat");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String idUser = firebaseUser.getUid();
            String delete = id + idUser;
            MessageModel messageModel = new MessageModel(idUser, id, message, type, date, hour, false, System.currentTimeMillis(), false, delete);
            String key;// biến để phân biệt tin nhắn chat là của user nào
            if (id.compareTo(idUser) > 0) {
                key = id + idUser;
            } else {
                key = idUser + id;
            }
            databaseReference.child(key).push().setValue(messageModel);
        }
    }
//load 10 tin nhắn
    public void getSomeOfMessage(String idFriend, MessageStatus messageStatus) {
        if (firebaseUser == null) {
            return;
        }
        String myId = firebaseUser.getUid();
        String key;
        if (idFriend.compareTo(myId) > 0) {
            key = idFriend + myId;
        } else {
            key = myId + idFriend;
        }
        Query query = FirebaseDatabase.getInstance().getReference("chat").child(key).limitToLast(10);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<MessageModel> messageList = new ArrayList<>();
                messageList.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    MessageModel message = keyNode.getValue(MessageModel.class);
                    messageList.add(message);
                }
                isLoadedMessage.setValue(true);
                messageStatus.DataIsLoaded(messageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
// hỏi trung
    public void getMessage(String idFriend, long lastPositionChat, MessageStatus messageStatus) {
        if (firebaseUser == null) {
            return;
        }
        String myId = firebaseUser.getUid();
        String key;
        if (idFriend.compareTo(myId) > 0) {
            key = idFriend + myId;
        } else {
            key = myId + idFriend;
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("chat").child(key);

        if (lastPositionChat == 0) {
            databaseReference.limitToLast(15).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<MessageModel> messageList = new ArrayList<>();
                    messageList.clear();
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        MessageModel message = keyNode.getValue(MessageModel.class);
                        messageList.add(message);
                    }

                    messageStatus.DataIsLoaded(messageList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Log.d("last time repo", lastPositionChat + "");
            databaseReference.orderByChild("timeLong").endAt(lastPositionChat).limitToLast(15).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<MessageModel> messageList = new ArrayList<>();
//                    messageList.clear();
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        MessageModel message = keyNode.getValue(MessageModel.class);

                        messageList.add(message);
                    }

                    messageStatus.DataIsLoaded(messageList);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ArrayList<MessageModel> messageList = new ArrayList<>();
//                messageList.clear();
//                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
//                    MessageModel message = keyNode.getValue(MessageModel.class);
//                    messageList.add(message);
//                }
//
//                messageStatus.DataIsLoaded(messageList);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    public void getAllIdListChat(ListIdChatStatus listIdChatStatus) {
        databaseReference = FirebaseDatabase.getInstance().getReference("chat");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listIdChat.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    String key = keyNode.getKey();
                    assert key != null;
                    //phân biệt user đã chat vs mk
                    if (key.contains(firebaseUser.getUid())) {
                        //key = myid + userid => chia ra để ktra xem id của  mk hay user khác để thêm vào list
                        String x = key.substring(0, key.length() / 2);
                        String y = key.substring(key.length() / 2);
                        if (!x.equals(firebaseUser.getUid())) {
                            listIdChat.add(x);
                        } else {
                            listIdChat.add(y);
                        }
                    }
                }
                isOk.setValue(true);
                listIdChatStatus.DataIsLoaded(listIdChat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAllInfoUserChat(ListInfoAllUser listInfoAllUser) {
        getAllIdListChat(listKey -> {
            if (isOk.getValue() != null && isOk.getValue()) {
                databaseReference = FirebaseDatabase.getInstance().getReference("user");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayAllUserChat.clear();
                        for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                            UserModel userModel = keyNode.getValue(UserModel.class);
                            for (int i = 0; i < listKey.size(); i++) {
                                assert userModel != null;
                                if (listKey.get(i).equals(userModel.getUserId())) {
                                    arrayAllUserChat.add(userModel);
                                }
                            }
                        }
                        isLoadInfoUser.setValue(true);
                        listInfoAllUser.DataIsLoaded(arrayAllUserChat);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public DatabaseReference checkSeen(String idFriend) {
        String myId = firebaseUser.getUid();
        String key;
        if (idFriend.compareTo(myId) > 0) {
            key = idFriend + myId;
        } else {
            key = myId + idFriend;
        }
        return databaseReference = FirebaseDatabase.getInstance().getReference("chat").child(key);
    }

    public void getListMessage(ListMessageStatus listMessageStatus) {
        getAllInfoUserChat(arrayInfoAllUserChat -> {
            if (isLoadInfoUser.getValue() != null && isLoadInfoUser.getValue()) {
                ArrayList<ChatModel> listChat = new ArrayList<>();
                listChat.clear();
                for (int i = 0; i < arrayInfoAllUserChat.size(); i++) {
                    int finalI = i;
                    ChatModel chatModel = new ChatModel();
                    int finalI1 = i;
                    getSomeOfMessage(arrayInfoAllUserChat.get(i).getUserId(), messageArray -> {
                        if (isLoadedMessage.getValue() != null && isLoadedMessage.getValue()) {
                            listChat.remove(chatModel);
                            chatModel.setUserModel(arrayInfoAllUserChat.get(finalI));
                            chatModel.setMessageModelArrayList(messageArray);
                            isLoadedMessage.setValue(false);
                        }
                        listChat.add(chatModel);
                        if (finalI1 == arrayInfoAllUserChat.size() - 1) {
                            listMessageStatus.DataIsLoaded(listChat);
                        }
                    });
                }
            }
        });
    }

    public interface ListMessageStatus {
        void DataIsLoaded(ArrayList<ChatModel> listChatArray);
    }

    public interface DataStatus {
        void DataIsLoaded(UserModel user);
    }

    public interface MessageStatus {
        void DataIsLoaded(List<MessageModel> messageArray);
    }

    public interface ListIdChatStatus {
        void DataIsLoaded(ArrayList<String> listKey);
    }

    public interface ListInfoAllUser {
        void DataIsLoaded(ArrayList<UserModel> arrayInfoAllUserChat);
    }
}