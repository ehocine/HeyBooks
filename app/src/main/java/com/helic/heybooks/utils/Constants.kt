package com.helic.heybooks.utils

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow

object Constants {

    const val TIMEOUT_IN_MILLIS = 10000L

    var loadingState = MutableStateFlow(LoadingState.IDLE)
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    const val FIRESTORE_DATABASE = "data"
    const val FIRESTORE_BOOKS_DOCUMENT = "books"
    const val FIRESTORE_USERS_DATABASE = "users"

    const val USERNAME_FIELD = "name"
    const val USER_BIO_FIELD = "bio"
    const val USER_IMAGE = "image"
    const val LIST_OF_BOOKS = "listOfBooks"

    val categoryList = listOf(
        "Action and Adventure",
        "Classics",
        "Comic Book",
        "Detective and Mystery",
        "Fantasy",
        "Romance",
        "Historical",
        "Science Fiction",
        "Thriller",
        "Biography",
        "Horror"
    )
}