package com.xhat.aminem.Model;

import com.google.gson.annotations.SerializedName;

public class VoucherItem {

    @SerializedName("_id")
    private String id;

    @SerializedName("created")
    private String created;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private Integer price;

    @SerializedName("status")
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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
                "VoucherItem{" +
                        "id = '" + id + '\'' +
                        ",created = '" + created + '\'' +
                        ",name = '" + name + '\'' +
                        ",image = '" + image + '\'' +
                        ",description = '" + description + '\'' +
                        ",price = '" + price + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
