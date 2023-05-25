package com.example.codewars.domain.repository

import androidx.paging.PagingData
import com.example.codewars.domain.model.ChallengeDetails
import com.example.codewars.domain.model.CompletedChallenge
import com.example.codewars.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CodewarsRepository {

    fun getCompletedChallenges(): Flow<PagingData<CompletedChallenge>>

    suspend fun getChallengeDetails(id: String): Flow<Resource<ChallengeDetails>>
}