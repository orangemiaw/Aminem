package com.xhat.aminem.Utils.Api;

import com.xhat.aminem.Model.ResponseLogItem;
import com.xhat.aminem.Model.ResponseLostItem;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface BaseApiService {
    @FormUrlEncoded
    @POST("authenticate")
    Call<ResponseBody> loginRequest(@Field("login") String username,
                                    @Field("password") String password);

    @GET("slider?status=1")
    Call<ResponseBody> getSlider(@Header("Authorization") String authToken);

    @GET("lost_item?order_by=DESC&status=1&data_per_page=5")
    Call<ResponseLostItem> getLastItemLost(@Header("Authorization") String authToken);

    @GET("change_log?order_by=DESC")
    Call<ResponseLogItem> getLogActivity(@Header("Authorization") String authToken);
}
