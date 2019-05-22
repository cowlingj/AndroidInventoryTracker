package tk.jonathancowling.inventorytracker.settings

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.Module
import dagger.Provides
import tk.jonathancowling.inventorytracker.authentication.services.AuthService

@Module
class SettingsModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun repository(
            auth: AuthService<FirebaseAuth, FirebaseUser>,
            context: Context
        ): SettingsRepository {

            return SharedPreferencesProvider(context, auth).get()
        }
    }
}