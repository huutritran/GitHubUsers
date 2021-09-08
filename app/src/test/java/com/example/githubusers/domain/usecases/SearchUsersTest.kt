package com.example.githubusers.domain.usecases

import com.example.githubusers.domain.models.User
import com.example.githubusers.domain.models.UserItems
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SearchUsersTest : UseCaseBaseTest() {
    lateinit var searchUsers: SearchUsers

    @Before
    override fun setup() {
        super.setup()
        searchUsers = SearchUsers(
            mainDispatcher,
            ioDispatcher,
            gitHubUserRepository
        )
    }

    @Test
    fun `SearchUsers should return success`() {
        //given
        val searchParams = SearchUsers.Params("Test", 1)
        coEvery { gitHubUserRepository.searchUsers(any(), any()) } returns Result.success(userItems)
        var result: Result<UserItems>? = null


        //when
        runBlocking {
            searchUsers(searchParams, scope) { result = it }
        }

        //then
        coVerify(exactly = 1) {
            gitHubUserRepository.searchUsers(
                searchParams.keywords,
                searchParams.page
            )
        }
        result shouldNotBe null
        result!!.shouldBeSuccess()
        result!!.getOrNull() shouldBe userItems
    }

    @Test
    fun `SearchUsers should return Failure`() {
        //given
        coEvery {
            gitHubUserRepository.searchUsers(
                any(),
                any()
            )
        } returns Result.failure(error)
        var result: Result<UserItems>? = null
        val searchParams = SearchUsers.Params("Test", 1)

        //when
        runBlocking {
            searchUsers(searchParams, scope) { result = it }
        }

        //then
        coVerify(exactly = 1) {
            gitHubUserRepository.searchUsers(
                searchParams.keywords,
                searchParams.page
            )
        }
        result shouldNotBe null
        result!!.shouldBeFailure()
        result!!.exceptionOrNull() shouldBe error
    }

    private companion object {
        val userItems = UserItems(
            users = listOf(
                User(1, "name", "url", 0, "html")
            ),
            totalItems = 10
        )
        val error = Throwable("ERROR_MESSAGE")
    }
}