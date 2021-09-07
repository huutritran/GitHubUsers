package com.example.githubusers.domain.models

data class UserDetail(
    val id: Long,
    val name: String,
    val displayName: String,
    val avatarUrl: String,
    val email: String,
    val bio: String,
    val publicRepos: Int,
    val followers: Int,
    val following: Int,
    val location: String
)