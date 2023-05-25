package com.example.codewars.domain.model

data class CompletedChallenge(
    val id : String,
    val name: String,
    val slug: String,
    val completedAt : String,
    val completedLanguages: List<String>
)

