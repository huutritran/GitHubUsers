package com.example.githubusers.data

import com.example.githubusers.data.entities.SearchUsersResult
import com.example.githubusers.data.entities.UserDetailEntity
import com.example.githubusers.util.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") keywords: String,
        @Query("page") pageNumber: Int,
        @Query("per_page") perPage: Int = Constants.ITEMS_PER_PAGE
    ): SearchUsersResult

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") userName: String
    ): UserDetailEntity

}