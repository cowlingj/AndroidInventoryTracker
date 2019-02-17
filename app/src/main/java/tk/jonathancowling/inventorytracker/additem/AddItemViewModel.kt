package tk.jonathancowling.inventorytracker.additem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tk.jonathancowling.inventorytracker.util.TripleState
import tk.jonathancowling.inventorytracker.util.addOnPropertyChangedCallback
import java.lang.IllegalStateException

class AddItemViewModel(addItem: AddItemObservable) : ViewModel() {
    private val model = MutableLiveData<Model>()
    val data: LiveData<Model> = model

    init {
        addItem.addOnPropertyChangedCallback { sender: AddItemObservable, _: Int ->
            sender.let {
                model.value = Model(it.getName(), it.getQuantity())
            }
        }
        model.value = Model(addItem.getName(), addItem.getQuantity())
    }

    fun isValid(): Boolean = (!model.value?.name.isNullOrEmpty() && model.value?.quantity?.isDataState() == true)

    fun validate(valid: (name: String, quantity: Int)->Unit, invalid: ()->Unit = {}) {
            TripleState.fromDataOrEmpty(model.value).transformWithMatchingState({ model ->
                if (!isValid()) {
                    throw IllegalStateException()
                } else {
                    model.quantity.dataToData { quantity ->
                        ValidatedModel(model.name, quantity)
                    }
                }
            }).tapWithMatchingState({ valid(it.name, it.quantity) }, { invalid() }, { invalid() })
    }

    private data class ValidatedModel(val name: String, val quantity: Int)

    class Factory(private val addItem: AddItemObservable) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) = AddItemViewModel(addItem) as T
    }
}