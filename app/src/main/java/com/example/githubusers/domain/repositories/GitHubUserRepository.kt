package com.example.githubusers.domain.repositories

import com.example.githubusers.domain.models.User
import com.example.githubusers.domain.models.UserDetail


interface GitHubUserRepository {

    suspend fun searchUsers(keywords: String, page: Int): Result<List<User>>

    suspend fun getUserDetail(userName: String): Result<UserDetail>

}