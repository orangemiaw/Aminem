package com.xhat.aminem.Model;

import com.google.gson.annotations.SerializedName;

public class MetaItem {

    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    public void setCode(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }

    public void setMessage(String message){
        this.code = message;
    }

    public String getMessage(){
        return message;
    }

    @Override
    public String toString(){
        return
                "MetaItem{" +
                        "code = '" + code + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}
