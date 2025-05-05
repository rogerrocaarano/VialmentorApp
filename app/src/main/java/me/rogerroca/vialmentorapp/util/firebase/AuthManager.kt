package me.rogerroca.vialmentorapp.util.firebase

import com.google.firebase.auth.FirebaseAuth

class AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signInAnonymously(onResult: (Boolean, String?) -> Unit) {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    onResult(true, uid)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun getIdToken(onResult: (Boolean, String?) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentUser.getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result?.token
                        onResult(true, idToken)
                    } else {
                        onResult(false, task.exception?.message)
                    }
                }
        } else {
            onResult(false, "User not authenticated")
        }
    }
}
