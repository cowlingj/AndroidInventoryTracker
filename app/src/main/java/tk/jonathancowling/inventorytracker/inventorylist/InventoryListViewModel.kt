package tk.jonathancowling.inventorytracker.inventorylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import io.reactivex.Observable
import tk.jonathancowling.inventorytracker.clients.list.models.Item

class InventoryListViewModel(private val listService: InventoryListService) : ViewModel() {

    fun getErrors(): LiveData<out Throwable> = listService.errors

    fun getData(): LiveData<out PagedList<Item>> = listService.inventoryList

    fun addItem(name: String, quantity: Int) = listService.add(name, quantity)

    fun removeItem(id: String) = listService.remove(id)

    class Factory(private val listService: InventoryListService) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) = InventoryListViewModel(listService) as T

    }

}