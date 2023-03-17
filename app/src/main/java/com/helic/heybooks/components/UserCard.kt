package com.helic.heybooks.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.helic.heybooks.R
import com.helic.heybooks.data.models.book.Book
import com.helic.heybooks.data.models.user.User
import com.helic.heybooks.ui.theme.ButtonColor
import com.helic.heybooks.ui.theme.ButtonTextColor
import com.helic.heybooks.ui.theme.TextColor
import com.helic.heybooks.utils.sendEmailToOwner


@Composable
fun UserCard(
    context: Context,
    user: User,
    book: Book,
    snackbar: (String, SnackbarDuration) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        SubcomposeAsyncImage(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(user.image)
                .crossfade(true)
                .error(R.drawable.account)
                .build(),
            contentDescription = "User Image"
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

        Column(modifier = Modifier) {
            Text(
                text = user.name,
                color = MaterialTheme.colors.TextColor,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.W600,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = user.bio,
                color = MaterialTheme.colors.TextColor,
                style = MaterialTheme.typography.caption,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            FloatingActionButton(
                modifier = Modifier.size(40.dp),
                onClick = {
                    sendEmailToOwner(
                        context = context,
                        emailAddress = user.email,
                        subject = "Requesting info about ${book.title}",
                        message = "Hey ${user.name}, I would like to request more info about ${book.title}.",
                        snackbar = snackbar
                    )
                },
                backgroundColor = MaterialTheme.colors.ButtonColor
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Default.Message,
                    contentDescription = "",
                    tint = MaterialTheme.colors.ButtonTextColor
                )
            }
        }
    }
}