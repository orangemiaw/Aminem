package com.xhat.aminem.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseVoucherItem {

    @SerializedName("data")
    private List<VoucherItem> data;

    @SerializedName("code")
    private int error;

    @SerializedName("message")
    private String message;

    public void SetVoucherItem(List<VoucherItem> data) {
        this.data = data;
    }

    public List<VoucherItem> getVoucherItem() {
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
                "ResponseVoucherItem{" +
                        "data = '" + data + '\'' +
                        ",error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}
