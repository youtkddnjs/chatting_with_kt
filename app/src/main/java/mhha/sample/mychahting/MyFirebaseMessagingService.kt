package mhha.sample.mychahting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // Create the NotificationChannel
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(getString(R.string.default_notification_channel_id), name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)

        val body = message.notification?.body ?: ""
        val notifiBuilder = NotificationCompat.Builder(applicationContext,getString(R.string.default_notification_channel_id))
            .setSmallIcon(R.drawable.ic_baseline_chat_24)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(body)
        notificationManager.notify(0, notifiBuilder.build())
    }//override fun onMessageReceived(message: RemoteMessage)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }//override fun onNewToken(token: String)

}//class MyFirebaseMessagingService: FirebaseMessagingService()q