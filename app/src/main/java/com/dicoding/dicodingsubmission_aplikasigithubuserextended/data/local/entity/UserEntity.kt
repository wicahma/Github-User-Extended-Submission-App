package com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class UserEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id: Int,

    @field:ColumnInfo(name = "login")
    val login: String,

    @field:ColumnInfo(name = "name")
    val name: String? = "User Name",

    @field:ColumnInfo(name = "node_id")
    val node_id: String,

    @field:ColumnInfo(name = "avatar_url", defaultValue = "https://source.boringavatars.com/beam/120?colors=264653,2a9d8f,e9c46a,f4a261,e76f51")
    val avatar_url: String?,

    @field:ColumnInfo(name = "public_repos")
    val public_repos: Int? = 0,

    @field:ColumnInfo(name = "followers")
    val followers: Int? = 0,

    @field:ColumnInfo(name = "following")
    val following: Int? = 0,

    @field:ColumnInfo(name = "bookmarked")
    var isBookmarked: Boolean
)