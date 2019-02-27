package tk.jonathancowling.inventorytracker.communications

import android.content.Context
import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.communications.CommunicationsChannelManager.Channel
import tk.jonathancowling.inventorytracker.communications.CommunicationsChannelManager.Channel.*

class AndroidStringFetcher(private val context: Context) {
    fun getString(channel: Channel) = context.getString(
        when (channel) {
            DEFAULT -> R.string.notification_channel_default
            ESSENTIAL -> R.string.notification_channel_essential
            REPORT -> R.string.notification_channel_report
        })
}