package com.example.iwanttobelieve.ui.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Image(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val postId: Int,
    val imageUri: String
)
