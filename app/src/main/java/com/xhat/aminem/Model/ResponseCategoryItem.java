package com.xhat.aminem.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCategoryItem {

    @SerializedName("data")
    private List<CategoryItem> data;

    @SerializedName("code")
    private int error;

    @SerializedName("message")
    private String message;

    public void SetCategoryItem(List<CategoryItem> data) {
        this.data = data;
    }

    public List<CategoryItem> getCategoryItem() {
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
                "ResponseCategoryItem{" +
                        "data = '" + data + '\'' +
                        ",error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}
