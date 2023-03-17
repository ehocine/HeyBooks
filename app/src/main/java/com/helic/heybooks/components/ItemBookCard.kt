package com.helic.heybooks.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.helic.heybooks.data.models.book.Book
import com.helic.heybooks.data.models.user.User
import com.helic.heybooks.ui.theme.CardColor
import com.helic.heybooks.ui.theme.TextColor

@Composable
fun ItemBookCard(book: Book, onItemClicked: (book: Book) -> Unit) {

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
                    .height(150.dp)
                    .weight(3f)
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

            Column(modifier = Modifier.align(Alignment.CenterVertically).weight(7f)) {
                Text(
                    text = book.title,
                    color = MaterialTheme.colors.TextColor,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = typography.h6
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = book.authors,
                    color = MaterialTheme.colors.TextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = typography.body2
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${book.pageCount} page(s)",
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colors.TextColor,
                    style = typography.caption
                )

                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    items(book.categories) { category ->
                        CategoryTag(category, modifier = Modifier.padding(end = 5.dp))
                    }

                }
            }
        }
    }
}