package com.xhat.aminem.Model;

import com.google.gson.annotations.SerializedName;

public class PurchasedVoucherItem {

    @SerializedName("_id")
    private String id;

    @SerializedName("purchased_date")
    private String purchased_date;

    @SerializedName("voucher_code")
    private String voucher_code;

    @SerializedName("voucher_name")
    private String voucher_name;

    @SerializedName("voucher_image")
    private String voucher_image;

    @SerializedName("voucher_description")
    private String voucher_description;

    @SerializedName("voucher_price")
    private Integer voucher_price;

    @SerializedName("voucher_status")
    private Integer voucher_status;

    public String getId() {
        return id;
    }

    @Override
    public String toString(){
        return
                "LogItem{" +
                        "id = '" + id + '\'' +
                        ",purchased_date = '" + purchased_date + '\'' +
                        ",voucher_code = '" + voucher_code + '\'' +
                        ",voucher_name = '" + voucher_name + '\'' +
                        ",voucher_image = '" + voucher_image + '\'' +
                        ",voucher_description = '" + voucher_description + '\'' +
                        ",voucher_price = '" + voucher_price + '\'' +
                        ",voucher_status = '" + voucher_status + '\'' +
                        "}";
    }
}
