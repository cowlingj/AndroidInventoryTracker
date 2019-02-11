package tk.jonathancowling.inventorytracker.inventorylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tk.jonathancowling.inventorytracker.util.splice

class InventoryListObservable : ViewModel() {
    private val list: MutableLiveData<List<DataListItem>> = MutableLiveData()

    fun getData(): LiveData<List<DataListItem>> = list

    init {
        list.value = listOf()
    }

    fun addItem(name: String, quantity: Int) {
        val newId = list.value.orEmpty().firstOrNull { !it.isInUse }
            ?.id.apply { list.value = list.value?.filter { it.id != this } }
            ?: list.value?.size
            ?: 0

        list.value = listOf(DataListItem(newId, name, quantity, true)) + (list.value ?: emptyList())
    }

    fun removeItem(id: Int) {
        list.value  = list.value.orEmpty() - ((list.value?: emptyList()).single { item -> item.id == id })
    }


    fun changeName(id: Int, newName: String) {
        val replaceAtIndex = list.value.orEmpty().indexOf(list.value.orEmpty().single { it.id == id })
        list.value = list.value.orEmpty().splice(replaceAtIndex, 1,
                listOf(DataListItem(
                    list.value.orEmpty()[replaceAtIndex].id,
                    newName,
                    list.value.orEmpty()[replaceAtIndex].quantity,
                    true
                )))
    }

    fun changeQuantity(id: Int, newQuantity: Int) {
        val replaceAtIndex = list.value.orEmpty().indexOf(list.value.orEmpty().single { it.id == id })
        list.value = list.value.orEmpty().splice(replaceAtIndex, 1,
                listOf(DataListItem(
                    list.value.orEmpty()[replaceAtIndex].id,
                    list.value.orEmpty()[replaceAtIndex].name,
                    newQuantity,
                    true
                )))
    }
}