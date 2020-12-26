package com.rikkei.tranning.chatapp.services.models;

public class FriendsModel {
    private String friendId;
    private String type;

    public FriendsModel() {
    }

    public FriendsModel(String friendId, String type) {
        this.friendId = friendId;
        this.type = type;
    }
    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
