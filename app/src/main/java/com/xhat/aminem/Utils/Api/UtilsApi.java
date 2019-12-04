package com.xhat.aminem.Utils.Api;

public class UtilsApi {

    public static final String BASE_API = "http://indiarkmedia.com/api/v1/";

    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_API).create(BaseApiService.class);
    }
}
