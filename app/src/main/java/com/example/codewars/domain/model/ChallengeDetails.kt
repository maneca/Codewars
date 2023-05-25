package com.example.codewars.domain.model

data class ChallengeDetails(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val languages: List<String> = emptyList(),
    val totalStars: Int = 0,
    val totalCompleted: Int = 0,
    val userNameCreator: String?
)
