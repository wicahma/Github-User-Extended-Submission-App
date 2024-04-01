package com.dicoding.dicodingsubmission_aplikasigithubuserextended.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity.UserBookmarkEntity
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity.UserEntity
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.room.BookmarkDao
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.room.UserDao
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.remote.response.User
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.remote.response.UserDetailResponse
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.remote.response.UserSearchResponse
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.remote.retrofit.ApiService
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val bookmarkDao: BookmarkDao,
    private val appExecutors: AppExecutors
) {

    fun getAllUser(): LiveData<Result<List<UserEntity>>> {
        val result = MediatorLiveData<Result<List<UserEntity>>>()

        result.value = Result.Loading
        val client = apiService.getAllUsers()
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    val newsList = ArrayList<UserEntity>()
                    appExecutors.diskIO.execute {
                        users?.forEach { user ->
                            val isBookmarked = user.id?.let { bookmarkDao.isUserBookmarked(it) }
                            val news = UserEntity(
                                user.id!!,
                                user.login!!,
                                null,
                                user.nodeId!!,
                                user.avatarUrl!!,
                                null,
                                null,
                                null,
                                isBookmarked!!
                            )
                            newsList.add(news)
                        }
                        userDao.deleteAll()
                        userDao.insertUser(newsList)
                    }
                    val localData = userDao.getUser()
                    result.addSource(localData) { newData: List<UserEntity> ->
                        result.value = Result.Success(newData)
                    }
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        return result
    }

    fun searchUser(username: String): LiveData<Result<List<UserEntity>>> {
        val result = MediatorLiveData<Result<List<UserEntity>>>()
        result.value = Result.Loading
        val client = apiService.searchUsers(username)
        client.enqueue(object : Callback<UserSearchResponse> {
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>
            ) {
                if (response.isSuccessful) {
                    val users = response.body()?.items
                    val userList = ArrayList<UserEntity>()
                    appExecutors.diskIO.execute {
                        users?.forEach { user ->
                            val isBookmarked = user?.id?.let { bookmarkDao.isUserBookmarked(it) }
                            val userData = UserEntity(
                                user?.id!!,
                                user.login!!,
                                null,
                                user.nodeId!!,
                                user.avatarUrl!!,
                                null,
                                null,
                                null,
                                isBookmarked!!
                            )
                            userList.add(userData)
                        }
                        userDao.deleteAll()
                        userDao.insertUser(userList)
                    }
                    result.value = Result.Success(userList)
                }
            }

            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        return result
    }

    fun getUserData(username: String): LiveData<Result<UserEntity>> {
        val result = MediatorLiveData<Result<UserEntity>>()
        result.value = Result.Loading
        val client = apiService.getDetailUser(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    appExecutors.diskIO.execute {
                        val userData = response.body()
                        val isBookmarked = bookmarkDao.isUserBookmarked(userData?.id ?: 0)
                        val user = UserEntity(
                            userData?.id!!,
                            userData.login,
                            userData.name,
                            userData.nodeId,
                            userData.avatarUrl,
                            userData.publicRepos,
                            userData.followers,
                            userData.following,
                            isBookmarked
                        )
                        userDao.deleteOne(userData.id)
                        userDao.insertOneUser(user)
                    }
                    val localData = userDao.getOneUser(username)
                    result.addSource(localData) {
                        result.value = Result.Success(it)
                    }
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        return result
    }

    fun getListUserFollow(username: String, fgmName: String): LiveData<Result<List<UserEntity>>> {
        val result = MediatorLiveData<Result<List<UserEntity>>>()
        result.value = Result.Loading
        val client = if (fgmName == "following") apiService
            .getUserFollowing(username) else apiService.getUserFollowers(username)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    val userList = ArrayList<UserEntity>()

                    users?.forEach { user ->
                        val userData = UserEntity(
                            user.id!!,
                            user.login!!,
                            null,
                            user.nodeId!!,
                            user.avatarUrl!!,
                            null,
                            null,
                            null,
                            isBookmarked = false
                        )
                        userList.add(userData)
                    }
                    result.value = Result.Success(userList)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        return result
    }

    fun getBookmarkedUser(): LiveData<List<UserBookmarkEntity>> {
        return bookmarkDao.getBookmarks()
    }

    fun setBookmarkedUser(user: UserBookmarkEntity, bookmarkState: Boolean) {
        appExecutors.diskIO.execute {
            user.isBookmarked = bookmarkState
            userDao.updateBookmarkUser(user.id, bookmarkState)
            if (bookmarkState) {
                bookmarkDao.insertOneBookmark(user)
            } else {
                bookmarkDao.deleteOneBookmark(user.id)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
            bookmarkDao: BookmarkDao,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao, bookmarkDao, appExecutors)
            }.also { instance = it }
    }
}