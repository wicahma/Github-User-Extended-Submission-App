package com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity.UserBookmarkEntity
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, UserBookmarkEntity::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        @Volatile
        private var instance: UserDatabase? = null
        fun getInstance(context: Context): UserDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java, "User.db"
                ).build()
            }
    }
}