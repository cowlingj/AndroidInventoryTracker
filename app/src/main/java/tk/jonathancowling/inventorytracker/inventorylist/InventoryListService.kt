package tk.jonathancowling.inventorytracker.inventorylist

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import tk.jonathancowling.inventorytracker.clients.list.models.Item

interface InventoryListService {
    val inventoryList: LiveData<out PagedList<Item>>
    val errors: LiveData<out Throwable>
    fun add(name: String, quantity: Int): Single<out Item>
    fun remove(id: String): Single<out Item>
    fun find(id: String): Single<out Item>
    fun changeName(id: String, newName: String): Single<out Item>
    fun changeQuantity(id: String, newQuantity: Int): Single<out Item>
}
