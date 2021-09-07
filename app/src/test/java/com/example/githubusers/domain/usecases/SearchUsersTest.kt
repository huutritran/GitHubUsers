package com.example.githubusers.domain.usecases

import com.example.githubusers.domain.models.User
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
            scope,
            gitHubUserRepository
        )
    }

    @Test
    fun `SearchUsers should return success`() {
        //given
        val searchParams = SearchUsers.Params("Test", 1)
        coEvery { gitHubUserRepository.searchUsers(any(), any()) } returns Result.success(userList)
        var result: Result<List<User>>? = null


        //when
        runBlocking {
            searchUsers(searchParams) { result = it }
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
        result!!.getOrNull() shouldBe userList
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
        var result: Result<List<User>>? = null
        val searchParams = SearchUsers.Params("Test", 1)

        //when
        runBlocking {
            searchUsers(searchParams) { result = it }
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
        val userList = listOf(
            User(1, "name", "url")
        )
        val error = Throwable("ERROR_MESSAGE")
    }
}