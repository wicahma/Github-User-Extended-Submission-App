package com.dicoding.dicodingsubmission_aplikasigithubuserextended.ui

import androidx.lifecycle.ViewModel
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.UserRepository
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity.UserBookmarkEntity

class BookmarkViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getBookmarkedUser() = userRepository.getBookmarkedUser()
    fun saveUser(user: UserBookmarkEntity) = userRepository.setBookmarkedUser(user, true)
    fun deleteUser(user: UserBookmarkEntity) = userRepository.setBookmarkedUser(user, false)
}