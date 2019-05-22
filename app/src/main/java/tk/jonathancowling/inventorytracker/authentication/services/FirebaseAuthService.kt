package tk.jonathancowling.inventorytracker.authentication.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import io.reactivex.SingleOperator
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

internal class FirebaseAuthService(private val auth: FirebaseAuth) :
    AuthService<FirebaseAuth, FirebaseUser> {

    override val authState = BehaviorSubject
        .create<AuthService.AuthState<FirebaseUser>>()

    init {
        auth.currentUser
            ?.let { authState.onNext(AuthService.AuthState.Authenticated(it)) }
            ?: let { authState.onNext(AuthService.AuthState.Unauthenticated()) }
    }

    override fun changeAuthState(authMechanism: SingleOperator<AuthService.AuthState<FirebaseUser>, FirebaseAuth>): Disposable {
        return Single.just(auth).lift(authMechanism).subscribe({
            authState.onNext(it)
        }, {
            authState.onNext(AuthService.AuthState.Unauthenticated())
        })
    }
}