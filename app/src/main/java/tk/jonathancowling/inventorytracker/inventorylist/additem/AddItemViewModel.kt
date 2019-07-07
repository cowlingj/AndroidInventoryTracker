package tk.jonathancowling.inventorytracker.inventorylist.additem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tk.jonathancowling.inventorytracker.util.addOnPropertyChangedCallback

class AddItemViewModel(addItem: AddItemObservable) : ViewModel() {
    private val _data = MutableLiveData<ModelWithValidation>()
    val data: LiveData<ModelWithValidation> = _data

    init {
        addItem.addOnPropertyChangedCallback { sender: AddItemObservable, _: Int ->
            _data.value = isValid(sender.getName(), sender.getQuantity())
        }
        _data.value = isValid(addItem.getName(), addItem.getQuantity())
    }

    private fun isValid(name: String, quantity: String): ModelWithValidation {
        if (name.isEmpty() || name.isBlank()) {
            return ModelWithValidation.InvalidModel(mapOf("name" to EmptyRequiredField()))
        }

        if (quantity.isEmpty() || quantity.isBlank()) {
           return ModelWithValidation.InvalidModel(mapOf("quantity" to EmptyRequiredField()))
        }

        return try {
            ModelWithValidation.ValidModel(name, Integer.parseInt(quantity))
        } catch (nfe: NumberFormatException) {
            ModelWithValidation.InvalidModel(mapOf("quantity" to nfe))
        }
    }

    sealed class ModelWithValidation {
        data class ValidModel(val name: String, val quantity: Int) : ModelWithValidation()
        data class InvalidModel(val errors: Map<String, Throwable>) : ModelWithValidation()
    }

    class Factory(private val addItem: AddItemObservable) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) = AddItemViewModel(addItem) as T
    }

    class EmptyRequiredField : Throwable()
}