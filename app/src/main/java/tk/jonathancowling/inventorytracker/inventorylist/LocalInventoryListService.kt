package tk.jonathancowling.inventorytracker.inventorylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tk.jonathancowling.inventorytracker.util.prepend

class LocalInventoryListService : InventoryListService {

    private val data: MutableLiveData<List<ListItem>> = MutableLiveData()
    private val shadowList = mutableListOf<AndroidListItem>()

    override val inventoryList: LiveData<List<ListItem>> = data

    init {
        data.value = emptyList()
    }

    override fun add(name: String, quantity: Int) = shadowList
        .indexOfFirst { !it.isInUse }
        .let { index ->
            AndroidListItem(if (index == -1) shadowList.size else index.also {
                shadowList.removeAt(index)
            }, name, quantity )
        }.also {
            shadowList.prepend(it)
            sync(data.value.orEmpty().size + 1)
        }

    override fun remove(id: Int) = shadowList
        .single{ it.id == id && it.isInUse }
        .also {
            it.isInUse = false
            sync()
        }

    override fun find(id: Int) = data.value.orEmpty().single { it.id == id }

    override fun pager(pageSize: Int) = {
        if (data.value.orEmpty().size != shadowList.size) {
            sync(pageSize + data.value.orEmpty().size)
        }
    }

    override fun all() {
        sync(shadowList.size)
    }

    override fun changeName(id: Int, newName: String) {
        shadowList
            .mapIndexed { i, item -> Pair(i, item) }
            .single { it.second.id == id && it.second.isInUse }
            .let {
                shadowList[it.first] = AndroidListItem(it.second.id, newName, it.second.quantity)
            }
        sync()
    }

    override fun changeQuantity(id: Int, newQuantity: Int) {
        shadowList
            .mapIndexed { i, item -> Pair(i, item) }
            .single { it.second.id == id && it.second.isInUse }
            .let {
                shadowList[it.first] = AndroidListItem(it.second.id, it.second.name, newQuantity)
            }
        sync()
    }

    private fun sync(numberToSync: Int = data.value.orEmpty().size) {
        data.value = shadowList.filter { it.isInUse }.take(numberToSync)
    }
}