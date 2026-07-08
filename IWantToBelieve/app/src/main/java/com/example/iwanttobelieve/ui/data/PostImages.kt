package com.example.iwanttobelieve.ui.data

import androidx.room.Embedded
import androidx.room.Relation

data class PostImages(
    @Embedded
    val post: Post,

    @Relation(
        parentColumn = "id",
        entityColumn = "postId"
    )
    val images: List<Image>
)