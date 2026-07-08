package com.example.iwanttobelieve.ui.data.repository

import com.example.iwanttobelieve.ui.data.User
import com.example.iwanttobelieve.ui.data.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun saveUserProfile(user: User) {
        firestore.collection("users")
            .document(user.uid)
            .set(user)
            .await()
    }

    suspend fun createPost(post: Post) {
        val newDocumentRef = firestore.collection("posts").document()
        val postWithId = post.copy(id = newDocumentRef.id)

        newDocumentRef.set(postWithId).await()
    }

    fun getAllPosts(): Flow<List<Post>> = callbackFlow {
        val listener = firestore.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val postsList = snapshot?.toObjects(Post::class.java) ?: emptyList()
                trySend(postsList)
            }

        awaitClose { listener.remove() }
    }


    suspend fun getUserProfile(uid: String): User? {
        return try {
            val document = firestore.collection("users").document(uid).get().await()
            document.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }
}