package com.dicoding.dicodingsubmission_aplikasigithubuserextended.di

import android.content.Context
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.UserRepository
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.room.UserDatabase
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.remote.retrofit.ApiConfig
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService, dao, appExecutors)
    }
}