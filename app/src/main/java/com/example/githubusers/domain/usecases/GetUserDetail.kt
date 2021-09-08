package com.example.githubusers.domain.usecases

import com.example.githubusers.di.IODispatcher
import com.example.githubusers.di.MainDispatcher
import com.example.githubusers.domain.models.UserDetail
import com.example.githubusers.domain.repositories.GitHubUserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GetUserDetail @Inject constructor(
    @MainDispatcher mainDispatcher: CoroutineDispatcher,
    @IODispatcher ioDispatcher: CoroutineDispatcher,
    private val gitHubUserRepository: GitHubUserRepository
) : UseCase<UserDetail, GetUserDetail.Params>(mainDispatcher, ioDispatcher) {

    override suspend fun run(params: Params): Result<UserDetail> {
        return gitHubUserRepository.getUserDetail(userName = params.userName)
    }

    data class Params(
        val userName: String
    )
}