package com.example.codewars.data.remote.model

import com.example.codewars.data.remote.dto.CompletedChallengeDto

data class ApiResponse(
    val totalPages: Int,
    val totalItems: Int,
    val data: List<CompletedChallengeDto>
)
