package com.example.codewars.ui

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.codewars.domain.model.ChallengeDetails
import com.example.codewars.ui.screens.ChallengeContents
import org.junit.Rule
import org.junit.Test

class ChallengeDetailsUiTests {

    @Rule
    @JvmField
    var composeTestRule: ComposeContentTestRule = createComposeRule()

    private val challengeDetails =  ChallengeDetails(
        id = "Challenge 1",
        name = "Challenge 1",
        userNameCreator = "username",
        description = "Challenge 1 details",
        languages = listOf("b"),
        totalStars = 2,
        totalCompleted = 1
    )

    @Test
    fun challengeContentTest(){

        composeTestRule.setContent {
            ChallengeContents(
                returnToMainScreen = {},
                challengeDetails = challengeDetails,
                isLoading = false,
                hasError = ""
            )
        }

        composeTestRule.onNodeWithText(challengeDetails.name).assertExists()
        composeTestRule.onNodeWithText(challengeDetails.description).assertExists()
        composeTestRule.onNodeWithText(challengeDetails.languages[0]).assertExists()
        challengeDetails.userNameCreator?.let { composeTestRule.onNodeWithText(it).assertExists() }
        composeTestRule.onNodeWithText("${challengeDetails.totalCompleted}").assertExists()
        composeTestRule.onNodeWithText("${challengeDetails.totalStars}").assertExists()
    }
}