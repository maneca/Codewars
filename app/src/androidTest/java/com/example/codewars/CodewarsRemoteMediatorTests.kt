package com.example.codewars

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.codewars.data.local.CodewarsDatabase
import com.example.codewars.data.local.entity.CompletedChallengeEntity
import com.example.codewars.data.remote.CodewarsApi
import com.example.codewars.data.remote.dto.CompletedChallengeDto
import com.example.codewars.data.remote.mediator.CodewarsRemoteMediator
import com.example.codewars.data.remote.model.ApiResponse
import com.example.codewars.utils.DEFAULT_USER
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk

import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response
import java.io.IOException

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class CodewarsRemoteMediatorTests {
    private val mockCompletedChallengesDto = listOf(
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
        ),
        CompletedChallengeDto(
            "3", "Challenge 3", "Challenge 3", "2017-04-06T16:32:09Z", listOf(
                "javascript",
                "coffeescript",
                "ruby",
            )
        ),
        CompletedChallengeDto(
            "4", "Challenge 4", "Challenge 4", "2017-04-06T16:32:09Z", listOf(
                "javascript",
                "coffeescript",
                "ruby",
            )
        ),
        CompletedChallengeDto(
            "5", "Challenge 5", "Challenge 5", "2017-04-06T16:32:09Z", listOf(
                "javascript",
                "coffeescript",
                "ruby",
            )
        )
    )

    private lateinit var mockDb : CodewarsDatabase
    private val mockApi = mockk<CodewarsApi>()

    @Before
    fun setup() {
        mockDb = CodewarsDatabase.invoke(ApplicationProvider.getApplicationContext())
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }


    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runBlocking {
        val mockResponse = Response.success(ApiResponse(totalPages = 1, totalItems = 5, data= mockCompletedChallengesDto))
        val remoteMediator = CodewarsRemoteMediator(api= mockApi, database = mockDb)
        coEvery { mockApi.getCompletedChallenges(DEFAULT_USER, 0) } coAnswers { mockResponse }

        val pagingState = PagingState<Int, CompletedChallengeEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        Truth.assertThat(result is RemoteMediator.MediatorResult.Success).isTrue()
        Truth.assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isFalse()
    }

    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runBlocking {
        val mockResponse = Response.success(ApiResponse(totalPages = 1, totalItems = 0, data= listOf()))
        val remoteMediator = CodewarsRemoteMediator(api= mockApi, database = mockDb)
        coEvery { mockApi.getCompletedChallenges(DEFAULT_USER, 0) } coAnswers { mockResponse }

        val pagingState = PagingState<Int, CompletedChallengeEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        Truth.assertThat(result is RemoteMediator.MediatorResult.Success).isTrue()
        Truth.assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isTrue()
    }

    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runBlocking {
        coEvery { mockApi.getCompletedChallenges(DEFAULT_USER, 0) } throws IOException()
        val remoteMediator = CodewarsRemoteMediator(api= mockApi, database = mockDb)
        val pagingState = PagingState<Int, CompletedChallengeEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        Truth.assertThat(result is RemoteMediator.MediatorResult.Error).isTrue()
    }

}