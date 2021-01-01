package com.linh.doan.services.models;

import com.linh.doan.services.models.MessageModel;
import com.linh.doan.services.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ChatModel {
    private UserModel userModel;
    private List<MessageModel> messageModelArrayList;

    public ChatModel() {
    }

    public ChatModel(UserModel userModel, ArrayList<MessageModel> messageModelArrayList) {
        this.userModel = userModel;
        this.messageModelArrayList = messageModelArrayList;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public List<MessageModel> getMessageModelArrayList() {
        return messageModelArrayList;
    }

    public void setMessageModelArrayList(List<MessageModel> messageModelArrayList) {
        this.messageModelArrayList = messageModelArrayList;
    }
}
