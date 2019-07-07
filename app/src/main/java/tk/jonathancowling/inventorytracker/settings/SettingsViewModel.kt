package tk.jonathancowling.inventorytracker.settings

import android.content.SharedPreferences
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable

class SettingsViewModel(sharedPreferences: Observable<NamedSharedPreferences>) : ViewModel() {
    val repository: SettingsRepository = AndroidSettingsRepository(sharedPreferences.map { it.preferences })
    val sharedPreferencesName = LiveDataReactiveStreams
        .fromPublisher(sharedPreferences.map { it.name }.toFlowable(BackpressureStrategy.LATEST))

    class Factory(
        private val sharedPreferences: Observable<NamedSharedPreferences>
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(sharedPreferences) as T
        }
    }

    data class NamedSharedPreferences(val name: String, val preferences: SharedPreferences)
}
