package com.helic.heybooks.view.screens.main_app

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.helic.heybooks.R
import com.helic.heybooks.components.AddCategoryTag
import com.helic.heybooks.components.Title
import com.helic.heybooks.data.models.book.Book
import com.helic.heybooks.data.viewmodels.MainViewModel
import com.helic.heybooks.navigation.Screens
import com.helic.heybooks.ui.theme.BackgroundColor
import com.helic.heybooks.ui.theme.ButtonColor
import com.helic.heybooks.ui.theme.ButtonTextColor
import com.helic.heybooks.ui.theme.TextColor
import com.helic.heybooks.utils.AddOrRemoveBookAction
import com.helic.heybooks.utils.Constants.categoryList
import com.helic.heybooks.utils.DropDownOptions
import com.helic.heybooks.utils.uploadBookPicture

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddAnimal(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit,
) {
    val context = LocalContext.current
    val currentUser by mainViewModel.userInfo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Book") },
                backgroundColor = MaterialTheme.colors.BackgroundColor,
                contentColor = MaterialTheme.colors.TextColor,
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp, 24.dp)
                            .clickable {
                                navController.navigate(Screens.Profile.route) {
                                    popUpTo(navController.graph.findStartDestination().id)
                                    launchSingleTop = true
                                }
                            },
                        tint = MaterialTheme.colors.TextColor
                    )
                },
                elevation = 0.dp
            )
        },
        content = {
            Surface(Modifier.fillMaxSize(), color = MaterialTheme.colors.BackgroundColor) {
                AnimalDetails(
                    mainViewModel = mainViewModel,
                    navController = navController,
                    userID = currentUser.userID,
                    context = context,
                    snackbar = snackbar
                )
            }
        }
    )
}

@Composable
fun AnimalDetails(
    mainViewModel: MainViewModel,
    navController: NavController,
    userID: String,
    context: Context,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    var bookTitle by remember { mutableStateOf("") }
    val bookCategories = remember { mutableStateListOf<String>() }
    var bookAuthors by remember { mutableStateOf("") }
    var bookPagesCount by remember { mutableStateOf("") }
    var bookDescription by remember { mutableStateOf("") }

    var imageUri by remember {
        mutableStateOf<Uri?>(Uri.parse(""))
    }
    var pictureChanged by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            pictureChanged = true
        } else {
            pictureChanged = false
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.BackgroundColor)
    ) {
        // Basic details
        item {
            Box(Modifier.fillMaxWidth()) {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RectangleShape)
                        .height(250.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri.toString())
                        .crossfade(true)
                        .error(R.drawable.placeholder)
                        .build(),
                    contentDescription = "Book Image"
                ) {
                    val state = painter.state
                    if (state is AsyncImagePainter.State.Loading) {
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
                IconButton(onClick = {
                    launcher.launch("image/*")
                }, Modifier.align(Alignment.BottomEnd)) {
                    Icon(imageVector = Icons.Default.Image, contentDescription = "Profile picture")
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row {
                Text(
                    text = "*",
                    color = Color.Red,
                    modifier = Modifier.padding(15.dp, 0.dp, 0.dp, 0.dp),
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.W600,
                )
                Title(title = "Book Title")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = bookTitle,
                onValueChange = { bookTitle = it },
                label = {
                    Text(
                        text = "Book Title",
                        color = MaterialTheme.colors.ButtonColor
                    )
                },
                placeholder = {
                    Text(
                        text = "Book Title",
                        color = MaterialTheme.colors.ButtonColor
                    )
                },
                maxLines = 1,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp, 16.dp, 0.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.ButtonColor
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row {
                Text(
                    text = "*",
                    color = Color.Red,
                    modifier = Modifier.padding(15.dp, 0.dp, 0.dp, 0.dp),
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.W600,
                )
                Title(title = "Book Authors")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = bookAuthors,
                onValueChange = { bookAuthors = it },
                label = {
                    Text(
                        text = "Authors",
                        color = MaterialTheme.colors.ButtonColor
                    )
                },
                placeholder = {
                    Text(
                        text = "Authors",
                        color = MaterialTheme.colors.ButtonColor
                    )
                },
                maxLines = 1,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp, 16.dp, 0.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.ButtonColor
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Title(title = "Tell us more")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = bookDescription,
                onValueChange = { bookDescription = it },
                label = {
                    Text(
                        text = "Description",
                        color = MaterialTheme.colors.ButtonColor
                    )
                },
                placeholder = {
                    Text(
                        text = "Add Description",
                        color = MaterialTheme.colors.ButtonColor
                    )
                },
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp, 16.dp, 0.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.ButtonColor
                )
            )

        }

        item {

            Spacer(modifier = Modifier.height(24.dp))
            Row {
                Text(
                    text = "*",
                    color = Color.Red,
                    modifier = Modifier.padding(15.dp, 0.dp, 0.dp, 0.dp),
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.W600,
                )
                Title(title = "Book Category")
            }
            Spacer(modifier = Modifier.height(16.dp))
            DropDownOptions(
                label = "Book Category",
                optionsList = categoryList,
                onOptionSelected = {
                    bookCategories.add(it)
                })
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                items(bookCategories) { category ->
                    AddCategoryTag(category, modifier = Modifier.padding(end = 5.dp), onDelete = {
                        bookCategories.remove(it)
                    })
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row {
                Text(
                    text = "*",
                    color = Color.Red,
                    modifier = Modifier.padding(15.dp, 0.dp, 0.dp, 0.dp),
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.W600,
                )
                Title(title = "Book Pages Count")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = bookPagesCount,
                onValueChange = { bookPagesCount = it },
                label = {
                    Text(
                        text = "Pages Count",
                        color = MaterialTheme.colors.ButtonColor
                    )
                },
                placeholder = {
                    Text(
                        text = "Pages Count",
                        color = MaterialTheme.colors.ButtonColor
                    )
                },
                maxLines = 1,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp, 16.dp, 0.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.ButtonColor
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))

        }

        item {
            Spacer(modifier = Modifier.height(36.dp))
            Button(
                onClick = {
                    if (pictureChanged && bookTitle.isNotEmpty()
                        && bookCategories.isNotEmpty()
                        && bookAuthors.isNotEmpty()
                        && bookPagesCount.isNotEmpty()
                    ) {
                        val book = Book(
                            title = bookTitle,
                            authors = bookAuthors,
                            categories = bookCategories,
                            description = bookDescription,
                            pageCount = bookPagesCount.toInt(),
                            thumbnailUrl = imageUri?.lastPathSegment.toString(),
                            userID = userID
                        )

                        mainViewModel.addOrRemoveBookFromFirebase(
                            context = context,
                            snackbar = snackbar,
                            book = book,
                            action = AddOrRemoveBookAction.ADD
                        )
                        mainViewModel.addOrRemoveBookForUser(
                            context = context,
                            snackbar = snackbar,
                            book = book,
                            action = AddOrRemoveBookAction.ADD
                        )
                        imageUri?.let { uploadBookPicture(fileUri = it, book = book) }

                        navController.navigate(Screens.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    } else {
                        snackbar("Please fill out all the required fields!", SnackbarDuration.Short)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(16.dp, 0.dp, 16.dp, 0.dp),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = MaterialTheme.colors.ButtonColor,
                    contentColor = MaterialTheme.colors.ButtonTextColor
                )
            ) {
                Text("Add")
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}