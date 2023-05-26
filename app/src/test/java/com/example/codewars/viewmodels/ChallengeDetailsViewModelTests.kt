package com.example.codewars.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.codewars.domain.model.ChallengeDetails
import com.example.codewars.domain.repository.CodewarsRepository
import com.example.codewars.presentation.ChallengeDetailsViewModel
import com.example.codewars.utils.ARGUMENT_KEY
import com.example.codewars.utils.CustomExceptions
import com.example.codewars.utils.DispatcherProvider
import com.example.codewars.utils.MainCoroutineRule
import com.example.codewars.utils.Resource
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChallengeDetailsViewModelTests {
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var mockRepo: CodewarsRepository

    private lateinit var viewModel: ChallengeDetailsViewModel

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testDispatcherProvider = object : DispatcherProvider {
        override fun default(): CoroutineDispatcher = testDispatcher
        override fun io(): CoroutineDispatcher = testDispatcher
        override fun main(): CoroutineDispatcher = testDispatcher
        override fun unconfined(): CoroutineDispatcher = testDispatcher

    }

    companion object{

        val challengeDetails = ChallengeDetails(
            id = "Challenge 1",
            name = "Challenge 1",
            userNameCreator = null,
            description = "Challenge 1",
            languages = listOf("b"),
            totalStars = 2,
            totalCompleted = 1
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `creation of the view model, successful`() = runTest (testDispatcher){

        val expectedChallengeId = "56d8ae9237123036d3001b54"
        val savedStateHandle = SavedStateHandle().apply {
            set(ARGUMENT_KEY, expectedChallengeId)
        }

        Truth.assertThat(mockRepo).isNotNull()

        coEvery { mockRepo.getChallengeDetails(any()) } returns flowOf(Resource.Success(
            challengeDetails))
        viewModel = ChallengeDetailsViewModel(savedStateHandle, testDispatcherProvider, mockRepo)

        viewModel.state.test{
            val emission = awaitItem()
            Truth.assertThat(emission.challengeDetails).isEqualTo(challengeDetails)
        }
    }

    @Test
    fun `creation of the view model, error`() = runTest (testDispatcher){

        val expectedChallengeId = null
        val savedStateHandle = SavedStateHandle().apply {
            set(ARGUMENT_KEY, expectedChallengeId)
        }

        Truth.assertThat(mockRepo).isNotNull()
        coEvery { mockRepo.getChallengeDetails(any()) } returns flowOf(Resource.Error(
            exception = CustomExceptions.NoInternetConnectionException(),
            data = null
        ))
        viewModel = ChallengeDetailsViewModel(savedStateHandle, testDispatcherProvider, mockRepo)

        viewModel.state.test{
            val emission = awaitItem()
            Truth.assertThat(emission.challengeDetails).isEqualTo(null)
        }
    }
}