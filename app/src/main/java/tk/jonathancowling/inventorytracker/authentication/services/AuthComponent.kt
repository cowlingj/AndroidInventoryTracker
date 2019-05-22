package tk.jonathancowling.inventorytracker.authentication.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AuthModule::class])
interface AuthComponent {

    fun authService(): AuthService<FirebaseAuth, FirebaseUser>
}