package com.example.codewars.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codewars.R
import com.example.codewars.ui.theme.PurpleGrey40
import com.example.codewars.ui.theme.PurpleGrey80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    canGoBack: Boolean,
    onClick: () -> Unit){
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = PurpleGrey80),
        navigationIcon = {
            if (canGoBack) {
                    IconButton(onClick = { onClick() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

            }
        }

    )
}

@Composable
fun LoadingView(modifier: Modifier){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        CircularProgressIndicator(color = Black)
    }
}

@Composable
fun LoadingItem(progressColor: Color = PurpleGrey80) {
    CircularProgressIndicator(
        color = progressColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.margin_default))
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun ErrorView(
    message: String,
    buttonTitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.margin_default))
    ) {
        Button(onClick = onClick) {
            Text(text = buttonTitle)
        }
        Text(
            text = message,
            maxLines = 1,
            color = Black
        )
    }
}


@Composable
fun SectionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Black, RectangleShape)
    ) {
        Text(
            title.uppercase(),
            fontSize = 20.sp,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_default_2x)),
            color = Color.White
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LanguagesFlow(languages: List<String>) {
    FlowRow(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.margin_default), Alignment.Start),
        verticalAlignment = Alignment.Top
    )
    {
        languages.forEach {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(dimensionResource(id = R.dimen.margin_default))
                    .border(
                        dimensionResource(id = R.dimen.border_width),
                        shape = RoundedCornerShape(12.dp),
                        color = PurpleGrey40
                    )
            ) {
                Text(
                    text = it,
                    color = PurpleGrey40,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_default_2x))
                )
            }
        }
    }
}

@Composable
fun IconText(
    icon: ImageVector,
    value: String
) {
    Row(modifier = Modifier
        .wrapContentSize()
        .padding(dimensionResource(id = R.dimen.margin_default)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.margin_default)),
    verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = "", modifier = Modifier.size(30.dp))
        Text(value, fontSize = 15.sp, modifier = Modifier
            .wrapContentSize())
    }
}

@Preview
@Composable
fun ComponentsPreview(){
    IconText(icon = Icons.Default.EmojiEvents, value = "4")
}