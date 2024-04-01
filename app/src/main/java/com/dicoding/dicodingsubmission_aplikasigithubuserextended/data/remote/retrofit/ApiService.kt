package com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.remote.retrofit

import com.dicoding.dicodingsubmission_aplikasigithubuserextended.BuildConfig
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.remote.response.User
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.remote.response.UserDetailResponse
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.remote.response.UserSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun searchUsers(@Query("q") username: String): Call<UserSearchResponse>

    @GET("users")
    fun getAllUsers(): Call<List<User>>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<User>>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<User>>
}