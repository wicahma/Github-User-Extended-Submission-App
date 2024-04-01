package com.dicoding.dicodingsubmission_aplikasigithubuserextended.ui

import androidx.lifecycle.ViewModel
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.UserRepository
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity.UserEntity

class UserFollowViewModel(private val userRepository: UserRepository):ViewModel() {

    companion object {
        private const val TAG = "UserFollowViewModel"
    }

    fun getListFollow(username: String, fgmName: String) = userRepository.getListUserFollow(username, fgmName)
    fun saveUser(user: UserEntity) = userRepository.setBookmarkedUser(user, true)
    fun deleteUser(user: UserEntity) = userRepository.setBookmarkedUser(user, false)
}