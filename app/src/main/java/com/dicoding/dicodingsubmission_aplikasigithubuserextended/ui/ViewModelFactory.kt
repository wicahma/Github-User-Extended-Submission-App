package com.dicoding.dicodingsubmission_aplikasigithubuserextended.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.UserRepository
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.di.Injection
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.utils.SettingPreferences

class ViewModelFactory private constructor(private val userRepository: UserRepository, private val pref: SettingPreferences) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) -> MainViewModel(userRepository, pref)
                isAssignableFrom(UserDetailViewModel::class.java) -> UserDetailViewModel(userRepository)
                isAssignableFrom(UserFollowViewModel::class.java) -> UserFollowViewModel(userRepository)
                isAssignableFrom(BookmarkViewModel::class.java) -> BookmarkViewModel(userRepository)

                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
    companion object : ViewModelProvider.Factory {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, pref: SettingPreferences): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), pref)
            }.also { instance = it }
    }
}