package com.rikkei.tranning.chatapp.services.models;


public class MessageModel {
    private String idSender;
    private String idReceiver;
    private String message;
    private String type;
    private String date;
    private String time;
    private long timeLong;
    private Boolean checkSeen;
    private Boolean isShow;

    public MessageModel(String idSender, String idReceiver, String message, String type, String date, String time, Boolean checkSeen, long timeLong, Boolean isShow) {
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.message = message;
        this.type = type;
        this.date = date;
        this.time = time;
        this.timeLong = timeLong;
        this.checkSeen = checkSeen;
        this.isShow=isShow;
    }

    public MessageModel() {
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public Boolean getCheckSeen() {
        return checkSeen;
    }

    public void setCheckSeen(Boolean checkSeen) {
        this.checkSeen = checkSeen;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(long timeLong) {
        this.timeLong = timeLong;
    }
}
