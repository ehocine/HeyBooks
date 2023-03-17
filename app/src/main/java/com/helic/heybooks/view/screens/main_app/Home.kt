package com.helic.heybooks.view.screens.main_app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.helic.heybooks.components.ItemBookCard
import com.helic.heybooks.components.TopBar
import com.helic.heybooks.data.viewmodels.MainViewModel
import com.helic.heybooks.navigation.Screens
import com.helic.heybooks.ui.theme.BackgroundColor
import com.helic.heybooks.utils.ErrorLoadingResults
import com.helic.heybooks.utils.LoadingList
import com.helic.heybooks.utils.LoadingState
import com.helic.heybooks.utils.NoResults

@Composable
fun Home(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    val context = LocalContext.current

    val bookList by mainViewModel.booksList.collectAsState()

    val state by mainViewModel.gettingListOfBooksState.collectAsState()

    LaunchedEffect(key1 = bookList) {
        mainViewModel.getListOfBooksFromFirebase(context = context, snackbar = snackbar)
    }

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colors.BackgroundColor) {
        Column(Modifier.fillMaxSize()) {
            TopBar(
                context = context,
                navController = navController,
                mainViewModel = mainViewModel,
                snackbar = snackbar
            )
            Spacer(modifier = Modifier.height(8.dp))
            when (state) {
                LoadingState.LOADING -> LoadingList()
                LoadingState.ERROR -> ErrorLoadingResults()
                else -> {
                    if (bookList.isEmpty()) {
                        NoResults()
                    } else {
                        LazyColumn {
                            items(bookList) { animal ->
                                ItemBookCard(
                                    book = animal,
                                    onItemClicked = {
                                        mainViewModel.selectedBook.value = it
                                        navController.navigate(Screens.Details.route)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}