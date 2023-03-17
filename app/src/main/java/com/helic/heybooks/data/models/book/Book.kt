package com.helic.heybooks.data.models.book

data class Book(
    val title: String = "",
    val authors: String = "",
    val categories: List<String> = emptyList(),
    val description: String = "",
    val pageCount: Int = 0,
    var thumbnailUrl: String = "",
    val userID: String = ""
)