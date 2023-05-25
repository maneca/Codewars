package com.example.codewars.data.remote.dto

import com.example.codewars.data.local.entity.CompletedChallengeEntity

data class CompletedChallengeDto(
    val id : String,
    val name: String,
    val slug: String,
    val completedAt : String,
    val completedLanguages: List<String>
){
    fun toCompletedChallengeEntity(): CompletedChallengeEntity{
        return CompletedChallengeEntity(
            id, name, slug, completedAt, completedLanguages
        )
    }
}
