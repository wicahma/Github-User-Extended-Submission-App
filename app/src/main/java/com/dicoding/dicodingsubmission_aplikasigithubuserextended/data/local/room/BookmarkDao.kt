package com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity.UserBookmarkEntity

@Dao
interface BookmarkDao {
    @Query("select * from bookmark")
    fun getBookmarks(): LiveData<List<UserBookmarkEntity>>

    @Query("SELECT EXISTS(SELECT * FROM bookmark WHERE id = :id)")
    fun isUserBookmarked(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOneBookmark(bookmark: UserBookmarkEntity)

    @Query("DELETE FROM bookmark WHERE id = :id")
    fun deleteOneBookmark(id: Int)
}