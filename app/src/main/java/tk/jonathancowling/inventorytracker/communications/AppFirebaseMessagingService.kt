package tk.jonathancowling.inventorytracker.communications

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import tk.jonathancowling.inventorytracker.R

class AppFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        NotificationManagerCompat.from(applicationContext).notify(
            message?.notification?.tag ?: "[null]",
            message?.data?.get("id")?.let { Integer.parseInt(it) } ?: 0,
            NotificationCompat.Builder(
                applicationContext,
                (message?.data?.get("channel")?.let {
                    CommunicationsChannelManager.Channel.valueOf(it)
                } ?: CommunicationsChannelManager.Channel.DEFAULT)
                    .name
            )
                .setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT)
                .setContentTitle(message?.notification?.title)
                .setContentText(message?.notification?.body)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(0xFFFF0000.toInt())
                .build()
        )
    }
}

