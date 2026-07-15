package com.example.iwanttobelieve.ui.data

import com.google.firebase.firestore.PropertyName

data class User(
    @get:PropertyName("uid") @set:PropertyName("uid") var uid: String = "",
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("nickname") @set:PropertyName("nickname") var nickname: String = "",
    @get:PropertyName("email") @set:PropertyName("email") var email: String = ""
) {
    constructor() : this("", "", "", "")
}