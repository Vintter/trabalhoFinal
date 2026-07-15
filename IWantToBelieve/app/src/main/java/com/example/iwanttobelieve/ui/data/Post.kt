package com.example.iwanttobelieve.ui.data


import com.google.firebase.firestore.PropertyName

data class Post(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("userId") @set:PropertyName("userId") var userId: String = "",
    @get:PropertyName("authorName") @set:PropertyName("authorName") var authorName: String = "",
    @get:PropertyName("authorNickname") @set:PropertyName("authorNickname") var authorNickname: String = "",
    @get:PropertyName("text") @set:PropertyName("text") var text: String = "",
    @get:PropertyName("timestamp") @set:PropertyName("timestamp") var timestamp: Long = 0L,
    @get:PropertyName("images") @set:PropertyName("images") var images: List<String> = emptyList()
) {
    constructor() : this("", "", "", "", "", 0L, emptyList())
}