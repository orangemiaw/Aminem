package com.xhat.aminem.Model;


import java.util.List;
import com.google.gson.annotations.SerializedName;

import retrofit2.http.Field;

public class ResponseLostItem {

    @SerializedName("data")
    private List<AlllostitemItem> data;

    @SerializedName("code")
    private int error;

    @SerializedName("message")
    private String message;

    public void SetAlllostitem(List<AlllostitemItem> data) {
        this.data = data;
    }

    public List<AlllostitemItem> getAlllostitem() {
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
                "ResponseLostItem{" +
                        "data = '" + data + '\'' +
                        ",error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }

}
