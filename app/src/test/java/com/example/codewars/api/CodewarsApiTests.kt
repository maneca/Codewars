package com.example.codewars.api

import com.example.codewars.data.remote.CodewarsApi
import com.example.codewars.data.remote.dto.ChallengeDetailsDto
import com.example.codewars.data.remote.dto.CompletedChallengeDto
import com.example.codewars.data.remote.model.ApiResponse
import com.example.codewars.utils.DEFAULT_USER
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class CodewarsApiTests {
    companion object{
        val mockCompletedChallengesDto = listOf(
            CompletedChallengeDto(
                "1", "Challenge 1", "Challenge 1", "2017-04-06T16:32:09Z", listOf(
                    "javascript",
                    "coffeescript",
                    "ruby",
                )
            ),
            CompletedChallengeDto(
                "2", "Challenge 2", "Challenge 2", "2017-04-06T16:32:09Z", listOf(
                    "javascript",
                    "coffeescript",
                    "ruby",
                )
            )
        )

        val challengeDetailsDto = ChallengeDetailsDto(
            id = "Challenge 1",
            name = "Challenge 1",
            createdBy = null,
            description = "Challenge 1",
            tags = listOf("a"),
            languages = listOf("b"),
            totalStars = 2,
            totalCompleted = 1,
            totalAttempts = 1
        )
    }


    @Test
    fun getCompletedChallengesSuccess() = runTest {

        val mockApi = mockk<CodewarsApi>()
        val mockResponse = Response.success(ApiResponse(1, 1, mockCompletedChallengesDto))
        coEvery { mockApi.getCompletedChallenges(any(), 0) } coAnswers { mockResponse }
        val result = mockApi.getCompletedChallenges(DEFAULT_USER, 0)
        Truth.assertThat(result.isSuccessful).isEqualTo(true)
        Truth.assertThat(result.body()?.data?.size).isEqualTo(2)
    }

    @Test
    fun getCompletedChallengesError() = runTest {

        val mockApi = mockk<CodewarsApi>()
        val mockResponse = Response.error<ApiResponse>(400, "".toResponseBody(null))
        coEvery { mockApi.getCompletedChallenges(any(), 0) } coAnswers { mockResponse }
        val result = mockApi.getCompletedChallenges(DEFAULT_USER, 0)
        Truth.assertThat(result.isSuccessful).isEqualTo(false)
    }

    @Test
    fun getChallengeDetailsSuccess() = runTest {

        val mockApi = mockk<CodewarsApi>()
        val mockResponse = Response.success(challengeDetailsDto)
        coEvery { mockApi.getChallengeDetails("a") } coAnswers { mockResponse }
        val result = mockApi.getChallengeDetails("a")
        Truth.assertThat(result.isSuccessful).isEqualTo(true)
        Truth.assertThat(result.body()).isEqualTo(challengeDetailsDto)
    }

    @Test
    fun getChallengeDetailsError() = runTest {

        val mockApi = mockk<CodewarsApi>()
        val mockResponse = Response.error<ChallengeDetailsDto>(400, "".toResponseBody(null))
        coEvery { mockApi.getChallengeDetails("a") } coAnswers { mockResponse }
        val result = mockApi.getChallengeDetails("a")
        Truth.assertThat(result.isSuccessful).isEqualTo(false)
    }
}