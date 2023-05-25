package com.example.codewars.data.remote

import com.example.codewars.data.remote.dto.ChallengeDetailsDto
import com.example.codewars.data.remote.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CodewarsApi {

    @GET("users/g964/code-challenges/completed")
    suspend fun getCompletedChallenges(@Query("page") page: Int): Response<ApiResponse>

    @GET("code-challenges/{challenge}")
    suspend fun getChallengeDetails(@Path("challenge") challenge : String): Response<ChallengeDetailsDto>
}