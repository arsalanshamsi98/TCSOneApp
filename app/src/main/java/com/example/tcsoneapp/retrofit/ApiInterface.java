package com.example.tcsoneapp.retrofit;


import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static com.example.tcsoneapp.retrofit.RetrofitService.BASE_URL;



public interface ApiInterface {

    @POST(BASE_URL)
    Call<ResponseBody> loginUser(@Body JsonObject body);


}