package tk.jonathancowling.inventorytracker.authentication

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import tk.jonathancowling.inventorytracker.BR
import tk.jonathancowling.inventorytracker.authentication.models.Model

class EmailPasswordObservable : BaseObservable() {

    var data = Model("", "")

    @Bindable fun getEmail() = data.email

    fun setEmail(value: String) {
        if (data.email == value) {
            return
        }

        data = data.copy(email = value)
        notifyPropertyChanged(BR.email)
    }


    @Bindable fun getPassword() = data.password

    fun setPassword(value: String) {
        if (data.password == value) {
            return
        }

        data = data.copy(password = value)
        notifyPropertyChanged(BR.password)
    }
}