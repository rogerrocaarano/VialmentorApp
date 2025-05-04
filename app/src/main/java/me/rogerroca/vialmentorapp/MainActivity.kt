package me.rogerroca.vialmentorapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        signInAnonymously()

        permissionManager = PermissionManager(this)
        permissionManager.requestPermission()

        setContent {
            var showDialog by remember { mutableStateOf(false) }
            MyApp(this)
        }
    }

    private fun signInAnonymously() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-in success
                    val user = auth.currentUser
                    Log.d("MainActivity", "Signed in as: ${user?.uid}")
                } else {
                    // Sign-in failed
                    Log.e("MainActivity", "Authentication failed", task.exception)
                }
            }
    }
}