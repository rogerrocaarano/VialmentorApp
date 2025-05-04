package me.rogerroca.vialmentorapp.data.remote.firebase

import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import me.rogerroca.vialmentorapp.R

class MyFirebaseMessagingService : FirebaseMessagingService() {
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        // sendRegistrationToServer(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        showNotification(message.notification!!.title!!, message.notification!!.body!!)
    }

    private fun showNotification(title: String, body: String) {
        val builder = NotificationCompat.Builder(this, "FCM_CHANNEL")
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_background)

        val manager : NotificationManager = getSystemService(NotificationManager::class.java)
        manager.notify(0, builder.build())
    }
}