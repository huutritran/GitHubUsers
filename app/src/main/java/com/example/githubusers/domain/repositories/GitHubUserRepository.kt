package com.example.githubusers.domain.repositories

import com.example.githubusers.domain.models.UserDetail
import com.example.githubusers.domain.models.UserItems


interface GitHubUserRepository {

    suspend fun searchUsers(keywords: String, page: Int): Result<UserItems>

    suspend fun getUserDetail(userName: String): Result<UserDetail>

}