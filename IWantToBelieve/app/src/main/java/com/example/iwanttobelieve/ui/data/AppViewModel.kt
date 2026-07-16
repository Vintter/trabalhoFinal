package com.example.iwanttobelieve.ui.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.iwanttobelieve.ui.data.repository.AuthRepository
import com.example.iwanttobelieve.ui.data.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import kotlinx.coroutines.Job

// IMPORTS CRUTIAIS CORRIGIDOS: Necessários aqui para o funcionamento do "by mutableStateOf"
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository()
    private val firestoreRepository = FirestoreRepository()

    private val _currentUser = MutableStateFlow<FirebaseUser?>(authRepository.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile.asStateFlow()

    private var postsJob: Job? = null
    var registerName by mutableStateOf("")
    var registerNickname by mutableStateOf("")
    var registerEmail by mutableStateOf("")
    var registerPassword by mutableStateOf("")
    var registerConfirmPassword by mutableStateOf("")

    // Função para resetar os campos quando o cadastro for bem-sucedido
    fun clearRegisterForm() {
        registerName = ""
        registerNickname = ""
        registerEmail = ""
        registerPassword = ""
        registerConfirmPassword = ""
    }

    init {
        authRepository.currentUser?.let { user ->
            _currentUser.value = user
            fetchUserProfile()
            startObservingPosts()
        }
    }

    fun startObservingPosts() {
        postsJob?.cancel()

        postsJob = viewModelScope.launch {
            try {
                android.util.Log.d("AppViewModel", "Iniciando escuta de posts em tempo real...")
                firestoreRepository.getAllPosts().collect { postsList ->
                    android.util.Log.d("AppViewModel", "Posts atualizados recebidos: ${postsList.size}")
                    _posts.value = postsList
                }
            } catch (e: Exception) {
                android.util.Log.e("AppViewModel", "Erro ao observar posts: ${e.message}", e)
                _posts.value = emptyList()
            }
        }
    }

    fun fetchUserProfile() {
        val uid = _currentUser.value?.uid ?: authRepository.currentUser?.uid ?: return
        android.util.Log.d("AppViewModel", "Buscando perfil para o UID: $uid")
        viewModelScope.launch {
            try {
                val profile = firestoreRepository.getUserProfile(uid)
                if (profile != null) {
                    android.util.Log.d("AppViewModel", "Perfil encontrado: ${profile.nickname}")
                    _userProfile.value = profile
                } else {
                    android.util.Log.e("AppViewModel", "Perfil NÃO encontrado no Firestore para o UID: $uid")
                }
            } catch (e: Exception) {
                android.util.Log.e("AppViewModel", "Erro ao buscar perfil: ${e.message}")
            }
        }
    }

    fun register(email: String, password: String, name: String, nickname: String) {
        viewModelScope.launch {
            _authError.value = null
            val firebaseUser = authRepository.registerWithEmail(email, password)
            if (firebaseUser != null) {
                _currentUser.value = firebaseUser
                val newUser =
                    User(uid = firebaseUser.uid, name = name, nickname = nickname, email = email)
                try {
                    firestoreRepository.saveUserProfile(newUser)
                    _userProfile.value = newUser
                    android.util.Log.d("AppViewModel", "Perfil salvo e atualizado localmente.")
                    startObservingPosts()
                } catch (e: Exception) {
                    _authError.value = "Erro ao salvar perfil: ${e.message}"
                }
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
                fetchUserProfile()
                startObservingPosts()
            } else {
                _authError.value = "E-mail ou senha incorretos."
            }
        }
    }

    fun logout() {
        postsJob?.cancel()
        postsJob = null

        authRepository.logout()
        _currentUser.value = null
        _userProfile.value = null
        _posts.value = emptyList()
    }

    fun addNewPost(
        text: String,
        authorName: String,
        authorNickname: String,
        imageUriString: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentUid = authRepository.currentUser?.uid

        if (currentUid == null) {
            onError("Erro: Usuário não autenticado.")
            return
        }

        viewModelScope.launch {
            try {
                val imageUrls = mutableListOf<String>()

                if (!imageUriString.isNullOrEmpty()) {
                    val localUri = imageUriString.toUri()
                    val context = getApplication<Application>().applicationContext

                    val base64String = firestoreRepository.compressAndEncodeToBase64(context, localUri)
                    imageUrls.add(base64String)
                }

                val newPost = Post(
                    userId = currentUid,
                    authorName = authorName,
                    authorNickname = authorNickname,
                    text = text,
                    timestamp = System.currentTimeMillis(),
                    images = imageUrls
                )

                firestoreRepository.createPost(newPost)
                onSuccess()
            } catch (e: Exception) {
                android.util.Log.e("AppViewModel", "Erro ao criar post: ${e.message}", e)
                onError("Erro ao criar post: ${e.message}")
            }
        }
    }
}