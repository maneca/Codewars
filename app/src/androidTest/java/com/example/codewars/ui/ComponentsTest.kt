package com.example.codewars.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.codewars.ui.components.ErrorView
import com.example.codewars.ui.components.LanguagesFlow
import com.example.codewars.ui.components.SectionHeader
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class ComponentsTest {

    @Rule
    @JvmField
    var composeTestRule: ComposeContentTestRule = createComposeRule()

    private var count = 0

    @Test
    fun errorViewContent(){

        composeTestRule.setContent {
            ErrorView("Erro", buttonTitle = "Retry", modifier = Modifier, onClick = { count++ })
        }

        composeTestRule.onNodeWithText("Erro").assertExists()
        composeTestRule.onNodeWithText("Retry").assertExists()
        composeTestRule.onNodeWithText("Retry").performClick()
        Assert.assertEquals(1, count)
    }

    @Test
    fun sectionHeaderContent(){

        composeTestRule.setContent {
            SectionHeader("Description")
        }

        composeTestRule.onNodeWithText("DESCRIPTION").assertExists()
    }

    @Test
    fun languageFlowContent(){
        val languages = listOf("java", "c", "lisp", "kotlin")
        composeTestRule.setContent {
            LanguagesFlow(languages)
        }

        languages.forEach {language ->
            composeTestRule.onNodeWithText(language).assertExists()
        }

    }
}