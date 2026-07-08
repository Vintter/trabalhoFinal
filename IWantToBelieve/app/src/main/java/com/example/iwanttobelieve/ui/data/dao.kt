package com.example.iwanttobelieve.ui.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAllPosts(): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE id = :id")
    suspend fun getPostById(id: Int): Post?

    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAllPostsWithImages(): Flow<List<PostImages>>

    @Insert
    suspend fun insertPost(post: Post): Long

    @Insert
    suspend fun insertImages(images: List<Image>)

    @Update
    suspend fun updatePost(post: Post)

    @Delete
    suspend fun deletePost(post: Post)
}