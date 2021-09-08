package com.example.githubusers.data

import com.example.githubusers.data.entities.toUserDetailModel
import com.example.githubusers.data.entities.toUserItemsModel
import com.example.githubusers.domain.models.UserDetail
import com.example.githubusers.domain.models.UserItems
import com.example.githubusers.domain.repositories.GitHubUserRepository
import javax.inject.Inject


class GitHubRepositoryImpl @Inject constructor(
    private val api: GitHubApi
) : GitHubUserRepository {

    override suspend fun searchUsers(keywords: String, page: Int): Result<UserItems> {
        val searchKey = "${keywords} in:login:"
        return kotlin.runCatching {
            api.searchUsers(searchKey, page).toUserItemsModel().let {
                Result.success(it)
            }
        }.getOrElse {
            Result.failure(it)
        }
    }

    override suspend fun getUserDetail(userName: String): Result<UserDetail> {
        return kotlin.runCatching {
            api.getUserDetail(userName).toUserDetailModel().let {
                Result.success(it)
            }
        }.getOrElse {
            Result.failure(it)
        }
    }
}