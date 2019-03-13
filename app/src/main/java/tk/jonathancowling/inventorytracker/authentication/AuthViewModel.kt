package tk.jonathancowling.inventorytracker.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import tk.jonathancowling.inventorytracker.BR
import tk.jonathancowling.inventorytracker.util.AutoDisposable
import tk.jonathancowling.inventorytracker.util.ValidationException
import tk.jonathancowling.inventorytracker.util.addOnPropertyChangedCallback
import java.lang.IllegalArgumentException

class AuthViewModel(observable: EmailPasswordObservable) : ViewModel() {

    private val model = MutableLiveData<Model>()
    val data: LiveData<Model> = model
    private val disposable: CompositeDisposable by AutoDisposable.Composite()

    init {
        observable.addOnPropertyChangedCallback<EmailPasswordObservable> { sender, propertyId ->
            if (propertyId != BR.email && propertyId != BR.password) {
                return@addOnPropertyChangedCallback
            }

            model.value = sender.data
        }
    }

    fun validate(): Single<Model> = doValidate().doOnSubscribe { disposable.add(it) }
    private fun doValidate(): Single<Model> {
        if (data.value == null) {
            return Single.error(InvalidModel(isEmailInvalid = true, isPasswordInvalid = true))
        }

        val invalidEmail = !isValidEmail(data.value?.email)
        val invalidPassword = !isValidPassword(data.value?.password)

        if (invalidEmail || invalidPassword) {
            return Single.error(InvalidModel(invalidEmail, invalidPassword))
        }

        return Single.just(data.value!!)
    }

    private fun isValidPassword(password: String?): Boolean {
        return password?.length?.let { it >= 8 } ?: false
    }

    private fun isValidEmail(email: String?): Boolean {
        return email?.matches(Regex("[a-zA-Z0-9.]+@[a-zA-Z0-9.]+.[a-zA-Z]+")) ?: false
    }

    data class InvalidModel(val isEmailInvalid: Boolean, val isPasswordInvalid: Boolean) : ValidationException()

    class Factory(private val observable: EmailPasswordObservable): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass != AuthViewModel::class.java) {
                throw IllegalArgumentException("${modelClass.name} is not an ${AuthViewModel::class.java.simpleName}")
            }

            return AuthViewModel(observable) as T
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}