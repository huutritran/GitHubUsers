package com.example.githubusers.domain.usecases

import com.example.githubusers.domain.repositories.GitHubUserRepository
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.Before

abstract class UseCaseBaseTest {
    lateinit var gitHubUserRepository: GitHubUserRepository
    lateinit var ioDispatcher: CoroutineDispatcher
    lateinit var mainDispatcher: CoroutineDispatcher
    lateinit var scope: CoroutineScope


    @Before
    open fun setup() {
        gitHubUserRepository = mockk(relaxed = true)
        ioDispatcher = Dispatchers.Unconfined
        mainDispatcher = Dispatchers.Unconfined
        scope = CoroutineScope(Dispatchers.Unconfined)
    }
}