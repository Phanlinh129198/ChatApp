package com.linh.doan.services.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linh.doan.services.models.AllUserModel;
import com.linh.doan.services.models.FriendsModel;
import com.linh.doan.services.models.UserModel;

import java.util.ArrayList;

public class FriendsRepository {
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<UserModel> userModelArrayList = new ArrayList<>();
    ArrayList<FriendsModel> friendsModelsArray = new ArrayList<>();
    ArrayList<AllUserModel> allUserModelArray = new ArrayList<>();
    Integer countNotiFi =0;

    MutableLiveData<Boolean> isLoadUser = new MutableLiveData<>(false);
    MutableLiveData<Boolean> isLoadFriend = new MutableLiveData<>(false);


    public interface DataStatus {
        void UserIsLoaded(ArrayList<UserModel> userModels);

        void FriendIsLoaded(ArrayList<FriendsModel> friendsModels);
    }

    public interface InfoUser {
        void InfoUserLoaded(ArrayList<AllUserModel> allUserModelArrayList, Integer count);
    }

    public void getUser(final DataStatus dataStatus) {
        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModelArrayList.clear();
                for (DataSnapshot Node : dataSnapshot.getChildren()) {
                    UserModel userModel = Node.getValue(UserModel.class);
                    if (userModel != null && !userModel.getUserId().equals(firebaseUser.getUid())) {
                        userModelArrayList.add(userModel);
                    }
                }
                isLoadUser.setValue(true);
                dataStatus.UserIsLoaded(userModelArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getFriend(final DataStatus dataStatus) {
        databaseReference = FirebaseDatabase.getInstance().getReference("friend").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friendsModelsArray.clear();
                for (DataSnapshot Node : dataSnapshot.getChildren()) {
                    FriendsModel friendsModel = Node.getValue(FriendsModel.class);
                    friendsModelsArray.add(friendsModel);
                }
                isLoadFriend.setValue(true);
                dataStatus.FriendIsLoaded(friendsModelsArray);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getUserInfo(final InfoUser infoUser) {
        getUser(new DataStatus() {
            @Override
            public void UserIsLoaded(ArrayList<UserModel> userModels) {
                userModelArrayList = userModels;
                if (isLoadUser.getValue() != null && isLoadUser.getValue()) {
                    getFriend(new DataStatus() {
                        @Override
                        public void UserIsLoaded(ArrayList<UserModel> userModels) {

                        }

                        @Override
                        public void FriendIsLoaded(ArrayList<FriendsModel> friendsModels) {
                            friendsModelsArray = friendsModels;
                            if (isLoadFriend.getValue() != null && isLoadFriend.getValue()) {
                                allUserModelArray.clear();
                                countNotiFi = 0;
                                for (int i = 0; i < userModelArrayList.size(); i++) {
                                    AllUserModel allUserModel = new AllUserModel();
                                    allUserModel.setUserId(userModelArrayList.get(i).getUserId());
                                    allUserModel.setUserImage(userModelArrayList.get(i).getUserImgUrl());
                                    allUserModel.setUserName(userModelArrayList.get(i).getUserName());
                                    int sum = 0;
                                    for (int j = 0; j < friendsModelsArray.size(); j++) {
                                        if (userModelArrayList.get(i).getUserId().equals(friendsModelsArray.get(j).getFriendId())) {
                                            sum++;
                                            allUserModel.setUserType(friendsModelsArray.get(j).getType());
                                            if (friendsModelsArray.get(j).getType().equals("friendRequest") )
                                                countNotiFi++;
                                        }
                                    }
                                    if (sum == 0) {
                                        allUserModel.setUserType("NoFriend");
                                    }
                                    allUserModelArray.add(allUserModel);
                                }
                                infoUser.InfoUserLoaded(allUserModelArray, countNotiFi);
                            }
                        }
                    });
                }
            }

            @Override
            public void FriendIsLoaded(ArrayList<FriendsModel> friendsModels) {

            }
        });

    }

    public void createFriend(AllUserModel user) {
        databaseReference = FirebaseDatabase.getInstance().getReference("friend");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        String userId = firebaseUser.getUid();
        FriendsModel friend = new FriendsModel(user.getUserId(), "sendRequest");
        FriendsModel friend2 = new FriendsModel(userId, "friendRequest");
        databaseReference.child(userId).child(user.getUserId()).setValue(friend);
        databaseReference.child(user.getUserId()).child(userId).setValue(friend2);
    }

    public void deleteFriend(AllUserModel user) {
        databaseReference = FirebaseDatabase.getInstance().getReference("friend");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        String userId = firebaseUser.getUid();
        databaseReference.child(userId).child(user.getUserId()).setValue(null);
        databaseReference.child(user.getUserId()).child(userId).setValue(null);
    }

    public void updateFriend(AllUserModel user) {
        databaseReference = FirebaseDatabase.getInstance().getReference("friend");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        String userId = firebaseUser.getUid();
        databaseReference.child(userId).child(user.getUserId()).child("type").setValue("friend");
        databaseReference.child(user.getUserId()).child(userId).child("type").setValue("friend");
    }
}