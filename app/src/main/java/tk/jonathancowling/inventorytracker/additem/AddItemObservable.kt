package tk.jonathancowling.inventorytracker.additem

import androidx.databinding.*
import androidx.databinding.library.baseAdapters.BR
import tk.jonathancowling.inventorytracker.util.TripleState

class AddItemObservable : BaseObservable() {

    private var name = ""
    private var quantity: TripleState<Int> = TripleState.fromEmpty()

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

    fun setQuantity(quantity: TripleState<Int>) {

        if (this.quantity == quantity) {
            return
        }

        if (!this.quantity.isErrorState() && quantity.isErrorState()) {
            notifyPropertyChanged(BR.quantity)
            return
        }

        this.quantity = quantity
        notifyPropertyChanged(BR.quantity)
    }
}

