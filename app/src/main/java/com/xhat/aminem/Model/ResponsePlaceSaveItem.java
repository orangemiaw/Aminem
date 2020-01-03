package com.xhat.aminem.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePlaceSaveItem {

    @SerializedName("data")
    private List<PlaceSaveItem> data;

    @SerializedName("code")
    private int error;

    @SerializedName("message")
    private String message;

    public void SetPlaceSaveItem(List<PlaceSaveItem> data) {
        this.data = data;
    }

    public List<PlaceSaveItem> getPlaceSaveItem() {
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
                "ResponsePlaceSaveItem{" +
                        "data = '" + data + '\'' +
                        ",error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}
