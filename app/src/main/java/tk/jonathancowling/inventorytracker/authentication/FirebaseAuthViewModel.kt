package tk.jonathancowling.inventorytracker.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.android.schedulers.AndroidSchedulers
import tk.jonathancowling.inventorytracker.authentication.services.AuthService
import tk.jonathancowling.inventorytracker.authentication.services.FirebaseAuthMechanisms
import tk.jonathancowling.inventorytracker.authentication.services.FirebaseAuthService
import tk.jonathancowling.inventorytracker.util.rx.Backoff
import tk.jonathancowling.inventorytracker.util.rx.publisherFromObservable

internal class FirebaseAuthViewModel private constructor(
    private val authService: AuthService<FirebaseAuth, FirebaseUser>
) : ViewModel() {

    private var isStartingSession = false

    var currentUser: FirebaseUser? = null

    val user: LiveData<AuthState> = LiveDataReactiveStreams
        .fromPublisher(publisherFromObservable(authService.authState
            .compose(Backoff.Linear(1))
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                when (it) {
                    is AuthService.AuthState.Authenticated<FirebaseUser> -> {
                        currentUser = it.user
                        AuthState.LoggedIn(it.user)
                    }
                    is AuthService.AuthState.Unauthenticated<FirebaseUser> -> {
                        currentUser = null
                        if (isStartingSession) {
                            AuthState.Failed
                        } else {
                            AuthState.LoggedOut
                        }
                    }
                    is AuthService.AuthState.Failed -> {
                        currentUser = null
                        AuthState.Failed
                    }
                }.also {
                    isStartingSession = false
                }
            }
        ))

    fun login(email: String, password: String) {
        isStartingSession = true
        authService.changeAuthState(FirebaseAuthMechanisms.loginEmailPassword(email, password))
    }

    fun anon() {
        isStartingSession = true
        authService.changeAuthState(FirebaseAuthMechanisms.continueAnonymously())
    }

    fun signUp(email: String, password: String) {
        isStartingSession = true
        authService.changeAuthState(FirebaseAuthMechanisms.signUpEmailPassword(email, password))
    }

    fun logout() {
        authService.changeAuthState(FirebaseAuthMechanisms.logout())
    }

    internal sealed class AuthState {
        data class LoggedIn(val user: FirebaseUser) : AuthState()
        object LoggedOut : AuthState()
        object Failed : AuthState()
    }

    internal class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return FirebaseAuthViewModel(FirebaseAuthService(FirebaseAuth.getInstance())) as T
        }
    }
}

