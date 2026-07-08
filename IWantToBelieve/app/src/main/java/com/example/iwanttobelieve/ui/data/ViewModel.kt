package com.example.iwanttobelieve.ui.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.iwanttobelieve.ui.data.PostDao
import com.example.iwanttobelieve.ui.data.Post
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PostViewModel(private val postDao: PostDao) : ViewModel() {

    val allPosts: StateFlow<List<Post>> = postDao.getAllPosts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertPost(text: String, authorName: String, authorNickname: String) {
        viewModelScope.launch {
            val newPost = Post(
                text = text,
                authorName = authorName,
                authorNickname = authorNickname,
                timestamp = "Agora mesmo"
            )
            postDao.insertPost(newPost)
        }
    }

    fun updatePost(post: Post) {
        viewModelScope.launch {
            postDao.updatePost(post)
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            postDao.deletePost(post)
        }
    }
}


class PostViewModelFactory(private val postDao: PostDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostViewModel(postDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}