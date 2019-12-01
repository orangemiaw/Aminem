package com.xhat.aminem.Model;

import com.google.gson.annotations.SerializedName;

public class AlllostitemItem {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("category")
    private String category;

    @SerializedName("image")
    private String image;

    @SerializedName("description")
    private String description;

    @SerializedName("place_found")
    private String place_found;

    @SerializedName("place_save_name")
    private String place_save_name;

    @SerializedName("place_save_image")
    private String place_save_image;

    @SerializedName("status")
    private String status;

    public void setItemName(String name){
        this.name = name;
    }

    public String getItemName(){
        return name;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getCategory(){
        return category;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getImage(){
        return image;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    public void setPlaceFound(String place_found){
        this.place_found = place_found;
    }

    public String getPlaceFound(){
        return place_found;
    }

    public void setPlaceSaveName(String place_save_name){
        this.place_save_name = place_save_name;
    }

    public String getPlaceSaveName(){
        return place_save_name;
    }

    public void setPlaceSaveImage(String place_save_image){
        this.place_found = place_save_image;
    }

    public String getPlaceSaveImage(){
        return place_save_image;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    @Override
    public String toString(){
        return
                "AlllostitemItem{" +
                        "id = '" + id + '\'' +
                        ",name = '" + name + '\'' +
                        ",category = '" + category + '\'' +
                        ",image = '" + image + '\'' +
                        ",description = '" + description + '\'' +
                        ",place_found = '" + place_found + '\'' +
                        ",place_save_name = '" + place_save_name + '\'' +
                        ",place_save_image = '" + place_save_image + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
