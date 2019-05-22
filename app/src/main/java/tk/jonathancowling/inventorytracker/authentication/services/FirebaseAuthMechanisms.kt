package tk.jonathancowling.inventorytracker.authentication.services

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.SingleObserver
import io.reactivex.SingleOperator
import io.reactivex.disposables.Disposable
import java.io.IOException

object FirebaseAuthMechanisms {

    fun loginEmailPassword(email: String, password: String) =
        useEmailPassword(
            email,
            password,
            FirebaseAuth::signInWithEmailAndPassword
        )

    fun signUpEmailPassword(email: String, password: String) =
        useEmailPassword(
            email,
            password,
            FirebaseAuth::createUserWithEmailAndPassword
        )

    fun continueAnonymously() =
        SingleOperator { observer: SingleObserver<in AuthService.AuthState<FirebaseUser>> ->
            object : SingleObserver<FirebaseAuth> {
                override fun onSuccess(auth: FirebaseAuth) {
                    auth.signInAnonymously().addOnSuccessListener {
                        auth.addAuthStateListener(object : FirebaseAuth.AuthStateListener {
                            override fun onAuthStateChanged(auth: FirebaseAuth) {
                                auth.removeAuthStateListener(this)

                                auth.currentUser?.let {
                                    observer.onSuccess(AuthService.AuthState.Authenticated(it))
                                } ?: let {
                                    observer.onError(IOException())
                                }
                            }
                        })
                    }.addOnFailureListener { observer.onError(it) }
                }

                override fun onSubscribe(d: Disposable) {
                    observer.onSubscribe(d)
                }

                override fun onError(e: Throwable) {
                    observer.onError(e)
                }

            }
        }

    fun logout() =
        SingleOperator { observer: SingleObserver<in AuthService.AuthState<FirebaseUser>> ->
            object : SingleObserver<FirebaseAuth> {
                override fun onSuccess(t: FirebaseAuth) {
                    t.apply {
                        addAuthStateListener(object : FirebaseAuth.AuthStateListener {
                            override fun onAuthStateChanged(auth: FirebaseAuth) {
                                auth.removeAuthStateListener(this)

                                auth.currentUser?.let {
                                    observer.onError(IOException())
                                } ?: let {
                                    observer.onSuccess(AuthService.AuthState.Unauthenticated())
                                }
                            }
                        })
                        signOut()
                    }
                }

                override fun onSubscribe(d: Disposable) {
                    observer.onSubscribe(d)
                }

                override fun onError(e: Throwable) {
                    observer.onError(e)
                }

            }
        }

    private fun useEmailPassword(
        email: String,
        password: String,
        useFor: (auth: FirebaseAuth, email: String, password: String) -> Task<AuthResult>
    ) =
        SingleOperator { observer: SingleObserver<in AuthService.AuthState<FirebaseUser>> ->
            object : SingleObserver<FirebaseAuth> {
                override fun onSuccess(auth: FirebaseAuth) {
                    try {
                        useFor(auth, email, password).addOnSuccessListener {
                            auth.addAuthStateListener(object : FirebaseAuth.AuthStateListener {
                                override fun onAuthStateChanged(auth: FirebaseAuth) {
                                    auth.removeAuthStateListener(this)

                                    auth.currentUser?.let {
                                        observer.onSuccess(AuthService.AuthState.Authenticated(it))
                                    } ?: let {
                                        observer.onError(IOException())
                                    }
                                }
                            })
                        }.addOnFailureListener { observer.onError(it) }
                    } catch (t: Throwable) {
                        observer.onError(t)
                    }
                }

                override fun onSubscribe(d: Disposable) {
                    observer.onSubscribe(d)
                }

                override fun onError(e: Throwable) {
                    observer.onError(e)
                }
            }
        }
}
