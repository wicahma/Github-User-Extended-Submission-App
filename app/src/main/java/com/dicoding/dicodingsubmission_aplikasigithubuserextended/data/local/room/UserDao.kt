package com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("select * from user order by id asc")
    fun getUser(): LiveData<List<UserEntity>>

    @Query("select * from user where login = :username")
    fun getOneUser(username: String): LiveData<UserEntity>

    @Query("select * from user where bookmarked = 1")
    fun getBookmarkedUser(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOneUser(user: UserEntity)

    @Update
    fun updateUser(user: UserEntity)

    @Query("delete from user where bookmarked = 0")
    fun deleteAll()

    @Query("delete from user where id = :id")
    fun deleteOne(id: Int)

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id AND bookmarked = 1)")
    fun isUserBookmarked(id: Int): Boolean
}