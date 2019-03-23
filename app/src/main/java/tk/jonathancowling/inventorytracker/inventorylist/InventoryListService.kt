package tk.jonathancowling.inventorytracker.inventorylist

import androidx.lifecycle.LiveData

interface InventoryListService {
    val inventoryList: LiveData<out List<ListItem>>
    fun add(name: String, quantity: Int): ListItem
    fun remove(id: Int): ListItem
    fun find(id: Int): ListItem
    fun pager(pageSize: Int): () -> Unit
    fun all()
    fun changeName(id: Int, newName: String)
    fun changeQuantity(id: Int, newQuantity: Int)
}
