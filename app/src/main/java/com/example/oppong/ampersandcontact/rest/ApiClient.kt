package com.example.oppong.ampersandcontact.rest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class ApiClient {
    companion object {
        val BASE_URL = "https://ampersand-contact-exchange-api.herokuapp.com/api/v1/"
        var retrofit: Retrofit? = null

        fun getClient(): Retrofit? {
            if(retrofit == null){
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    }
}