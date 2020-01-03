package com.xhat.aminem.Model;

import com.google.gson.annotations.SerializedName;

public class PlaceSaveItem {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("status")
    private Integer status;

    @Override
    public String toString(){
        return
                "PlaceSaveItem{" +
                        "id = '" + id + '\'' +
                        ",name = '" + name + '\'' +
                        ",image = '" + image + '\'' +
                        ",latitude = '" + latitude + '\'' +
                        ",longitude = '" + longitude + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
