package com.example.codewars.data.remote.dto

import com.example.codewars.domain.model.ChallengeDetails

data class ChallengeDetailsDto(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val languages: List<String> = listOf(),
    val tags: List<String> = listOf(),
    val createdBy: User? = null,
    val totalAttempts: Int = 0,
    val totalCompleted: Int = 0,
    val totalStars: Int = 0,
){
    fun toChallengeDetails(): ChallengeDetails{
        return ChallengeDetails(
            id, name, description, languages, totalStars, totalCompleted, createdBy?.username
        )
    }
}

data class User(
    val username: String,
    val url: String
)
