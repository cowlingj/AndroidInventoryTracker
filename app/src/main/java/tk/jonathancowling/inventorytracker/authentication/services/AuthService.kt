package tk.jonathancowling.inventorytracker.authentication.services

import io.reactivex.Observable
import io.reactivex.SingleOperator
import io.reactivex.disposables.Disposable

interface AuthService<ProviderT, UserT> {
    fun changeAuthState(authMechanism: SingleOperator<AuthState<UserT>, ProviderT>): Disposable
    val authState: Observable<AuthState<UserT>>

    sealed class AuthState<UserT> {
        data class Authenticated<UserT>(val user: UserT) : AuthState<UserT>()
        class Unauthenticated<UserT> : AuthState<UserT>()
    }
}