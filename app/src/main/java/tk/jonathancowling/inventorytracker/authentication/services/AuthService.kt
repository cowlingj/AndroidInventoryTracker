package tk.jonathancowling.inventorytracker.authentication.services

import io.reactivex.Single
import io.reactivex.SingleOperator
import tk.jonathancowling.inventorytracker.util.Optional

interface AuthService<ProviderT, UserT> {
    fun login(authMechanism: SingleOperator<Unit, ProviderT>): Single<Unit>
    fun logout(authMechanism: SingleOperator<Unit, ProviderT>): Single<Unit>
    fun signUp(authMechanism: SingleOperator<Unit, ProviderT>): Single<Unit>
    fun withUser(): Optional<UserT>
    fun isLoggedIn(): Boolean
}