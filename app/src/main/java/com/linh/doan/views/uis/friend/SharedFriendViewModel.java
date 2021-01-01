package com.linh.doan.views.uis.friend;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.linh.doan.services.models.AllUserModel;
import com.linh.doan.services.repositories.FriendsRepository;
import com.linh.doan.services.repositories.ProfileRepository;

import java.util.ArrayList;
import java.util.Collections;

public class SharedFriendViewModel extends ViewModel {


    public ArrayList<AllUserModel> arrayAllFriend = new ArrayList<>();
    FriendsRepository friendRepository;
    public MutableLiveData<ArrayList<AllUserModel>> allUserListLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<AllUserModel>> getUserFromLiveData = new MutableLiveData<>();
    public MutableLiveData<String> countNotifiRequest = new MutableLiveData<>("0");

    @RequiresApi(api = Build.VERSION_CODES.N)
    public SharedFriendViewModel() {// số lượng request > 10 thì để thành 9+
        friendRepository = new FriendsRepository();
        friendRepository.getUserInfo((allUserModels, count) -> {
            allUserListLiveData.setValue(allUserModels);
            getUserFromLiveData.setValue(allUserModels);
            arrayAllFriend = allUserModels;
            if (count > 10) {
                countNotifiRequest.setValue("9+");
            } else
                countNotifiRequest.setValue(Integer.toString(count));
        });
    }

    public void collectionArray(ArrayList<AllUserModel> userArray) {
        Collections.sort(userArray, (o1, o2) -> o1.getUserName().substring(0, 1).toLowerCase().compareTo(o2.getUserName().substring(0, 1).toLowerCase()));
    }

    public void createFriend(AllUserModel userModel) {
        friendRepository.createFriend(userModel);
    }

    public void deleteFriend(AllUserModel userModel) {
        friendRepository.deleteFriend(userModel);
    }

    public void updateFriend(AllUserModel userModel) {
        friendRepository.updateFriend(userModel);
    }

    public void searchFriend(final String s, ArrayList<AllUserModel> getUserFromLiveData) {
        ArrayList<AllUserModel> allUserList = new ArrayList<>();
        for (int i = 0; i < getUserFromLiveData.size(); i++) {
            String a = getUserFromLiveData.get(i).getUserName();
            if (a.toLowerCase().contains(s.toLowerCase())) {
                allUserList.add(getUserFromLiveData.get(i));
            }
        }
        allUserListLiveData.setValue(allUserList);
    }


    public void updateStatus(String key, String value) {
        new ProfileRepository().updateInforFromFirebase(key, value);
    }
}
