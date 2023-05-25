package com.example.codewars.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.codewars.domain.model.CompletedChallenge

@Entity(tableName = "completedChallenges")
data class CompletedChallengeEntity(
    @PrimaryKey val id : String,
    val name: String,
    val slug: String,
    val completedAt : String,
    val completedLanguages: List<String>
) {
    fun toCompletedChallenge(): CompletedChallenge{
        return CompletedChallenge(
            id, name, slug, completedAt, completedLanguages
        )
    }
}