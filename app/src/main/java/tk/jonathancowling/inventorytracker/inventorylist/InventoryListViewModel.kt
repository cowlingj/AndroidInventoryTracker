package tk.jonathancowling.inventorytracker.inventorylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tk.jonathancowling.inventorytracker.listclient.Item

class InventoryListViewModel(private val listService: InventoryListService) : ViewModel() {

    fun getData(): LiveData<out List<Item>> = listService.inventoryList

    val pager by lazy { listService.pager(10) }

    fun addItem(name: String, quantity: Int) = listService.add(name, quantity)

    fun removeItem(id: String) = listService.remove(id)

    class Factory(private val listService: InventoryListService) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) = InventoryListViewModel(listService) as T

    }

}