package com.example.codewars.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.codewars.R
import com.example.codewars.domain.model.CompletedChallenge
import com.example.codewars.presentation.CodewarsMainViewModel
import com.example.codewars.ui.components.CustomTopAppBar
import com.example.codewars.ui.components.ErrorView
import com.example.codewars.ui.components.LoadingItem
import com.example.codewars.ui.components.LoadingView
import com.example.codewars.utils.toCorrectFormat
import kotlinx.coroutines.flow.flowOf

@Composable
fun CodewarsMainScreen(
    viewModel: CodewarsMainViewModel,
    navigateToChallenge: (String) -> Unit
) {
    val completedChallenges = viewModel.completedChallenges.collectAsLazyPagingItems()

    MainContent(
        completedChallenges = completedChallenges,
        navigateToChallenge = navigateToChallenge,
        scrollState = rememberLazyListState()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    completedChallenges: LazyPagingItems<CompletedChallenge>,
    scrollState: LazyListState,
    navigateToChallenge: (String) -> Unit
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(canGoBack = false) {

            }
        }
    ) {
        CompletableChallengesLayout(
            completedChallenges = completedChallenges,
            navigateToChallenge = navigateToChallenge,
            scrollState = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        )
    }
}

@Composable
fun CompletableChallengesLayout(
    modifier: Modifier,
    scrollState: LazyListState,
    completedChallenges: LazyPagingItems<CompletedChallenge>,
    navigateToChallenge: (String) -> Unit
){
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.margin_default_2x)),
        state = scrollState
    ) {
        items(
            count = completedChallenges.itemCount,
            key = completedChallenges.itemKey(),
            contentType = completedChallenges.itemContentType(
            )
        ) { index ->
            val item = completedChallenges[index]
            Divider(
                Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color.LightGray
            )
            item?.let{ challenge ->
                CompletedChallengeDetails(challenge, navigateToChallenge)
            }
        }
        completedChallenges.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                }
                loadState.append is LoadState.Loading -> {
                    item { LoadingItem() }
                }
                loadState.refresh is LoadState.Error -> {
                    item {
                        ErrorView(
                            message = stringResource(id = R.string.something_went_wrong),
                            buttonTitle = stringResource(id = R.string.try_again),
                            modifier = Modifier.fillParentMaxSize(),
                            onClick = { retry() }
                        )
                    }
                }
                loadState.append is LoadState.Error -> {
                    item {
                        ErrorView(
                            message = stringResource(id = R.string.something_went_wrong),
                            buttonTitle = stringResource(id = R.string.try_again),
                            onClick = { retry() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CompletedChallengeDetails(
    challenge: CompletedChallenge,
    navigateToChallenge: (String) -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(dimensionResource(id = R.dimen.margin_default))
            .clickable {
                navigateToChallenge(challenge.id)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.margin_default_2x))
    ) {
        Icon(
            painterResource(id = R.drawable.ic_codewars),
            contentDescription = "",
            modifier = Modifier.size(50.dp))
        Column(
            modifier = Modifier
                .weight(3f)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.margin_default))
        ) {
            Text(text = challenge.name, modifier= Modifier
                .wrapContentHeight()
                .fillMaxWidth())
            Text(text = challenge.completedAt.toCorrectFormat(), modifier= Modifier
                .wrapContentHeight()
                .fillMaxWidth())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainContent(
        completedChallenges = flowOf(PagingData.from(listOf(CompletedChallenge(
            id = "5571d9fc11526780a000011a",
            name = "Multiples of 3 and 5",
            slug = "Multiples of 3 and 5",
            completedAt = "2017-04-06T16:32:09Z",
            completedLanguages = listOf( "javascript",
                "coffeescript",
                "ruby",
                "javascript",
                "ruby",
                "javascript",
                "ruby",
                "coffeescript",
                "javascript",
                "ruby",
                "coffeescript")

        ),
            CompletedChallenge(
                id = "5571d9fc11526780a000011a",
                name = "Multiples of 3 and 5",
                slug = "Multiples of 3 and 5",
                completedAt = "2017-04-06T16:32:09Z",
                completedLanguages = listOf( "javascript",
                    "coffeescript",
                    "ruby",
                    "javascript",
                    "ruby",
                    "javascript",
                    "ruby",
                    "coffeescript",
                    "javascript",
                    "ruby",
                    "coffeescript")

            )))).collectAsLazyPagingItems(),
        scrollState = rememberLazyListState(),
        navigateToChallenge = {})
}