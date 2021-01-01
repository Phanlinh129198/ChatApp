package com.linh.doan.services.models;

public class EditUserModel {
    private String userName;
    private String userPhone;
    private String userDateOfBirth;

    public EditUserModel(String userName, String userPhone, String userDateOfBirth) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.userDateOfBirth = userDateOfBirth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserDateOfBirth() {
        return userDateOfBirth;
    }

    public void setUserDateOfBirth(String userDateOfBirth) {
        this.userDateOfBirth = userDateOfBirth;
    }
}
