package com.example.iwanttobelieve.ui.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val authorName: String,
    val authorNickname: String,
    val text: String,
    val timestamp: String
)