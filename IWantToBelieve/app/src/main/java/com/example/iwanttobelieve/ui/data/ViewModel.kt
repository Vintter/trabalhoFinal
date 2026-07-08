package com.example.iwanttobelieve.ui.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iwanttobelieve.ui.data.repository.AuthRepository
import com.example.iwanttobelieve.ui.data.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val firestoreRepository = FirestoreRepository()

    private val _currentUser = MutableStateFlow<FirebaseUser?>(authRepository.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    init {
        observePosts()
    }

    fun register(email: String, password: String, name: String, nickname: String) {
        viewModelScope.launch {
            _authError.value = null
            val firebaseUser = authRepository.registerWithEmail(email, password)
            if (firebaseUser != null) {
                _currentUser.value = firebaseUser
                val newUser = User(uid = firebaseUser.uid, name = name, nickname = nickname, email = email)
                firestoreRepository.saveUserProfile(newUser)
            } else {
                _authError.value = "Falha no cadastro. Verifique os dados ou a senha."
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authError.value = null
            val firebaseUser = authRepository.loginWithEmail(email, password)
            if (firebaseUser != null) {
                _currentUser.value = firebaseUser
            } else {
                _authError.value = "E-mail ou senha incorretos."
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _currentUser.value = null
    }

    fun addNewPost(text: String, authorName: String, authorNickname: String, imageUrls: List<String>) {
        val currentUid = _currentUser.value?.uid ?: return
        viewModelScope.launch {
            val newPost = Post(
                userId = currentUid,
                authorName = authorName,
                authorNickname = authorNickname,
                text = text,
                timestamp = System.currentTimeMillis(),
                images = imageUrls
            )
            firestoreRepository.createPost(newPost)
        }
    }

    private fun observePosts() {
        viewModelScope.launch {
            firestoreRepository.getAllPosts().collect { postsList ->
                _posts.value = postsList
            }
        }
    }
}