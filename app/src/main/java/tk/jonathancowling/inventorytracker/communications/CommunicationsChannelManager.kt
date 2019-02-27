package tk.jonathancowling.inventorytracker.communications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessaging

class CommunicationsChannelManager(
    private val notificationManager: NotificationManagerCompat,
    private val stringFetcher: (Channel)->String) {

    @SuppressLint("NewApi")
    fun subscribe(vararg channels: Channel) {
        channels.forEach {
            notificationManager
                .createNotificationChannel(
                    NotificationChannel(
                        it.name,
                        stringFetcher(it),
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                )

            FirebaseMessaging.getInstance().subscribeToTopic(it.name)
        }
    }

    fun unsubscribe(vararg channels: Channel) {
        channels.forEach {
            notificationManager.deleteNotificationChannel(it.name)
            FirebaseMessaging.getInstance().unsubscribeFromTopic(it.name)
        }
    }

    enum class Channel {
        DEFAULT,
        ESSENTIAL,
        REPORT
    }

}
