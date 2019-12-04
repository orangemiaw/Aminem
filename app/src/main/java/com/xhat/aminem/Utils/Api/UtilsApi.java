package com.xhat.aminem.Utils.Api;

public class UtilsApi {

    public static final String BASE_API = "https://357c99cd.ngrok.io/aminem/api/v1/";

    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_API).create(BaseApiService.class);
    }
}
