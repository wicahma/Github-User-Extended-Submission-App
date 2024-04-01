package com.dicoding.dicodingsubmission_aplikasigithubuserextended.ui

import androidx.lifecycle.ViewModel
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.UserRepository

class UserDetailViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUserData(username: String) = userRepository.getUserData(username)
}