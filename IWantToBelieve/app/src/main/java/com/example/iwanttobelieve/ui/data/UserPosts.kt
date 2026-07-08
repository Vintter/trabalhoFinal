package com.example.iwanttobelieve.ui.data;

import androidx.room.Embedded
import androidx.room.Relation

data class UserPosts(
    @Embedded
    val user: User,

    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val posts:List<Post>
)