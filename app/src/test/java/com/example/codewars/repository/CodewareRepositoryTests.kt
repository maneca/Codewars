package com.example.codewars.repository

import com.example.codewars.data.local.CodewarsDatabase
import com.example.codewars.data.remote.CodewarsApi
import com.example.codewars.data.remote.dto.ChallengeDetailsDto
import com.example.codewars.data.repository.CodewarsRepositoryImp
import com.example.codewars.domain.repository.CodewarsRepository
import com.example.codewars.utils.Resource
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class CodewareRepositoryTests {
    @MockK
    private lateinit var mockApi: CodewarsApi

    @MockK(relaxUnitFun = true)
    private lateinit var mockDatabase: CodewarsDatabase

    private lateinit var repository: CodewarsRepository

    companion object{
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

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = CodewarsRepositoryImp(api = mockApi, database = mockDatabase)
    }

    @Test
    fun getChallengeDetailsSuccessful() = runBlocking {
        Assert.assertNotNull(mockApi)
        val mockResponse = Response.success(challengeDetailsDto)
        coEvery { mockApi.getChallengeDetails(any()) } coAnswers { mockResponse }

        val result = repository.getChallengeDetails("a").toList()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(challengeDetailsDto.name, result[0].data?.name)
    }

    @Test
    fun getCompanyInfoException() = runBlocking {
        Assert.assertNotNull(mockApi)
        coEvery { mockApi.getChallengeDetails(any()) } throws IOException()

        val result = repository.getChallengeDetails("a").toList()

        Truth.assertThat((result[0] as Resource.Error).data).isNull()
        Truth.assertThat((result[0] as Resource.Error).exception).isNotNull()
    }
}