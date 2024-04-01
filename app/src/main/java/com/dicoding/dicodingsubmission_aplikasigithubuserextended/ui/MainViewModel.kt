package com.dicoding.dicodingsubmission_aplikasigithubuserextended.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.UserRepository
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity.UserEntity
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.utils.SettingPreferences
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val pref: SettingPreferences
) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getAllUser() = userRepository.getAllUser()
    fun saveUser(user: UserEntity) = userRepository.setBookmarkedUser(user, true)
    fun deleteUser(user: UserEntity) = userRepository.setBookmarkedUser(user, false)
    fun searchUser(username: String) = userRepository.searchUser(username)
}