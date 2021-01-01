package com.linh.doan.views.uis.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.linh.doan.services.models.UserModel;
import com.linh.doan.services.repositories.ProfileRepository;

public class EditProfileViewModel extends ViewModel {
    public MutableLiveData<UserModel> userMutableLiveData = new MutableLiveData<>();

    public void getInfoUser() {
        new ProfileRepository().infoUserFromFirebase(user -> userMutableLiveData.setValue(user));
    }

    public void updateInfoUser(String key, String value) {
        new ProfileRepository().updateInforFromFirebase(key, value);
    }

    public Boolean validatePhoneNumber(String a) {
        return a.length() == 10 && a.startsWith("0") || a.equals("default");
    }
    public Boolean validateName(String a) {
        return a.length() <= 60  || a.equals("default");
    }
}