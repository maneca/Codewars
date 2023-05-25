package com.example.codewars.presentation.states

import com.example.codewars.domain.model.ChallengeDetails
import com.example.codewars.utils.CustomExceptions

data class ChallengeState(
    val challengeDetails : ChallengeDetails? = null,
    val exception: CustomExceptions? = null
)