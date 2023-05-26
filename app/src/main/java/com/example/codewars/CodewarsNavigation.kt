package com.example.codewars

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.codewars.presentation.ChallengeDetailsViewModel
import com.example.codewars.presentation.CodewarsMainViewModel
import com.example.codewars.ui.screens.ChallengeDetailsScreen
import com.example.codewars.ui.screens.CodewarsMainScreen
import com.example.codewars.utils.ARGUMENT_KEY

@Composable
fun CodewarsNavApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.MainScreen.route) {
        composable(Screen.MainScreen.route){
            BackHandler(true) {}
            val viewModel = hiltViewModel<CodewarsMainViewModel>()
            CodewarsMainScreen(
                viewModel = viewModel,
                navigateToChallenge = {
                    navController.navigate(Screen.ChallengeScreen.withArgs(it))
                }
            )
        }
        composable(
            route = Screen.ChallengeScreen.route + "/{challengeId}",
            arguments = listOf(
                navArgument(ARGUMENT_KEY) {
                    type = NavType.StringType
                }
            )
        ) {
            BackHandler(true) {}
            val viewModel = hiltViewModel<ChallengeDetailsViewModel>()
            ChallengeDetailsScreen(
                viewModel = viewModel,
                returnToMainScreen = {
                    navController.navigateUp()
                }
            )

        }


    }
}

internal sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object ChallengeScreen : Screen("challenge_screen")

    fun withArgs(vararg args: String?): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}