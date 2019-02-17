package tk.jonathancowling.inventorytracker.util

import androidx.databinding.BaseObservable
import androidx.databinding.Observable

fun <T> BaseObservable.addOnPropertyChangedCallback(lambda: (sender: T, propertyId: Int)->Unit) =
    object : Observable.OnPropertyChangedCallback() {
        @Suppress("UNCHECKED_CAST")
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            lambda(sender as T, propertyId)
        }
    }.apply {
        addOnPropertyChangedCallback(this)
    }
