package com.example.codewars.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.codewars.R
import com.example.codewars.domain.model.ChallengeDetails
import com.example.codewars.presentation.ChallengeDetailsViewModel
import com.example.codewars.presentation.states.UiEvent
import com.example.codewars.ui.components.CustomTopAppBar
import com.example.codewars.ui.components.IconText
import com.example.codewars.ui.components.LanguagesFlow
import com.example.codewars.ui.components.SectionHeader
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChallengeDetailsScreen(
    viewModel: ChallengeDetailsViewModel,
    returnToMainScreen: () -> Unit
) {

    val state = viewModel.state.collectAsStateWithLifecycle()
    val isLoading = remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.Loading -> {
                    isLoading.value = true
                }

                is UiEvent.DetailsLoaded -> {
                    isLoading.value = false
                }

                is UiEvent.Failed -> {
                    isLoading.value = false
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.something_went_wrong),
                        duration = SnackbarDuration.Short
                    )
                }

                is UiEvent.NoInternetConnection -> {
                    isLoading.value = false
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.no_internet),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
    ChallengeContents(
        snackbarHostState = snackbarHostState,
        returnToMainScreen = returnToMainScreen,
        challengeDetails = state.value.challengeDetails)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeContents(
    snackbarHostState: SnackbarHostState,
    returnToMainScreen: () -> Unit,
    challengeDetails: ChallengeDetails?
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(canGoBack = true) {
                returnToMainScreen()
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.margin_default_2x))
        ) {
            challengeDetails?.let { challenge ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(dimensionResource(id = R.dimen.margin_default_2x)),
                    fontSize = 40.sp,
                    text = challenge.name,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconText(icon = Icons.Default.StarBorder, value = "${challenge.totalStars}")
                    IconText(icon = Icons.Default.EmojiEvents, value = "${challenge.totalCompleted}")
                    challenge.userNameCreator?.let {username ->
                        IconText(icon = Icons.Default.PersonPin, value = username)
                    }
                }
                SectionHeader(stringResource(R.string.description))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(dimensionResource(id = R.dimen.margin_default_2x)),
                    fontSize = 18.sp,
                    lineHeight = 25.sp,
                    text = challenge.description
                )
                SectionHeader(stringResource(R.string.languages))
                LanguagesFlow(languages = challenge.languages)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ChallengeDetailsPreview() {
    ChallengeContents(
        snackbarHostState = SnackbarHostState(),
        returnToMainScreen = {},
        challengeDetails = ChallengeDetails(
            name = "Valid Braces",
            id = "5277c8a221e209d3f6000b56",
            description = "Write a function called `validBraces` that takes a string ...",
            languages = listOf(
                "javascript",
                "coffeescript",
                "ruby",
                "javascript",
                "ruby",
                "javascript",
                "ruby",
                "coffeescript",
                "javascript",
                "ruby",
                "coffeescript"
            ),
            totalCompleted = 100,
            totalStars = 40,
            userNameCreator = "xDranik"
        ))
}
