package tk.jonathancowling.inventorytracker.inventorylist.additem

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class AddItemObservable : BaseObservable() {

    private var name = ""
    private var quantity = ""

    @Bindable
    fun getName() = name

    fun setName(name: String) {
        if (this.name == name) {
            return
        }
        this.name = name
        notifyPropertyChanged(BR.name)
    }

    @Bindable
    fun getQuantity() = quantity

    fun setQuantity(quantity: String) {

        if (this.quantity == quantity) {
            return
        }

        this.quantity = quantity
        notifyPropertyChanged(BR.quantity)
    }
}

