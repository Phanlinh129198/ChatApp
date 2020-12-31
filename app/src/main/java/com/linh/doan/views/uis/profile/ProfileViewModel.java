package com.linh.doan.views.uis.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.linh.doan.services.models.UserModel;
import com.linh.doan.services.repositories.ProfileRepository;

public class ProfileViewModel extends ViewModel {
    public MutableLiveData<UserModel> userMutableLiveData = new MutableLiveData<>();

    public void getInfoUser() {
        new ProfileRepository().infoUserFromFirebase(user -> userMutableLiveData.setValue(user));
    }
}
