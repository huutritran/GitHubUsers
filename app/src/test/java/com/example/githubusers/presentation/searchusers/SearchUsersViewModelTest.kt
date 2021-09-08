package com.example.githubusers.presentation.searchusers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.githubusers.domain.models.User
import com.example.githubusers.domain.models.UserItems
import com.example.githubusers.domain.repositories.GitHubUserRepository
import com.example.githubusers.domain.usecases.SearchUsers
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchUsersViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var gitHubUserRepository: GitHubUserRepository
    lateinit var useCase: SearchUsers
    lateinit var viewModel: SearchUsersViewModel

    @Before
    fun setup() {
        gitHubUserRepository = mockk()
        useCase = SearchUsers(
            Dispatchers.Unconfined,
            Dispatchers.Unconfined,
            gitHubUserRepository
        )
        viewModel = SearchUsersViewModel(useCase)
    }

    @Test
    fun `searchUsers should get the correct value when success`() {
        //given
        val userList = listOf(User(1, "name", "avatar", 1, "html"))
        coEvery {
            useCase.run(
                SearchUsers.Params(
                    "",
                    1
                )
            )
        } returns Result.success(UserItems(users = userList, userList.size))

        val loadingObserver = mockk<Observer<Boolean>> { every { onChanged(any()) } just Runs }
        viewModel.isLoading.observeForever(loadingObserver)


        //when
        runBlocking { viewModel.searchUsers("") }


        //then
        coEvery { useCase.run(SearchUsers.Params("", 1)) }

        viewModel.users.observeForever {
            it.size shouldBeEqualComparingTo userList.size
            it[0].id shouldBe 1
            it[0].name shouldBe "name"
            it[0].avatarUrl shouldBe "avatar"
            it[0].html shouldBe "html"
        }

        viewModel.errorMessage.value shouldBe null

        viewModel.isLastPage.value shouldBe true

        viewModel.getCurrentPage() shouldBeEqualComparingTo 1

        verifySequence {
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }
    }
}