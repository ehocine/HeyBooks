package com.helic.heybooks.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.helic.heybooks.data.models.book.Book
import com.helic.heybooks.data.viewmodels.MainViewModel
import com.helic.heybooks.ui.theme.CardColor
import com.helic.heybooks.ui.theme.TextColor
import com.helic.heybooks.ui.theme.RedColor
import com.helic.heybooks.utils.AddOrRemoveBookAction

@Composable
fun ItemBookCardOfUser(
    book: Book,
    context: Context,
    mainViewModel: MainViewModel,
    onItemClicked: (book: Book) -> Unit,
    snackbar: (String, SnackbarDuration) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = { onItemClicked(book) }),
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.CardColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp)),
                alignment = Alignment.CenterStart,
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
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(
                    text = book.title,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colors.TextColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = book.authors,
                    color = MaterialTheme.colors.TextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${book.pageCount} page(s)",
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colors.TextColor,
                    style = MaterialTheme.typography.caption
                )

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                DeleteChipView(
                    onDeleteClicked = {

                        // Delete the Book from firebase
                        mainViewModel.addOrRemoveBookFromFirebase(
                            context = context,
                            snackbar = snackbar,
                            book = book,
                            action = AddOrRemoveBookAction.REMOVE
                        )

                        //Delete the Book from the user's record
                        mainViewModel.addOrRemoveBookForUser(
                            context = context,
                            snackbar = snackbar,
                            book = book,
                            action = AddOrRemoveBookAction.REMOVE
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun DeleteChipView(onDeleteClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(RedColor.copy(.08f))
            .clickable { onDeleteClicked() }
    ) {
        Icon(
            imageVector = Icons.Default.Cancel,
            contentDescription = "Delete Icon",
            tint = RedColor,
            modifier = Modifier.padding(6.dp)
        )
    }
}