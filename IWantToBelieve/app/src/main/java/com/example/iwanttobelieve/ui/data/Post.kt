package com.example.iwanttobelieve.ui.data

data class Post(
    val id: String = "",
    val userId: String = "",
    val authorName: String = "",
    val authorNickname: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    val images: List<String> = emptyList()
)