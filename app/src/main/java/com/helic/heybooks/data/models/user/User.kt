package com.helic.heybooks.data.models.user

import com.helic.heybooks.data.models.book.Book

data class User(
    var userID: String = "",
    var name: String = "",
    var email: String = "",
    var bio: String = "",
    var image: String = "",
    var listOfBooks: List<Book> = listOf(),
)
