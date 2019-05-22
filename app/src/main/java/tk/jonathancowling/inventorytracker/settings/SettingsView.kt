package tk.jonathancowling.inventorytracker.settings

import android.os.Bundle
import android.text.InputType
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import tk.jonathancowling.inventorytracker.BuildConfig
import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.authentication.FirebaseAuthViewModel

class SettingsView : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        val authVM = ViewModelProviders.of(requireActivity(), FirebaseAuthViewModel.Factory()).get<FirebaseAuthViewModel>()

        authVM.user.observe(this, Observer {
            when (it) {
                is FirebaseAuthViewModel.AuthState.LoggedIn -> {
                    preferenceManager.sharedPreferencesName = """${it.user.uid}.${BuildConfig.APPLICATION_ID}"""

                    setPreferencesFromResource(R.xml.sync_preferences, rootKey)
                    findPreference<EditTextPreference>("apiKey")!!.setOnBindEditTextListener { editText ->
                        editText.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }
                }
                is FirebaseAuthViewModel.AuthState.LoggedOut -> {
                    findNavController().navigate(R.id.login_destination)
                }
            }
        })
    }
}