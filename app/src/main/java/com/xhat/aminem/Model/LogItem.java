package com.xhat.aminem.Model;

import com.google.gson.annotations.SerializedName;

public class LogItem {

    @SerializedName("_id")
    private String id;

    @SerializedName("created")
    private String created;

    @SerializedName("action")
    private String action;

    @SerializedName("ip")
    private String ip;

    @SerializedName("controller")
    private String controller;

    public void setLogId(String id){
        this.id = id;
    }

    public String getLogId(){
        return id;
    }

    public void setCreated(String created){
        this.created = created;
    }

    public String getCreated(){
        return created;
    }

    public void setAction(String action){
        this.action = action;
    }

    public String getAction(){
        return action;
    }

    public void setIp(String ip){
        this.ip = ip;
    }

    public String getIp(){
        return ip;
    }

    public void setController(String controller){
        this.controller = controller;
    }

    public String getController(){
        return controller;
    }

    @Override
    public String toString(){
        return
                "LogItem{" +
                        "id = '" + id + '\'' +
                        ",created = '" + created + '\'' +
                        ",action = '" + action + '\'' +
                        ",ip = '" + ip + '\'' +
                        ",controller = '" + controller + '\'' +
                        "}";
    }
}
