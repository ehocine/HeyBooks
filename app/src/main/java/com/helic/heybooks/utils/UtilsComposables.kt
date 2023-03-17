package com.helic.heybooks.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.helic.heybooks.R
import com.helic.heybooks.ui.theme.BackgroundColor
import com.helic.heybooks.ui.theme.TextColor


@Composable
fun LoadingList() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.BackgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = MaterialTheme.colors.TextColor)
    }
}

@Composable
fun ErrorLoadingResults() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.BackgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(120.dp),
            painter = painterResource(id = R.drawable.ic_sentiment),
            contentDescription = "", tint = MaterialTheme.colors.TextColor
        )
        Text(
            text = "Error loading data",
            color = MaterialTheme.colors.TextColor,
            fontSize = MaterialTheme.typography.h6.fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun NoResults() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.BackgroundColor),
//            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(120.dp),
            painter = painterResource(id = R.drawable.ic_sentiment),
            contentDescription = "", tint = MaterialTheme.colors.TextColor
        )
        Text(
            text = "No data",
            color = MaterialTheme.colors.TextColor,
            fontSize = MaterialTheme.typography.h6.fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}