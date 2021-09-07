package com.example.githubusers.domain.usecases

import com.example.githubusers.domain.models.UserDetail
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetUserDetailTest : UseCaseBaseTest() {
    lateinit var getUserDetail: GetUserDetail

    @Before
    override fun setup() {
        super.setup()
        getUserDetail = GetUserDetail(
            mainDispatcher,
            ioDispatcher,
            gitHubUserRepository
        )
    }

    @Test
    fun `GetUserDetail should return success`() {
        //given
        coEvery { gitHubUserRepository.getUserDetail(userDetail.name) } returns Result.success(userDetail)
        var result: Result<UserDetail>? = null
        val userDetailParams = GetUserDetail.Params(userName = "userName")

        //when
        runBlocking {
            getUserDetail(userDetailParams, scope) { result = it }
        }

        //then
        coVerify(exactly = 1) {
            gitHubUserRepository.getUserDetail("userName")
        }
        result shouldNotBe null
        result!!.shouldBeSuccess()
        result!!.getOrNull() shouldBe userDetail
    }

    @Test
    fun `GetUserDetail should return Failure`() {

        //given
        coEvery {
            gitHubUserRepository.getUserDetail(userDetail.name)
        } returns Result.failure(error)
        var result: Result<UserDetail>? = null
        val userDetailParams = GetUserDetail.Params(userName = "userName")

        //when
        runBlocking {
            getUserDetail(userDetailParams, scope) { result = it }
        }

        //then
        coVerify(exactly = 1) {
            gitHubUserRepository.getUserDetail(userName = "userName")
        }
        result shouldNotBe null
        result!!.shouldBeFailure()
        result!!.exceptionOrNull() shouldBe error
    }

    private companion object {
        val userDetail = UserDetail(
            id = 1,
            name = "userName",
            displayName = "String",
            avatarUrl = "String",
            email = "String",
            bio = "String",
            publicRepos = 1,
            followers = 2,
            following = 3,
            location = "location"
        )
        val error = Throwable("ERROR_MESSAGE")
    }

}