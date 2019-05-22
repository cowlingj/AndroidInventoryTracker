package tk.jonathancowling.inventorytracker.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import tk.jonathancowling.inventorytracker.authentication.models.ValidationException
import tk.jonathancowling.inventorytracker.authentication.services.AuthService
import tk.jonathancowling.inventorytracker.authentication.services.DaggerAuthComponent
import tk.jonathancowling.inventorytracker.authentication.services.FirebaseAuthMechanisms
import tk.jonathancowling.inventorytracker.util.Exceptions.AuthenticationException
import tk.jonathancowling.inventorytracker.util.rx.AutoCompositeDisposable
import tk.jonathancowling.inventorytracker.util.rx.Backoff
import tk.jonathancowling.inventorytracker.util.rx.plus

class FirebaseAuthViewModel private constructor(private val authService: AuthService<FirebaseAuth, FirebaseUser>) : ViewModel() {

    private val _user = MutableLiveData<AuthState>()
    val user: LiveData<AuthState> = _user

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    private val disposables: CompositeDisposable by AutoCompositeDisposable()

    private var isStartingSession = false

    init {
        disposables + authService.authState
            .compose(Backoff.Linear(1))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                when (it) {
                    is AuthService.AuthState.Authenticated<FirebaseUser> -> {
                        isStartingSession = false
                        _user.value = AuthState.LoggedIn(it.user)
                    }
                    is AuthService.AuthState.Unauthenticated<FirebaseUser> -> {
                        if (isStartingSession) {
                            _error.value = AuthenticationException()
                            _error.value = null
                        } else {
                            _user.value = AuthState.LoggedOut
                        }
                    }
                }
                isStartingSession = false
            }
    }

    private fun isValidPassword(password: String): Boolean {
        return password.matches(Regex(".{8,}"))
    }

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
        if (isValidPassword(password)) {
            authService.changeAuthState(FirebaseAuthMechanisms.signUpEmailPassword(email, password))
        } else {
            _error.value = ValidationException()
            _user.value = AuthState.LoggedOut
            _error.value = null
        }
    }

    fun logout() {
        authService.changeAuthState(FirebaseAuthMechanisms.logout())
    }

    sealed class AuthState {
        data class LoggedIn(val user: FirebaseUser) : AuthState()
        object LoggedOut : AuthState()
    }

    class Factory: ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return FirebaseAuthViewModel(DaggerAuthComponent.create().authService()) as T
        }
    }
}

