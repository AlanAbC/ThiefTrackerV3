package com.iw.thieftrackerv3.Services

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.iw.thieftrackerv3.Config.BaseUrl
import com.iw.thieftrackerv3.Login
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Services {

    //Service login
    @POST("login/")
    @FormUrlEncoded
    abstract fun login(@Field("email") code: String, @Field("password") password: String): Call<com.iw.thieftrackerv3.Services.Login.Login>


}