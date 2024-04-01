package com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("INSERT INTO user (id, login, node_id, avatar_url, bookmarked) VALUES (:id, :login, :nodeId, :avatar, :bookmarked) ON CONFLICT (id) DO UPDATE SET bookmarked = :bookmarked WHERE id=:id")
    fun updateOrCreateUser(
        id: Int,
        login: String,
        nodeId: String,
        avatar: String?,
        bookmarked: Boolean
    )

    @Query("UPDATE user SET bookmarked=:bookmarked WHERE id = :id")
    fun updateBookmarkUser(id: Int, bookmarked: Boolean)

    @Query("delete from user")
    fun deleteAll()

    @Query("delete from user where id = :id")
    fun deleteOne(id: Int)

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id AND bookmarked = 1)")
    fun isUserBookmarked(id: Int): Boolean
}