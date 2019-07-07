package tk.jonathancowling.inventorytracker.settings

import io.reactivex.Observable

interface SettingsRepository {
    val settings: Observable<SettingsModel>

    data class SettingsModel(
        val baseUrl: String?,
        val apiKey: String?,
        val useCloudSync: Boolean
    )
}
