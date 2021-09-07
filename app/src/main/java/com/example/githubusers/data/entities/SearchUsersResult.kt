package com.example.githubusers.data.entities

import com.example.githubusers.domain.models.User
import com.example.githubusers.domain.models.UserItems
import com.google.gson.annotations.SerializedName

data class SearchUsersResult(
    @SerializedName("total_count")
    val count: Int,
    @SerializedName("items")
    val users: List<UserEntity>
)

fun SearchUsersResult.toUserItemsModel(): UserItems {
    return UserItems(
        users = users.map { it.toUserModel() },
        totalItems = count
    )
}

data class UserEntity(
    @SerializedName("id")
    val id: Long,
    @SerializedName("login")
    val login: String,
    @SerializedName("avatar_url")
    val avatar_url: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("html_url")
    val html_url: String
)

fun UserEntity.toUserModel(): User {
    return User(
        id = id,
        name = login,
        avatarUrl = avatar_url
    )
}