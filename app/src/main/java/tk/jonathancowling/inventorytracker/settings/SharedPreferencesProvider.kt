package tk.jonathancowling.inventorytracker.settings

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import tk.jonathancowling.inventorytracker.BuildConfig
import tk.jonathancowling.inventorytracker.authentication.services.AuthService

internal class SharedPreferencesProvider(
    private val context: Context,
    private val auth: AuthService<FirebaseAuth, FirebaseUser>
) {

    private val defaultPrefs =
        context.applicationContext.getSharedPreferences(
            BuildConfig.APPLICATION_ID,
            Context.MODE_PRIVATE
        )

    private fun getName(user: FirebaseUser) = """${user.uid}.${BuildConfig.APPLICATION_ID}"""

    fun get(): SettingsRepository {

        return AndroidSettingsRepository(auth.authState.map {
            return@map if (it is AuthService.AuthState.Authenticated) {
                context.applicationContext.getSharedPreferences(
                    getName(it.user),
                    Context.MODE_PRIVATE
                )
            } else {
                defaultPrefs
            }
        }.onErrorReturn {
            defaultPrefs
        })
    }
}