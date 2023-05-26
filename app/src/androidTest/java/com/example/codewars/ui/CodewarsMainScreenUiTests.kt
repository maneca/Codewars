package com.example.codewars.ui

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.codewars.domain.model.CompletedChallenge
import com.example.codewars.ui.screens.CompletedChallengeDetails
import com.example.codewars.utils.toCorrectFormat
import org.junit.Rule
import org.junit.Test

class CodewarsMainScreenUiTests {

    @Rule
    @JvmField
    var composeTestRule: ComposeContentTestRule = createComposeRule()

    private val completedChallenge = CompletedChallenge(
        id = "Challenge 1",
        name = "Challenge 1",
        slug = "",
        completedAt = "2021-10-27T09:21:44.135Z",
        completedLanguages = listOf()
    )

    @Test
    fun completedChallengeDetails() {
        composeTestRule.setContent {
            CompletedChallengeDetails(
                challenge = completedChallenge,
                navigateToChallenge = {}
            )
        }

        composeTestRule.onNodeWithText(completedChallenge.name).assertExists()
        composeTestRule.onNodeWithText(completedChallenge.completedAt.toCorrectFormat()).assertExists()
    }
}