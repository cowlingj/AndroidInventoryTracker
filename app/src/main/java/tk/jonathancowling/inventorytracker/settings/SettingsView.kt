package tk.jonathancowling.inventorytracker.settings

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.util.existingScope

class SettingsView : PreferenceFragmentCompat() {

    private val userScope by existingScope()

    private val settingsViewModel by viewModels<SettingsViewModel>(
        ownerProducer = { ViewModelStoreOwner { userScope.store } }
    )

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        settingsViewModel.sharedPreferencesName.observe(this, Observer {
            preferenceManager.sharedPreferencesName = it

            setPreferencesFromResource(R.xml.sync_preferences, rootKey)

            findPreference<EditTextPreference>("apiKey")!!.setOnBindEditTextListener { editText ->
                editText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        })
    }
}