package com.rikkei.tranning.chatapp.services.models;

public class AllUserModel {
    private String userId;
    private String userName;
    private String userType;
    private String userImage;

    public AllUserModel() {
    }

    public AllUserModel(String userName, String userType, String userImage, String userId) {
        this.userName = userName;
        this.userType = userType;
        this.userImage = userImage;
        this.userId=userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
