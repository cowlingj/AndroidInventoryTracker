package tk.jonathancowling.inventorytracker.settings

import android.content.SharedPreferences
import io.reactivex.Observable

class AndroidSettingsRepository(sharedPreferences: Observable<SharedPreferences>): SettingsRepository {

    override val settings: Observable<SettingsRepository.SettingsModel> = sharedPreferences.map {
        SettingsRepository.SettingsModel(extractBaseUrl(it), extractApiKey(it), extractUseCloudSync(it))
    }

    private fun extractBaseUrl(preferences: SharedPreferences): String? =
        preferences.getString("baseUrl", null)?.let {
            if (it.last() == '/') it else "$it/"
        }

    private fun extractApiKey(preferences: SharedPreferences) =
        preferences.getString("apiKey", null)

    private fun extractUseCloudSync(preferences: SharedPreferences) =
        preferences.getBoolean("useCloudSync", false)
}