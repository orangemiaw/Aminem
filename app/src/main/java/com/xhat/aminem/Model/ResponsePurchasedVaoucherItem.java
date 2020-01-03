package com.xhat.aminem.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePurchasedVaoucherItem {

    @SerializedName("data")
    private List<LogItem> data;

    @SerializedName("code")
    private int error;

    @SerializedName("message")
    private String message;

    public void SetLogitem(List<LogItem> data) {
        this.data = data;
    }

    public List<LogItem> getLogitem() {
        return data;
    }

    public void setError(int error){
        this.error = error;
    }

    public boolean isError(){
        if(error != 200) {
            return true;
        }
        return false;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    @Override
    public String toString(){
        return
                "ResponseLogItem{" +
                        "data = '" + data + '\'' +
                        ",error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}
