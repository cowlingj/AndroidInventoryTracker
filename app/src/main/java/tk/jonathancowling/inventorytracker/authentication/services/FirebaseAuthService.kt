package tk.jonathancowling.inventorytracker.authentication.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import io.reactivex.SingleOperator
import tk.jonathancowling.inventorytracker.authentication.services.AuthService
import tk.jonathancowling.inventorytracker.util.Optional

class FirebaseAuthService(private val auth: FirebaseAuth) :
    AuthService<FirebaseAuth, FirebaseUser> {

    override fun login(authMechanism: SingleOperator<Unit, FirebaseAuth>) =
        Single.just(auth).lift(authMechanism)

    override fun logout(authMechanism: SingleOperator<Unit, FirebaseAuth>) =
        Single.just(auth).lift(authMechanism)

    override fun signUp(authMechanism: SingleOperator<Unit, FirebaseAuth>) =
        Single.just(auth).lift(authMechanism)

    override fun isLoggedIn() = auth.currentUser != null

    override fun withUser() = Optional.ofNullable(auth.currentUser)

    class Factory {
        fun create() = FirebaseAuthService(FirebaseAuth.getInstance())
    }
}