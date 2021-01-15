package com.linh.doan.services.models;


import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class MessageModel {
    private String idKeyNode;
    private String idSender;
    private String idReceiver;
    private String message;
    private String type;
    private String date;
    private String time;
    private long timeLong;
    private Boolean checkSeen;
    private Boolean isShow;
    private String delete;

    public MessageModel(String idKeyNode, String idSender, String idReceiver, String message, String type, String date, String time, Boolean checkSeen, long timeLong, Boolean isShow, String delete) {
        this.idSender = idSender;
        this.idKeyNode = idKeyNode;
        this.idReceiver = idReceiver;
        this.message = message;
        this.type = type;
        this.date = date;
        this.time = time;
        this.timeLong = timeLong;
        this.checkSeen = checkSeen;
        this.isShow = isShow;
        this.delete = delete;
    }

    public MessageModel(String idSender, String idReceiver, String message, String type, String date, String time, Boolean checkSeen, long timeLong, Boolean isShow, String delete) {
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.message = message;
        this.type = type;
        this.date = date;
        this.time = time;
        this.timeLong = timeLong;
        this.checkSeen = checkSeen;
        this.isShow = isShow;
        this.delete = delete;
    }

    public MessageModel() {
    }

    public String getIdKeyNode() {
        return idKeyNode;
    }

    public void setIdKeyNode(String idKeyNode) {
        this.idKeyNode = idKeyNode;
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("checkSeen", checkSeen);
        result.put("date", date);
        result.put("delete", delete);
        result.put("idReceiver", idReceiver);
        result.put("idSender", idSender);
        result.put("isShow", isShow);
        result.put("message", message);
        result.put("time", time);
        result.put("timeLong", timeLong);
        result.put("type", type);

        return result;
    }
}
