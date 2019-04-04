package com.iw.thieftrackerv3.Config

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.iw.thieftrackerv3.Services.Services
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Service// Create object Gson for converter factory
// Create object retrofit for petition

// Create service from te interface of services
    () {
    lateinit var services: Services

    init {
        val baseUrl = BaseUrl().baseurl
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        services = retrofit.create(Services::class.java)
    }

    public fun getService(): Services {
        return services
    }
}