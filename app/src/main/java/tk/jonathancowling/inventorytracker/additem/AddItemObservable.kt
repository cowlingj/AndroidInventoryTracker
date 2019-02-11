package tk.jonathancowling.inventorytracker.additem

import android.util.Log
import androidx.databinding.*
import androidx.databinding.library.baseAdapters.BR

class AddItemObservable : BaseObservable(), AddItemImmutableObservable {

    private var name = ""
    private var quantity: Int? = null

    @Bindable
    override fun getName() = name

    fun setName(name: String) {
        Log.i("OBSERVABLE", "maybe setting name")
        if (this.name == name) {
            return
        }
        Log.i("OBSERVABLE", "actually setting name")
        this.name = name
        notifyPropertyChanged(BR.name)
    }

    @Bindable
    override fun getQuantity() = quantity

    fun setQuantity(quantity: Int?) {
        Log.i("OBSERVABLE", "maybe setting quant")
        if (this.quantity?.equals(quantity) == true) {
            return
        }
        Log.i("OBSERVABLE", "actually setting quant")
        this.quantity = quantity
        notifyPropertyChanged(BR.quantity)
    }
}

