package com.dicoding.dicodingsubmission_aplikasigithubuserextended.ui

import androidx.lifecycle.ViewModel
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.UserRepository
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity.UserEntity

class UserFollowViewModel(private val userRepository: UserRepository):ViewModel() {

    fun getListFollow(username: String, fgmName: String) = userRepository.getListUserFollow(username, fgmName)
}