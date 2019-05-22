package tk.jonathancowling.inventorytracker.authentication.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AuthModule {

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun auth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }

        @JvmStatic
        @Singleton
        @Provides
        fun service(auth: FirebaseAuth): AuthService<FirebaseAuth, FirebaseUser> {
            return FirebaseAuthService(auth)
        }
    }
}