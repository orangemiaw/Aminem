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
import retrofit2.http.Path;

public interface BaseApiService {
    @FormUrlEncoded
    @POST("authenticate")
    Call<ResponseBody> loginRequest(@Field("login") String username,
                                    @Field("password") String password);

    @GET("slider?status=1")
    Call<ResponseBody> getSlider(@Header("Authorization") String authToken);

    @GET("lost_item?order=DESC&status=1&data_per_page=5")
    Call<ResponseLostItem> getLastItemLost(@Header("Authorization") String authToken);

    @GET("lost_item/detail/{itemId}")
    Call<ResponseBody> getLostItemDetail(@Header("Authorization") String authToken,
                                         @Path("itemId") String itemId);

    @GET("change_log?order=DESC")
    Call<ResponseLogItem> getLogActivity(@Header("Authorization") String authToken);

    @GET("member/detail")
    Call<ResponseBody> getProfile(@Header("Authorization") String authToken);

    @FormUrlEncoded
    @POST("member/change_password")
    Call<ResponseBody> changePassword(@Header("Authorization") String authToken,
                                      @Field("password") String password,
                                      @Field("password_repeat") String confirmPassword);
}
