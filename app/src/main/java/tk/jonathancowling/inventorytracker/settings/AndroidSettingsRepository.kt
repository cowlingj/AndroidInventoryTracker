package tk.jonathancowling.inventorytracker.settings

import android.content.SharedPreferences
import io.reactivex.Observable
import tk.jonathancowling.inventorytracker.util.Optional

class AndroidSettingsRepository(sharedPreferences: Observable<SharedPreferences>): SettingsRepository {

    override val settings: Observable<SettingsRepository.SettingsModel> = sharedPreferences.map {
        SettingsRepository.SettingsModel(extractBaseUrl(it), extractApiKey(it), extractUseCloudSync(it))
    }

    private fun extractBaseUrl(preferences: SharedPreferences) =
        preferences.getString("baseUrl", null).let {
            if (it.isNullOrEmpty()) {
                return@let Optional.empty<String>()
            }

            if (it.last() != '/') {
                return@let Optional.of("$it/")
            }

            return@let Optional.of(it)
        }

    private fun extractApiKey(preferences: SharedPreferences) =
        preferences.getString("apiKey", null).let {
            if (it.isNullOrEmpty()) {
                return@let Optional.empty<String>()
            }

            return@let Optional.of(it)
        }

    private fun extractUseCloudSync(preferences: SharedPreferences) =
        preferences.getBoolean("useCloudSync", false)
}