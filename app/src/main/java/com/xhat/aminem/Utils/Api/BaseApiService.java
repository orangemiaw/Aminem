package com.xhat.aminem.Utils.Api;

import com.xhat.aminem.Model.CategoryItem;
import com.xhat.aminem.Model.PlaceSaveItem;
import com.xhat.aminem.Model.ResponseCategoryItem;
import com.xhat.aminem.Model.ResponseLogItem;
import com.xhat.aminem.Model.ResponseLostItem;
import com.xhat.aminem.Model.ResponsePlaceSaveItem;
import com.xhat.aminem.Model.ResponseVoucherItem;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {
    @FormUrlEncoded
    @POST("authenticate")
    Call<ResponseBody> loginRequest(@Field("login") String username,
                                    @Field("password") String password);

    @GET("slider?status=1")
    Call<ResponseBody> getSlider(@Header("Authorization") String authToken);

    @GET("lost_item?order=DESC&status=1&data_per_page=5")
    Call<ResponseLostItem> getLastItemLost(@Header("Authorization") String authToken);

    @GET("lost_item")
    Call<ResponseLostItem> getItemByCategory(@Header("Authorization") String authToken,
                                             @Query("category") String categoryId);

    @GET("lost_item/detail/{itemId}")
    Call<ResponseBody> getLostItemDetail(@Header("Authorization") String authToken,
                                         @Path("itemId") String itemId);

    @GET("lost_item/category_status/{categoryId}")
    Call<ResponseBody> getCategoryStatus(@Header("Authorization") String authToken,
                                         @Path("categoryId") String categoryId);

    @GET("change_log?order=DESC")
    Call<ResponseLogItem> getLogActivity(@Header("Authorization") String authToken);

    @GET("member/detail")
    Call<ResponseBody> getProfile(@Header("Authorization") String authToken);

    @FormUrlEncoded
    @POST("member/change_password")
    Call<ResponseBody> changePassword(@Header("Authorization") String authToken,
                                      @Field("password") String password,
                                      @Field("password_repeat") String confirmPassword);

    @GET("place_save/detail/{placeId}")
    Call<ResponseBody> getPlaceDetail(@Header("Authorization") String authToken,
                                         @Path("placeId") String placeId);

    @GET("lost_item")
    Call<ResponseLostItem> getItemHistory(@Header("Authorization") String authToken,
                                          @Query("user_id") String userId,
                                          @Query("order") String order);

    @GET("lost_item/delete/{itemId}")
    Call<ResponseBody> deleteItem(@Header("Authorization") String authToken,
                                         @Path("itemId") String itemId);

    @GET("voucher?order=DESC")
    Call<ResponseVoucherItem> getVoucherActivity(@Header("Authorization") String authToken);

    @FormUrlEncoded
    @POST("purchased_voucher/add")
    Call<ResponseBody> purchaseVoucher(@Header("Authorization") String authToken,
                                       @Field("voucher_id") String voucherId);

    @GET("lost_item")
    Call<ResponseLostItem> searchItem(@Header("Authorization") String authToken,
                                      @Query("keyword") String keyword);

    @Multipart
    @POST("lost_item/add")
    Call<ResponseBody> addItem(@Header("Authorization") String authToken,
                               @Part MultipartBody.Part image,
                               @Part("name") RequestBody name,
                               @Part("category") RequestBody category,
                               @Part("place_found") RequestBody placeFound,
                               @Part("place_save") RequestBody placeSave,
                               @Part("description") RequestBody description);

    @FormUrlEncoded
    @POST("lost_item/update")
    Call<ResponseBody> updateItem(@Header("Authorization") String authToken,
                                  @Field("item_id") String itemId,
                                  @Field("name") String name,
                                  @Field("category") String category,
                                  @Field("place_found") String placeFound,
                                  @Field("place_save") String placeSave,
                                  @Field("description") String description);

    @GET("item_type")
    Call<ResponseCategoryItem> getAllItemCategory(@Header("Authorization") String authToken);

    @GET("place_save")
    Call<ResponsePlaceSaveItem> getAllPlaceSave(@Header("Authorization") String authToken);
}
