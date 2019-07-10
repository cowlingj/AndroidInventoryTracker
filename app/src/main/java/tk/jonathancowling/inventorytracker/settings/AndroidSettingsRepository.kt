package tk.jonathancowling.inventorytracker.settings

import android.content.SharedPreferences
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class AndroidSettingsRepository(sharedPreferences: Observable<SharedPreferences>) : SettingsRepository {

    override val settings: Observable<SettingsRepository.SettingsModel> = sharedPreferences.lift { downstream ->
        object : Observer<SharedPreferences>,
            SharedPreferences.OnSharedPreferenceChangeListener {

            private var currentSharedPreferences: SharedPreferences? = null

            override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
                emit()
            }


            override fun onComplete() {
                swapPreferences(null)
                downstream.onComplete()
            }

            override fun onSubscribe(d: Disposable) {
                downstream.onSubscribe(d)
            }

            override fun onError(e: Throwable) {
                swapPreferences(null)
                downstream.onError(e)
            }

            override fun onNext(it: SharedPreferences) {
                swapPreferences(it)
                emit()
            }

            private fun swapPreferences(newSharedPreferences: SharedPreferences?) {
                currentSharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
                currentSharedPreferences = newSharedPreferences
                currentSharedPreferences?.registerOnSharedPreferenceChangeListener(this)
            }

            private fun emit() {
                currentSharedPreferences?.let {
                    downstream.onNext(
                        SettingsRepository.SettingsModel(
                            extractBaseUrl(it),
                            extractApiKey(it),
                            extractUseCloudSync(it)
                        )
                    )
                }
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
    }
}