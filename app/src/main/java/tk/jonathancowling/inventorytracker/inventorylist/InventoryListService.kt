package tk.jonathancowling.inventorytracker.inventorylist

import androidx.lifecycle.LiveData
import io.reactivex.ObservableSource
import io.reactivex.Single
import tk.jonathancowling.inventorytracker.listclient.models.Item

interface InventoryListService {
    val inventoryList: LiveData<out List<Item>>
    fun add(name: String, quantity: Int): Single<out Item>
    fun remove(id: String): Single<out Item>
    fun find(id: String): Single<out Item>
    fun pager(pageSize: Int): ObservableSource<Unit>
    fun all(): Single<Unit>
    fun changeName(id: String, newName: String): Single<out Item>
    fun changeQuantity(id: String, newQuantity: Int): Single<out Item>
}
