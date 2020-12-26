package com.rikkei.tranning.chatapp.views.uis.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rikkei.tranning.chatapp.services.models.UserModel;
import com.rikkei.tranning.chatapp.services.repositories.ProfileRepository;

public class ProfileViewModel extends ViewModel {
    public MutableLiveData<UserModel> userMutableLiveData = new MutableLiveData<>();

    public void getInfoUser() {
        new ProfileRepository().infoUserFromFirebase(user -> userMutableLiveData.setValue(user));
    }
}
