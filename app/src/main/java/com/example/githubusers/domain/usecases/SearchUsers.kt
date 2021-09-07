package com.example.githubusers.domain.usecases

import com.example.githubusers.domain.models.User
import com.example.githubusers.domain.repositories.GitHubUserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

class SearchUsers(
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    coroutineScope: CoroutineScope,
    private val gitHubUserRepository: GitHubUserRepository
) : UseCase<List<User>, SearchUsers.Params>(
    mainDispatcher, ioDispatcher, coroutineScope
) {

    data class Params(
        val keywords: String,
        val page: Int,
    )

    override suspend fun run(params: Params): Result<List<User>> {
        return gitHubUserRepository.searchUsers(
            keywords = params.keywords,
            page = params.page
        )
    }
}