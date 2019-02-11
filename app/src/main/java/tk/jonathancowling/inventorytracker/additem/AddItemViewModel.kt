package tk.jonathancowling.inventorytracker.additem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddItemViewModel : ViewModel() {
    private val model = MutableLiveData<Model>()
    val data: LiveData<Model> = model

    fun setData(newData: AddItemImmutableObservable) {
        model.value = Model(newData.getName(), newData.getQuantity() ?: 0)
    }

    fun isValid(): Boolean = true
}