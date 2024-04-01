package com.dicoding.dicodingsubmission_aplikasigithubuserextended.ui

import androidx.lifecycle.ViewModel
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.UserRepository
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity.UserEntity

class BookmarkViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getBookmarkedUser() = userRepository.getBookmarkedUser()
    fun saveUser(user: UserEntity) = userRepository.setBookmarkedUser(user, true)
    fun deleteUser(user: UserEntity) = userRepository.setBookmarkedUser(user, false)
}