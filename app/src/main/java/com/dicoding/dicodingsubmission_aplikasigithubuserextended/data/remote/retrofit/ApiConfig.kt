package com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.remote.retrofit

import com.dicoding.dicodingsubmission_aplikasigithubuserextended.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {

    companion object {
        fun getApiService(): ApiService {

            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).addInterceptor { chain ->
                    chain.proceed(
                        chain.request().newBuilder().also { header ->
                            header.addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
                        }.build()
                    )
                }.build()
            val retrofit = Retrofit.Builder().baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create()).client(client).build()
            return retrofit.create(ApiService::class.java)
        }
    }
}