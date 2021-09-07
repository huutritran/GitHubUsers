package com.example.githubusers.domain.usecases

import com.example.githubusers.domain.models.UserDetail
import com.example.githubusers.domain.repositories.GitHubUserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

class GetUserDetail(
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    coroutineScope: CoroutineScope,
    private val gitHubUserRepository: GitHubUserRepository
) : UseCase<UserDetail, GetUserDetail.Params>(mainDispatcher, ioDispatcher, coroutineScope) {

    override suspend fun run(params: Params): Result<UserDetail> {
        return gitHubUserRepository.getUserDetail(userName = params.userName)
    }

    data class Params(
        val userName: String
    )
}