package com.helic.heybooks.view.screens.main_app

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.helic.heybooks.components.BookInfoCard
import com.helic.heybooks.components.Title
import com.helic.heybooks.components.UserCard
import com.helic.heybooks.data.models.book.Book
import com.helic.heybooks.data.models.user.User
import com.helic.heybooks.data.viewmodels.MainViewModel
import com.helic.heybooks.ui.theme.BackgroundColor
import com.helic.heybooks.ui.theme.TextColor


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Details(
    navController: NavController,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    val selectedBook by mainViewModel.selectedBook
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        mainViewModel.getAddedByUserInfo(context = context, snackbar = snackbar)
    }

    val addedByUser by mainViewModel.addedByUser.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                backgroundColor = MaterialTheme.colors.BackgroundColor,
                contentColor = MaterialTheme.colors.TextColor,
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp, 24.dp)
                            .clickable {
                                navController.navigateUp()
                            },
                        tint = MaterialTheme.colors.TextColor
                    )
                },
                elevation = 0.dp
            )
        },
        content = {
            DetailsView(
                context = context,
                book = selectedBook,
                addedByUser = addedByUser,
                snackbar = snackbar
            )
        }

    )
}

@Composable
fun DetailsView(
    context: Context,
    book: Book,
    addedByUser: User,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.BackgroundColor)
    ) {

        // book Image
        item {
            book.let {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RectangleShape)
                        .height(250.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(book.thumbnailUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Book Image"
                ) {
                    val state = painter.state
                    if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colors.TextColor)
                        }
                    } else {
                        SubcomposeAsyncImageContent(
                            modifier = Modifier.clip(RectangleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                BookInfoCard(book = it)
            }
        }

        // Book about
        item {
            book.let {
                Spacer(modifier = Modifier.height(24.dp))
                Title(title = "About ".plus(book.title))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it.description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 0.dp, 16.dp, 0.dp),
                    color = MaterialTheme.colors.TextColor,
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Start
                )
            }
        }
        // Added by info
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Title(title = "Added by")
            Spacer(modifier = Modifier.height(16.dp))
            UserCard(context = context, user = addedByUser, book = book, snackbar = snackbar)
        }
    }
}


