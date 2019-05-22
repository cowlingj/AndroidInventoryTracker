package tk.jonathancowling.inventorytracker.settings

import io.reactivex.Observable
import tk.jonathancowling.inventorytracker.util.Optional

interface SettingsRepository {
    val settings: Observable<SettingsModel>

    data class SettingsModel(
        val baseUrl: Optional<String>,
        val apiKey: Optional<String>,
        val useCloudSync: Boolean
    )
}
