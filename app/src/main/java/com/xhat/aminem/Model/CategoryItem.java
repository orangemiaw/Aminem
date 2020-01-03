package com.xhat.aminem.Model;

import com.google.gson.annotations.SerializedName;

public class CategoryItem {

    @SerializedName("_id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("status")
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return
                "CategoryItem{" +
                        "id = '" + id + '\'' +
                        ",category = '" + type + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
