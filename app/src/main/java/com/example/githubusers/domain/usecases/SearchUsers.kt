package com.example.githubusers.domain.usecases

import com.example.githubusers.di.IODispatcher
import com.example.githubusers.di.MainDispatcher
import com.example.githubusers.domain.models.UserItems
import com.example.githubusers.domain.repositories.GitHubUserRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


class SearchUsers @Inject constructor(
    @MainDispatcher mainDispatcher: CoroutineDispatcher,
    @IODispatcher ioDispatcher: CoroutineDispatcher,
    private val gitHubUserRepository: GitHubUserRepository
) : UseCase<UserItems, SearchUsers.Params>(
    mainDispatcher, ioDispatcher
) {

    data class Params(
        val keywords: String,
        val page: Int,
    )

    override suspend fun run(params: Params): Result<UserItems> {
        return gitHubUserRepository.searchUsers(
            keywords = params.keywords,
            page = params.page
        )
    }
}